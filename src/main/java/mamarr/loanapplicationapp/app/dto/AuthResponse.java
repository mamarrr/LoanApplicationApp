package mamarr.loanapplicationapp.app.dto;

import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String email,
        String firstName,
        String lastName,
        String role,
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {
}
