package io.miragon.bpmrepo.core.user.api.mapper;

import io.miragon.bpmrepo.core.user.api.transport.UserInfoTO;
import io.miragon.bpmrepo.core.user.api.transport.UserTO;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.model.UserInfo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserApiMapper {

    UserInfoTO mapToTO(UserInfo info);

    List<UserInfoTO> mapToTO(List<UserInfo> list);

    UserTO mapToTO(User user);

}
