package de.muenchen.oss.digiwf.cocreation.core.user.domain.mapper;

import de.muenchen.oss.digiwf.cocreation.core.user.domain.model.User;
import de.muenchen.oss.digiwf.cocreation.core.user.infrastructure.entity.UserEntity;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.model.UserInfo;
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
