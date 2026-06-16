package mamarr.loanapplicationapp.bll;

import mamarr.loanapplicationapp.bll.contracts.IAuthService;
import mamarr.loanapplicationapp.bll.contracts.ITokenService;
import mamarr.loanapplicationapp.dal.contracts.IUserRepository;
import mamarr.loanapplicationapp.domain.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements IAuthService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ITokenService tokenService;

    public AuthService(IUserRepository userRepository, PasswordEncoder passwordEncoder, ITokenService tokenService) {
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
