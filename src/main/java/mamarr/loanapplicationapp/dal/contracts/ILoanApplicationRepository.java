package mamarr.loanapplicationapp.dal.contracts;

import mamarr.loanapplicationapp.domain.ApplicationStatus;
import mamarr.loanapplicationapp.domain.LoanApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ILoanApplicationRepository {
    LoanApplication save(LoanApplication application);

    Optional<LoanApplication> findById(UUID id);

    Optional<LoanApplication> findByIdAndUserId(UUID id, UUID userId);

    List<LoanApplication> findByUserId(UUID userId);

    List<LoanApplication> findAll();

    List<LoanApplication> findAllByStatus(ApplicationStatus status);
}
