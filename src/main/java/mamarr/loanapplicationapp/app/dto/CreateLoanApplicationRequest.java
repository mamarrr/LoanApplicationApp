package mamarr.loanapplicationapp.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateLoanApplicationRequest(
        @NotNull @DecimalMin(value = "1.00") BigDecimal amount,
        @NotNull @Min(1) @Max(360) Integer termMonths,
        @NotNull @DecimalMin(value = "1.00") BigDecimal monthlyIncome,
        @NotNull @DecimalMin(value = "0.00") BigDecimal monthlyExpenses,
        @NotNull @DecimalMin(value = "0.00") BigDecimal existingLiabilities,
        @Size(max = 255) String purpose
) {
}
