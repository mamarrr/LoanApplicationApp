package mamarr.loanapplicationapp.bll.contracts;

import mamarr.loanapplicationapp.bll.AuthResult;
import mamarr.loanapplicationapp.bll.LoginCommand;
import mamarr.loanapplicationapp.bll.RegisterCommand;

public interface IAuthService {
    AuthResult register(RegisterCommand command);

    AuthResult login(LoginCommand command);
}
