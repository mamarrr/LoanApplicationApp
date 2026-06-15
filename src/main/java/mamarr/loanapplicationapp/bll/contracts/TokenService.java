package mamarr.loanapplicationapp.bll.contracts;

import mamarr.loanapplicationapp.domain.AppUser;

public interface TokenService {
    String createAccessToken(AppUser user);

    long accessTokenTtlSeconds();
}
