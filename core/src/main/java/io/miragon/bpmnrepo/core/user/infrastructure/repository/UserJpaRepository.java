package io.miragon.bpmnrepo.core.user.infrastructure.repository;

import io.miragon.bpmnrepo.core.user.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByUserName(String userName);

    UserEntity findByUserId(String userId);

    Boolean existsUserEntityByUserName(String userName);

    //Both parameters are the same value (only one search field that queries for name AND email at the same time
    List<UserEntity> findAllByUserNameStartsWith(String typedName);
}
