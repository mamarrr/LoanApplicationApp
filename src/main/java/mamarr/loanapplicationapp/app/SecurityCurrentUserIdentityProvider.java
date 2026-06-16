package mamarr.loanapplicationapp.app;

import mamarr.loanapplicationapp.app.security.AuthenticatedUser;
import mamarr.loanapplicationapp.bll.contracts.ICurrentUserIdentityProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityCurrentUserIdentityProvider implements ICurrentUserIdentityProvider {
    @Override
    public UUID currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new IllegalStateException("No authenticated user is available");
        }
        return user.id();
    }
}
