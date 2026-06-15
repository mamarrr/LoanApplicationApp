package mamarr.loanapplicationapp.bll.contracts;

import mamarr.loanapplicationapp.bll.CreateLoanApplicationCommand;
import mamarr.loanapplicationapp.domain.ApplicationStatus;
import mamarr.loanapplicationapp.domain.LoanApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanApplicationService {
    LoanApplication createForCurrentUser(CreateLoanApplicationCommand command);

    List<LoanApplication> listForCurrentUser();

    LoanApplication getForCurrentUser(UUID id);

    List<LoanApplication> listAll(Optional<ApplicationStatus> status);

    LoanApplication getByIdForAdmin(UUID id);

    LoanApplication updateStatus(UUID id, ApplicationStatus status);
}
