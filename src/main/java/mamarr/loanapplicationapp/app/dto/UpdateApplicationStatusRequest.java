package mamarr.loanapplicationapp.app.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateApplicationStatusRequest(@NotNull String status) {
}
