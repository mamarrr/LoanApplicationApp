package mamarr.loanapplicationapp.bll;

import mamarr.loanapplicationapp.bll.contracts.CurrentUserIdentityProvider;
import mamarr.loanapplicationapp.bll.contracts.CurrentUserService;
import mamarr.loanapplicationapp.dal.contracts.UserRepository;
import mamarr.loanapplicationapp.domain.AppUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultCurrentUserService implements CurrentUserService {
    private final CurrentUserIdentityProvider identityProvider;
    private final UserRepository userRepository;

    public DefaultCurrentUserService(CurrentUserIdentityProvider identityProvider, UserRepository userRepository) {
        this.identityProvider = identityProvider;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser currentUser() {
        return userRepository.findById(identityProvider.currentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
}
