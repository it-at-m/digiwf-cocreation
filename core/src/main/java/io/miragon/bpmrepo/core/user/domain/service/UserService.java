package io.miragon.bpmrepo.core.user.domain.service;

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

    public User createUser(final String username) {
        log.debug("Persisting user");
        final User user = new User(username);
        this.checkIfUsernameIsAvailable(username);
        return this.saveToDb(user);
    }

    public User updateUser(final UserUpdateTO userUpdateTO) {
        log.debug("Persisting user update");
        this.verifyUserIsChangingOwnProfile(userUpdateTO.getUserId());
        return this.updateOrAdoptProperties(userUpdateTO);
    }

    private User updateOrAdoptProperties(final UserUpdateTO userUpdateTO) {
        final User user = this.getCurrentUser();
        if (userUpdateTO.getUsername() != null && !userUpdateTO.getUsername().equals(user.getUsername())) {
            this.checkIfUsernameIsAvailable(userUpdateTO.getUsername());
            user.updateUserName(userUpdateTO.getUsername());
        }
        return this.saveToDb(user);
    }

    private void verifyUserIsChangingOwnProfile(final String userId) {
        final String currentUserId = this.getUserIdOfCurrentUser();
        if (!currentUserId.equals(userId)) {
            throw new AccessRightException("You can only change your own profile");
        }
    }

    public String getUserIdByUsername(final String username) {
        log.debug("Querying User by Username");
        return this.userJpaRepository.findByUsername(username)
                .map(UserEntity::getId)
                .orElseThrow();
    }

    public String getUserIdOfCurrentUser() {
        log.debug("Querying current userId");
        final String username = this.userContext.getUserName();
        return this.getUserIdByUsername(username);
    }

    public void checkIfUsernameIsAvailable(final String username) {
        if (this.userJpaRepository.existsUserEntityByUsername(username)) {
            throw new UsernameAlreadyInUseException(username);
        }
    }

    public User getCurrentUser() {
        log.debug("Querying current user");
        final String userName = this.userContext.getUserName();
        return this.userJpaRepository.findByUsername(userName)
                .map(this.mapper::mapToModel)
                .orElseThrow();
    }

    public UserInfo getUserInfo() {
        log.debug("Querying current userInfo");
        final User currentUser = this.getCurrentUser();
        return this.mapper.mapToInfo(currentUser);
    }

    public List<UserInfo> searchUsers(final String typedName) {
        log.debug("Querying list of matching users");
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
