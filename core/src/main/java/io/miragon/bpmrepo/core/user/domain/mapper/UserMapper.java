package io.miragon.bpmrepo.core.user.domain.mapper;

import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.model.UserInfo;
import io.miragon.bpmrepo.core.user.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserEntity mapToEntity(User model);

    User mapToModel(UserEntity userEntity);

    UserInfo mapToInfo(UserEntity userEntity);
    
    List<UserInfo> mapToInfo(List<UserEntity> userEntity);

    List<User> mapToModel(List<UserEntity> userEntity);

    UserInfo mapToInfo(User user);
}
