package io.miragon.bpmnrepo.core.user.domain.mapper;

import io.miragon.bpmnrepo.core.user.api.transport.UserInfoTO;
import io.miragon.bpmnrepo.core.user.api.transport.UserTO;
import io.miragon.bpmnrepo.core.user.domain.model.User;
import io.miragon.bpmnrepo.core.user.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
//    User toModel(UserTO userTO);
    UserEntity toEntity(User model);
    User toModel(UserEntity userEntity);
    UserTO toTO(User user);
    UserInfoTO toInfoTO(User user);

}
