package mamarr.loanapplicationapp.bll.contracts;

import mamarr.loanapplicationapp.domain.AppUser;

public interface ITokenService {
    String createAccessToken(AppUser user);

    long accessTokenTtlSeconds();
}
