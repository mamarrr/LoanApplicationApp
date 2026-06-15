package mamarr.loanapplicationapp.bll;

import mamarr.loanapplicationapp.bll.contracts.TokenService;
import mamarr.loanapplicationapp.dal.contracts.UserRepository;
import mamarr.loanapplicationapp.domain.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultAuthServiceTest {
    private final FakeUserRepository userRepository = new FakeUserRepository();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenService tokenService = new FakeTokenService();
    private final DefaultAuthService authService = new DefaultAuthService(userRepository, passwordEncoder, tokenService);

    @Test
    void registerHashesPasswordAndSavesUserRole() {
        AuthResult result = authService.register(new RegisterCommand(
                "USER@EXAMPLE.COM",
                "password123",
                "Regular",
                "User"
        ));

        AppUser saved = userRepository.findByEmail("user@example.com").orElseThrow();
        assertThat(saved.email()).isEqualTo("user@example.com");
        assertThat(saved.role().name()).isEqualTo("USER");
        assertThat(saved.passwordHash()).isNotEqualTo("password123");
        assertThat(passwordEncoder.matches("password123", saved.passwordHash())).isTrue();
        assertThat(result.accessToken()).isEqualTo("token-" + saved.id());
    }

    @Test
    void registerRejectsDuplicateEmail() {
        authService.register(new RegisterCommand("user@example.com", "password123", "A", "B"));

        assertThatThrownBy(() -> authService.register(new RegisterCommand("USER@example.com", "password123", "A", "B")))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void loginRejectsInvalidPassword() {
        authService.register(new RegisterCommand("user@example.com", "password123", "A", "B"));

        assertThatThrownBy(() -> authService.login(new LoginCommand("user@example.com", "wrong-password")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    private static class FakeUserRepository implements UserRepository {
        private final Map<UUID, AppUser> usersById = new HashMap<>();
        private final Map<String, AppUser> usersByEmail = new HashMap<>();

        @Override
        public AppUser save(AppUser user) {
            usersById.put(user.id(), user);
            usersByEmail.put(user.email(), user);
            return user;
        }

        @Override
        public Optional<AppUser> findById(UUID id) {
            return Optional.ofNullable(usersById.get(id));
        }

        @Override
        public Optional<AppUser> findByEmail(String email) {
            return Optional.ofNullable(usersByEmail.get(email.toLowerCase()));
        }

        @Override
        public boolean existsByEmail(String email) {
            return usersByEmail.containsKey(email.toLowerCase());
        }
    }

    private static class FakeTokenService implements TokenService {
        @Override
        public String createAccessToken(AppUser user) {
            return "token-" + user.id();
        }

        @Override
        public long accessTokenTtlSeconds() {
            return 3600;
        }
    }
}
