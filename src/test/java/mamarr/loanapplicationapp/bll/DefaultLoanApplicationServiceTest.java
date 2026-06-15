package mamarr.loanapplicationapp.bll;

import mamarr.loanapplicationapp.bll.contracts.CurrentUserService;
import mamarr.loanapplicationapp.dal.contracts.LoanApplicationRepository;
import mamarr.loanapplicationapp.domain.AppUser;
import mamarr.loanapplicationapp.domain.ApplicationStatus;
import mamarr.loanapplicationapp.domain.LoanApplication;
import mamarr.loanapplicationapp.domain.UserRole;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultLoanApplicationServiceTest {
    private final UUID currentUserId = UUID.randomUUID();
    private final AppUser currentUser = new AppUser(
            currentUserId,
            "user@example.com",
            "hash",
            "Regular",
            "User",
            UserRole.USER,
            Instant.now(),
            Instant.now()
    );
    private final FakeLoanApplicationRepository repository = new FakeLoanApplicationRepository();
    private final DefaultLoanApplicationService service = new DefaultLoanApplicationService(repository, () -> currentUser);

    @Test
    void createForCurrentUserCalculatesAndStartsPending() {
        LoanApplication application = service.createForCurrentUser(new CreateLoanApplicationCommand(
                new BigDecimal("10000"),
                48,
                new BigDecimal("1800"),
                new BigDecimal("700"),
                new BigDecimal("250"),
                "Car purchase"
        ));

        assertThat(application.userId()).isEqualTo(currentUserId);
        assertThat(application.status()).isEqualTo(ApplicationStatus.PENDING);
        assertThat(application.monthlyPayment()).isGreaterThan(BigDecimal.ZERO);
        assertThat(application.debtToIncomeRatio()).isGreaterThan(BigDecimal.ZERO);
        assertThat(repository.saved).containsExactly(application);
    }

    @Test
    void getForCurrentUserDoesNotReturnAnotherUsersApplication() {
        LoanApplication otherUsersApplication = TestApplications.application(UUID.randomUUID());
        repository.saved.add(otherUsersApplication);

        assertThatThrownBy(() -> service.getForCurrentUser(otherUsersApplication.id()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void adminCanUpdateStatus() {
        LoanApplication application = TestApplications.application(currentUserId);
        repository.saved.add(application);
        AppUser admin = new AppUser(
                UUID.randomUUID(),
                "admin@example.com",
                "hash",
                "Admin",
                "User",
                UserRole.ADMIN,
                Instant.now(),
                Instant.now()
        );
        DefaultLoanApplicationService adminService = new DefaultLoanApplicationService(repository, () -> admin);

        LoanApplication updated = adminService.updateStatus(application.id(), ApplicationStatus.APPROVED);

        assertThat(updated.status()).isEqualTo(ApplicationStatus.APPROVED);
        assertThat(repository.findById(application.id()).orElseThrow().status()).isEqualTo(ApplicationStatus.APPROVED);
    }

    @Test
    void nonAdminCannotUpdateStatus() {
        LoanApplication application = TestApplications.application(currentUserId);
        repository.saved.add(application);

        assertThatThrownBy(() -> service.updateStatus(application.id(), ApplicationStatus.APPROVED))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Admin");
    }

    private static class FakeLoanApplicationRepository implements LoanApplicationRepository {
        private final List<LoanApplication> saved = new ArrayList<>();

        @Override
        public LoanApplication save(LoanApplication application) {
            saved.removeIf(existing -> existing.id().equals(application.id()));
            saved.add(application);
            return application;
        }

        @Override
        public Optional<LoanApplication> findById(UUID id) {
            return saved.stream().filter(application -> application.id().equals(id)).findFirst();
        }

        @Override
        public Optional<LoanApplication> findByIdAndUserId(UUID id, UUID userId) {
            return saved.stream()
                    .filter(application -> application.id().equals(id) && application.userId().equals(userId))
                    .findFirst();
        }

        @Override
        public List<LoanApplication> findByUserId(UUID userId) {
            return saved.stream().filter(application -> application.userId().equals(userId)).toList();
        }

        @Override
        public List<LoanApplication> findAll() {
            return List.copyOf(saved);
        }

        @Override
        public List<LoanApplication> findAllByStatus(ApplicationStatus status) {
            return saved.stream().filter(application -> application.status() == status).toList();
        }
    }

    private static class TestApplications {
        static LoanApplication application(UUID userId) {
            Instant now = Instant.now();
            return new LoanApplication(
                    UUID.randomUUID(),
                    userId,
                    new BigDecimal("10000"),
                    48,
                    new BigDecimal("1800"),
                    new BigDecimal("700"),
                    new BigDecimal("250"),
                    new BigDecimal("0.0750"),
                    new BigDecimal("241.79"),
                    new BigDecimal("850.00"),
                    new BigDecimal("27.32"),
                    mamarr.loanapplicationapp.domain.RiskLevel.LOW,
                    ApplicationStatus.PENDING,
                    "Car purchase",
                    now,
                    now
            );
        }
    }
}
