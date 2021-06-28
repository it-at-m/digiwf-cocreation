package io.miragon.bpmrepo.core.user.domain.business;

import io.miragon.bpmrepo.core.security.UserContext;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.user.api.transport.UserUpdateTO;
import io.miragon.bpmrepo.core.user.domain.exception.UsernameAlreadyInUseException;
import io.miragon.bpmrepo.core.user.domain.mapper.UserMapper;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.model.UserInfo;
import io.miragon.bpmrepo.core.user.infrastructure.entity.UserEntity;
import io.miragon.bpmrepo.core.user.infrastructure.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final UserContext userContext;
    private final UserMapper mapper;

    public void createUser(final String username) {
        final User user = new User(username);
        this.checkIfUsernameIsAvailable(username);
        this.saveToDb(user);
    }

    public void updateUser(final UserUpdateTO userUpdateTO) {
        this.verifyUserIsChangingOwnProfile(userUpdateTO.getUserId());
        this.updateOrAdoptProperties(userUpdateTO);
    }

    private void updateOrAdoptProperties(final UserUpdateTO userUpdateTO) {
        final User user = this.getCurrentUser();
        if (userUpdateTO.getUsername() != null && !userUpdateTO.getUsername().equals(user.getUsername())) {
            this.checkIfUsernameIsAvailable(userUpdateTO.getUsername());
            user.updateUserName(userUpdateTO.getUsername());
        }
        this.saveToDb(user);
    }

    private void verifyUserIsChangingOwnProfile(final String userId) {
        final String currentUserId = this.getUserIdOfCurrentUser();
        if (!currentUserId.equals(userId)) {
            throw new AccessRightException("You can only change your own profile");
        }
    }

    public String getUserIdByUsername(final String username) {
        return this.userJpaRepository.findByUsername(username)
                .map(UserEntity::getUsername)
                .orElseThrow();
    }

    public String getUserIdOfCurrentUser() {
        final String username = this.userContext.getUserName();
        return this.getUserIdByUsername(username);
    }

    public void checkIfUsernameIsAvailable(final String username) {
        if (this.userJpaRepository.existsUserEntityByUsername(username)) {
            throw new UsernameAlreadyInUseException(username);
        }
    }

    public User getCurrentUser() {
        final String userName = this.userContext.getUserName();
        return this.userJpaRepository.findByUsername(userName)
                .map(this.mapper::mapToModel)
                .orElseThrow();
    }

    public UserInfo getUserInfo() {
        final User currentUser = this.getCurrentUser();
        return this.mapper.mapToInfo(currentUser);
    }

    public List<UserInfo> searchUsers(final String typedName) {
        //Parameters correspond to (username) -> only one search field that queries both
        final List<UserEntity> userEntities = this.userJpaRepository.findAllByUsernameStartsWithIgnoreCase(typedName);
        return userEntities.stream()
                .map(this.mapper::mapToInfo)
                .collect(Collectors.toList());
    }

    public User saveToDb(final User user) {
        val savedUser = this.userJpaRepository.save(this.mapper.mapToEntity(user));
        return this.mapper.mapToModel(savedUser);
    }
}
