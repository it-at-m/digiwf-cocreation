package io.miragon.bpmrepo.core.user.api.mapper;

import io.miragon.bpmrepo.core.user.api.transport.UserInfoTO;
import io.miragon.bpmrepo.core.user.api.transport.UserTO;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.model.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface UserApiMapper {

    UserInfoTO mapInfo(UserInfo info);

    List<UserInfoTO> mapInfo(List<UserInfo> list);

    @Mapping(source = "username", target = "userName")
    UserTO mapToTO(User user);

}
