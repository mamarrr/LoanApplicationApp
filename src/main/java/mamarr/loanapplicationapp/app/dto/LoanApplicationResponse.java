package mamarr.loanapplicationapp.app.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LoanApplicationResponse(
        UUID id,
        UUID userId,
        BigDecimal amount,
        int termMonths,
        BigDecimal monthlyIncome,
        BigDecimal monthlyExpenses,
        BigDecimal existingLiabilities,
        BigDecimal interestRate,
        BigDecimal monthlyPayment,
        BigDecimal availableMonthlyIncome,
        BigDecimal debtToIncomeRatio,
        String riskLevel,
        String status,
        String purpose,
        Instant createdAt,
        Instant updatedAt
) {
}
