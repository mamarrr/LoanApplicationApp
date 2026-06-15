package mamarr.loanapplicationapp.dal;

import mamarr.loanapplicationapp.domain.AppUser;

final class UserMapper {
    private UserMapper() {
    }

    static AppUser toDomain(UserJpaEntity entity) {
        return new AppUser(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    static UserJpaEntity toEntity(AppUser user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.id());
        entity.setEmail(user.email());
        entity.setPasswordHash(user.passwordHash());
        entity.setFirstName(user.firstName());
        entity.setLastName(user.lastName());
        entity.setRole(user.role());
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());
        return entity;
    }
}
