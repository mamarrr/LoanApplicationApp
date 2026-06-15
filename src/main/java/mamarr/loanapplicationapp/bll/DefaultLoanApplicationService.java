package mamarr.loanapplicationapp.bll;

import mamarr.loanapplicationapp.bll.contracts.CurrentUserService;
import mamarr.loanapplicationapp.bll.contracts.LoanApplicationService;
import mamarr.loanapplicationapp.dal.contracts.LoanApplicationRepository;
import mamarr.loanapplicationapp.domain.AppUser;
import mamarr.loanapplicationapp.domain.ApplicationStatus;
import mamarr.loanapplicationapp.domain.LoanApplication;
import mamarr.loanapplicationapp.domain.LoanCalculationResult;
import mamarr.loanapplicationapp.domain.LoanCalculator;
import mamarr.loanapplicationapp.domain.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultLoanApplicationService implements LoanApplicationService {
    private final LoanApplicationRepository repository;
    private final CurrentUserService currentUserService;
    private final LoanCalculator calculator;

    public DefaultLoanApplicationService(LoanApplicationRepository repository, CurrentUserService currentUserService) {
        this.repository = repository;
        this.currentUserService = currentUserService;
        this.calculator = new LoanCalculator();
    }

    @Override
    @Transactional
    public LoanApplication createForCurrentUser(CreateLoanApplicationCommand command) {
        AppUser user = currentUserService.currentUser();
        LoanCalculationResult calculation = calculator.calculate(
                command.amount(),
                command.termMonths(),
                command.monthlyIncome(),
                command.monthlyExpenses(),
                command.existingLiabilities()
        );
        LoanApplication application = LoanApplication.createPending(
                user.id(),
                command.amount(),
                command.termMonths(),
                command.monthlyIncome(),
                command.monthlyExpenses(),
                command.existingLiabilities(),
                command.purpose(),
                calculation
        );
        return repository.save(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplication> listForCurrentUser() {
        return repository.findByUserId(currentUserService.currentUser().id());
    }

    @Override
    @Transactional(readOnly = true)
    public LoanApplication getForCurrentUser(UUID id) {
        return repository.findByIdAndUserId(id, currentUserService.currentUser().id())
                .orElseThrow(() -> new ResourceNotFoundException("Loan application not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplication> listAll(Optional<ApplicationStatus> status) {
        requireAdmin();
        return status.map(repository::findAllByStatus).orElseGet(repository::findAll);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanApplication getByIdForAdmin(UUID id) {
        requireAdmin();
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan application not found"));
    }

    @Override
    @Transactional
    public LoanApplication updateStatus(UUID id, ApplicationStatus status) {
        requireAdmin();
        LoanApplication application = getByIdForAdmin(id);
        return repository.save(application.withStatus(status));
    }

    private void requireAdmin() {
        if (currentUserService.currentUser().role() != UserRole.ADMIN) {
            throw new SecurityException("Admin role is required");
        }
    }
}
