package mamarr.loanapplicationapp.app;

import jakarta.validation.Valid;
import mamarr.loanapplicationapp.app.dto.AuthResponse;
import mamarr.loanapplicationapp.app.dto.LoginRequest;
import mamarr.loanapplicationapp.app.dto.RegisterRequest;
import mamarr.loanapplicationapp.bll.LoginCommand;
import mamarr.loanapplicationapp.bll.RegisterCommand;
import mamarr.loanapplicationapp.bll.contracts.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return ApiMapper.toResponse(authService.register(new RegisterCommand(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName()
        )));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return ApiMapper.toResponse(authService.login(new LoginCommand(request.email(), request.password())));
    }
}
