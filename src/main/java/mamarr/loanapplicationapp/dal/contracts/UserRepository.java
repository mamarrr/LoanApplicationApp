package mamarr.loanapplicationapp.dal.contracts;

import mamarr.loanapplicationapp.domain.AppUser;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    AppUser save(AppUser user);

    Optional<AppUser> findById(UUID id);

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
