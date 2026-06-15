package mamarr.loanapplicationapp.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record LoanApplication(
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
        RiskLevel riskLevel,
        ApplicationStatus status,
        String purpose,
        Instant createdAt,
        Instant updatedAt
) {
    public LoanApplication {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(userId, "userId is required");
        requirePositive(amount, "amount");
        if (termMonths <= 0) {
            throw new IllegalArgumentException("termMonths must be positive");
        }
        requirePositive(monthlyIncome, "monthlyIncome");
        requireNonNegative(monthlyExpenses, "monthlyExpenses");
        requireNonNegative(existingLiabilities, "existingLiabilities");
        requireNonNegative(interestRate, "interestRate");
        requireNonNegative(monthlyPayment, "monthlyPayment");
        Objects.requireNonNull(availableMonthlyIncome, "availableMonthlyIncome is required");
        requireNonNegative(debtToIncomeRatio, "debtToIncomeRatio");
        Objects.requireNonNull(riskLevel, "riskLevel is required");
        Objects.requireNonNull(status, "status is required");
        Objects.requireNonNull(createdAt, "createdAt is required");
        Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public static LoanApplication createPending(
            UUID userId,
            BigDecimal amount,
            int termMonths,
            BigDecimal monthlyIncome,
            BigDecimal monthlyExpenses,
            BigDecimal existingLiabilities,
            String purpose,
            LoanCalculationResult calculation
    ) {
        Instant now = Instant.now();
        return new LoanApplication(
                UUID.randomUUID(),
                userId,
                amount,
                termMonths,
                monthlyIncome,
                monthlyExpenses,
                existingLiabilities,
                calculation.interestRate(),
                calculation.monthlyPayment(),
                calculation.availableMonthlyIncome(),
                calculation.debtToIncomeRatio(),
                calculation.riskLevel(),
                ApplicationStatus.PENDING,
                purpose,
                now,
                now
        );
    }

    public LoanApplication withStatus(ApplicationStatus newStatus) {
        return new LoanApplication(
                id,
                userId,
                amount,
                termMonths,
                monthlyIncome,
                monthlyExpenses,
                existingLiabilities,
                interestRate,
                monthlyPayment,
                availableMonthlyIncome,
                debtToIncomeRatio,
                riskLevel,
                newStatus,
                purpose,
                createdAt,
                Instant.now()
        );
    }

    private static void requirePositive(BigDecimal value, String name) {
        Objects.requireNonNull(value, name + " is required");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }

    private static void requireNonNegative(BigDecimal value, String name) {
        Objects.requireNonNull(value, name + " is required");
        if (value.signum() < 0) {
            throw new IllegalArgumentException(name + " must be non-negative");
        }
    }
}
