package mamarr.loanapplicationapp.bll;

import mamarr.loanapplicationapp.bll.contracts.ICurrentUserIdentityProvider;
import mamarr.loanapplicationapp.bll.contracts.ICurrentUserService;
import mamarr.loanapplicationapp.dal.contracts.IUserRepository;
import mamarr.loanapplicationapp.domain.AppUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentUserService implements ICurrentUserService {
    private final ICurrentUserIdentityProvider identityProvider;
    private final IUserRepository userRepository;

    public CurrentUserService(ICurrentUserIdentityProvider identityProvider, IUserRepository userRepository) {
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
