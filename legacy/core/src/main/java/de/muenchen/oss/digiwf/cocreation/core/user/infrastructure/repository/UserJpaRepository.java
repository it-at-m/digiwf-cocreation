package de.muenchen.oss.digiwf.cocreation.core.user.infrastructure.repository;

import de.muenchen.oss.digiwf.cocreation.core.user.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    Boolean existsUserEntityByUsername(String username);

    List<UserEntity> findAllByUsernameStartsWithIgnoreCase(String typedName);
}
