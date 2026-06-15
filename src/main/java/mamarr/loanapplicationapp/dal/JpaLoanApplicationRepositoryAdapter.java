package mamarr.loanapplicationapp.dal;

import mamarr.loanapplicationapp.dal.contracts.LoanApplicationRepository;
import mamarr.loanapplicationapp.domain.ApplicationStatus;
import mamarr.loanapplicationapp.domain.LoanApplication;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaLoanApplicationRepositoryAdapter implements LoanApplicationRepository {
    private final LoanApplicationSpringDataRepository repository;
    private final UserSpringDataRepository userRepository;

    public JpaLoanApplicationRepositoryAdapter(
            LoanApplicationSpringDataRepository repository,
            UserSpringDataRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public LoanApplication save(LoanApplication application) {
        UserJpaEntity user = userRepository.findById(application.userId())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        return LoanApplicationMapper.toDomain(repository.save(LoanApplicationMapper.toEntity(application, user)));
    }

    @Override
    public Optional<LoanApplication> findById(UUID id) {
        return repository.findById(id).map(LoanApplicationMapper::toDomain);
    }

    @Override
    public Optional<LoanApplication> findByIdAndUserId(UUID id, UUID userId) {
        return repository.findByIdAndUserId(id, userId).map(LoanApplicationMapper::toDomain);
    }

    @Override
    public List<LoanApplication> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream().map(LoanApplicationMapper::toDomain).toList();
    }

    @Override
    public List<LoanApplication> findAll() {
        return repository.findAll().stream().map(LoanApplicationMapper::toDomain).toList();
    }

    @Override
    public List<LoanApplication> findAllByStatus(ApplicationStatus status) {
        return repository.findByStatus(status).stream().map(LoanApplicationMapper::toDomain).toList();
    }
}
