package mamarr.loanapplicationapp.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record AppUser(
        UUID id,
        String email,
        String passwordHash,
        String firstName,
        String lastName,
        UserRole role,
        Instant createdAt,
        Instant updatedAt
) {
    public AppUser {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(email, "email is required");
        Objects.requireNonNull(passwordHash, "passwordHash is required");
        Objects.requireNonNull(firstName, "firstName is required");
        Objects.requireNonNull(lastName, "lastName is required");
        Objects.requireNonNull(role, "role is required");
        Objects.requireNonNull(createdAt, "createdAt is required");
        Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public static AppUser newUser(String email, String passwordHash, String firstName, String lastName) {
        Instant now = Instant.now();
        return new AppUser(UUID.randomUUID(), email.toLowerCase(), passwordHash, firstName, lastName, UserRole.USER, now, now);
    }
}
