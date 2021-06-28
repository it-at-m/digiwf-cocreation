package io.miragon.bpmrepo.core.user.domain.mapper;

import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.model.UserInfo;
import io.miragon.bpmrepo.core.user.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserEntity mapToEntity(User model);

    User mapToModel(UserEntity userEntity);

    UserInfo mapToInfo(UserEntity userEntity);

    UserInfo mapToInfo(User user);
}
