package mamarr.loanapplicationapp.domain;

import java.math.BigDecimal;

public record LoanCalculationResult(
        BigDecimal interestRate,
        BigDecimal monthlyPayment,
        BigDecimal availableMonthlyIncome,
        BigDecimal debtToIncomeRatio,
        RiskLevel riskLevel
) {
}
