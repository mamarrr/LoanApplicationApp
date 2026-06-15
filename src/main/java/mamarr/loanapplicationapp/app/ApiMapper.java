package mamarr.loanapplicationapp.app;

import mamarr.loanapplicationapp.app.dto.AuthResponse;
import mamarr.loanapplicationapp.app.dto.LoanApplicationResponse;
import mamarr.loanapplicationapp.bll.AuthResult;
import mamarr.loanapplicationapp.domain.LoanApplication;

public final class ApiMapper {
    private ApiMapper() {
    }

    public static AuthResponse toResponse(AuthResult result) {
        return new AuthResponse(
                result.userId(),
                result.email(),
                result.firstName(),
                result.lastName(),
                result.role().name(),
                result.accessToken(),
                "Bearer",
                result.expiresInSeconds()
        );
    }

    public static LoanApplicationResponse toResponse(LoanApplication application) {
        return new LoanApplicationResponse(
                application.id(),
                application.userId(),
                application.amount(),
                application.termMonths(),
                application.monthlyIncome(),
                application.monthlyExpenses(),
                application.existingLiabilities(),
                application.interestRate(),
                application.monthlyPayment(),
                application.availableMonthlyIncome(),
                application.debtToIncomeRatio(),
                application.riskLevel().name(),
                application.status().name(),
                application.purpose(),
                application.createdAt(),
                application.updatedAt()
        );
    }
}
