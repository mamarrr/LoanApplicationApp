package mamarr.loanapplicationapp.dal;

import mamarr.loanapplicationapp.dal.contracts.IUserRepository;
import mamarr.loanapplicationapp.domain.AppUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository implements IUserRepository {
    private final UserSpringDataRepository repository;

    public UserRepository(UserSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public AppUser save(AppUser user) {
        return UserMapper.toDomain(repository.save(UserMapper.toEntity(user)));
    }

    @Override
    public Optional<AppUser> findById(UUID id) {
        return repository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return repository.findByEmail(email.toLowerCase()).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email.toLowerCase());
    }
}
