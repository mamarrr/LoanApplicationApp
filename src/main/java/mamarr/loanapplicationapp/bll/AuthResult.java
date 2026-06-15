package mamarr.loanapplicationapp.bll;

import mamarr.loanapplicationapp.domain.UserRole;

import java.util.UUID;

public record AuthResult(
        UUID userId,
        String email,
        String firstName,
        String lastName,
        UserRole role,
        String accessToken,
        long expiresInSeconds
) {
}
