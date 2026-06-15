package mamarr.loanapplicationapp.bll;

import mamarr.loanapplicationapp.bll.contracts.AuthService;
import mamarr.loanapplicationapp.bll.contracts.TokenService;
import mamarr.loanapplicationapp.dal.contracts.UserRepository;
import mamarr.loanapplicationapp.domain.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultAuthService implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public DefaultAuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public AuthResult register(RegisterCommand command) {
        String email = command.email().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email is already registered");
        }

        AppUser user = AppUser.newUser(
                email,
                passwordEncoder.encode(command.password()),
                command.firstName(),
                command.lastName()
        );
        AppUser saved = userRepository.save(user);
        return toAuthResult(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResult login(LoginCommand command) {
        AppUser user = userRepository.findByEmail(command.email().toLowerCase())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(command.password(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }
        return toAuthResult(user);
    }

    private AuthResult toAuthResult(AppUser user) {
        String token = tokenService.createAccessToken(user);
        return new AuthResult(
                user.id(),
                user.email(),
                user.firstName(),
                user.lastName(),
                user.role(),
                token,
                tokenService.accessTokenTtlSeconds()
        );
    }
}
