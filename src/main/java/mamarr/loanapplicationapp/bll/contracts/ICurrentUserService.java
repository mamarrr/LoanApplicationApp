package mamarr.loanapplicationapp.bll.contracts;

import mamarr.loanapplicationapp.domain.AppUser;

public interface ICurrentUserService {
    AppUser currentUser();
}
