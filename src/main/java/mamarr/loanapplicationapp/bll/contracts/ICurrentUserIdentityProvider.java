package mamarr.loanapplicationapp.bll.contracts;

import java.util.UUID;

public interface ICurrentUserIdentityProvider {
    UUID currentUserId();
}
