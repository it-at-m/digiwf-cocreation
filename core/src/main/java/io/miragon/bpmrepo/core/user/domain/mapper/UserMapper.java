package io.miragon.bpmrepo.core.user.domain.mapper;

import io.miragon.bpmrepo.core.user.api.transport.UserInfoTO;
import io.miragon.bpmrepo.core.user.api.transport.UserTO;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserEntity toEntity(User model);

    User toModel(UserEntity userEntity);

    UserTO toTO(User user);

    UserInfoTO toInfoTO(User user);

}
