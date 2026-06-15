package mamarr.loanapplicationapp.bll;

import java.math.BigDecimal;

public record CreateLoanApplicationCommand(
        BigDecimal amount,
        int termMonths,
        BigDecimal monthlyIncome,
        BigDecimal monthlyExpenses,
        BigDecimal existingLiabilities,
        String purpose
) {
}
