package de.muenchen.oss.digiwf.cocreation.core.user.api.mapper;

import de.muenchen.oss.digiwf.cocreation.core.user.api.transport.UserInfoTO;
import de.muenchen.oss.digiwf.cocreation.core.user.api.transport.UserTO;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.model.User;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.model.UserInfo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserApiMapper {

    UserInfoTO mapToTO(UserInfo info);

    List<UserInfoTO> mapToTO(List<UserInfo> list);

    UserTO mapToTO(User user);

}
