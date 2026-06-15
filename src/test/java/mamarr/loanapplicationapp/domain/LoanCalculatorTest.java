package mamarr.loanapplicationapp.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoanCalculatorTest {
    private final LoanCalculator calculator = new LoanCalculator();

    @Test
    void calculatesPaymentAvailableIncomeDtiAndLowRisk() {
        LoanCalculationResult result = calculator.calculate(
                new BigDecimal("5000"),
                60,
                new BigDecimal("5000"),
                new BigDecimal("1500"),
                new BigDecimal("300")
        );

        assertThat(result.monthlyPayment()).isEqualByComparingTo("100.19");
        assertThat(result.availableMonthlyIncome()).isEqualByComparingTo("3200.00");
        assertThat(result.debtToIncomeRatio()).isEqualByComparingTo("8.00");
        assertThat(result.riskLevel()).isEqualTo(RiskLevel.LOW);
    }

    @Test
    void riskIsMediumAtThirtyThroughFiftyPercentDti() {
        LoanCalculationResult result = calculator.calculate(
                new BigDecimal("1000"),
                12,
                new BigDecimal("1000"),
                BigDecimal.ZERO,
                new BigDecimal("300")
        );

        assertThat(result.debtToIncomeRatio()).isBetween(new BigDecimal("30.00"), new BigDecimal("50.00"));
        assertThat(result.riskLevel()).isEqualTo(RiskLevel.MEDIUM);
    }

    @Test
    void riskIsHighAboveFiftyPercentDti() {
        LoanCalculationResult result = calculator.calculate(
                new BigDecimal("20000"),
                24,
                new BigDecimal("1800"),
                new BigDecimal("700"),
                new BigDecimal("250")
        );

        assertThat(result.debtToIncomeRatio()).isGreaterThan(new BigDecimal("50.00"));
        assertThat(result.riskLevel()).isEqualTo(RiskLevel.HIGH);
    }

    @Test
    void rejectsInvalidNumericInputs() {
        assertThatThrownBy(() -> calculator.calculate(
                BigDecimal.ZERO,
                12,
                new BigDecimal("1000"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("amount");

        assertThatThrownBy(() -> calculator.calculate(
                new BigDecimal("1000"),
                0,
                new BigDecimal("1000"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("termMonths");
    }
}
