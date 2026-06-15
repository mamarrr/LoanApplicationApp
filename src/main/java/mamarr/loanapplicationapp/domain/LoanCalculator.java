package mamarr.loanapplicationapp.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoanCalculator {
    public static final BigDecimal FIXED_ANNUAL_INTEREST_RATE = new BigDecimal("0.0750");

    public LoanCalculationResult calculate(
            BigDecimal amount,
            int termMonths,
            BigDecimal monthlyIncome,
            BigDecimal monthlyExpenses,
            BigDecimal existingLiabilities
    ) {
        if (termMonths <= 0) {
            throw new IllegalArgumentException("termMonths must be positive");
        }
        requirePositive(amount, "amount");
        requirePositive(monthlyIncome, "monthlyIncome");
        requireNonNegative(monthlyExpenses, "monthlyExpenses");
        requireNonNegative(existingLiabilities, "existingLiabilities");

        BigDecimal monthlyRate = FIXED_ANNUAL_INTEREST_RATE.divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = calculateAmortizedPayment(amount, termMonths, monthlyRate);
        BigDecimal availableMonthlyIncome = monthlyIncome
                .subtract(monthlyExpenses)
                .subtract(existingLiabilities)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal debtToIncomeRatio = existingLiabilities
                .add(monthlyPayment)
                .divide(monthlyIncome, 6, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);

        return new LoanCalculationResult(
                FIXED_ANNUAL_INTEREST_RATE,
                monthlyPayment,
                availableMonthlyIncome,
                debtToIncomeRatio,
                riskLevelFor(debtToIncomeRatio)
        );
    }

    private BigDecimal calculateAmortizedPayment(BigDecimal principal, int termMonths, BigDecimal monthlyRate) {
        double rate = monthlyRate.doubleValue();
        double denominator = 1 - Math.pow(1 + rate, -termMonths);
        double payment = principal.doubleValue() * rate / denominator;
        return BigDecimal.valueOf(payment).setScale(2, RoundingMode.HALF_UP);
    }

    private RiskLevel riskLevelFor(BigDecimal debtToIncomeRatio) {
        if (debtToIncomeRatio.compareTo(new BigDecimal("30.00")) < 0) {
            return RiskLevel.LOW;
        }
        if (debtToIncomeRatio.compareTo(new BigDecimal("50.00")) <= 0) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.HIGH;
    }

    private void requirePositive(BigDecimal value, String name) {
        if (value == null || value.signum() <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }

    private void requireNonNegative(BigDecimal value, String name) {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException(name + " must be non-negative");
        }
    }
}
