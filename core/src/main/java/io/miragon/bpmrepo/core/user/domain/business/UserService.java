package io.miragon.bpmrepo.core.user.domain.business;

import io.miragon.bpmrepo.core.security.UserContext;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.shared.exception.NameNotExistentException;
import io.miragon.bpmrepo.core.shared.exception.UserNotExistentException;
import io.miragon.bpmrepo.core.user.api.transport.UserInfoTO;
import io.miragon.bpmrepo.core.user.api.transport.UserUpdateTO;
import io.miragon.bpmrepo.core.user.domain.exception.UsernameAlreadyInUseException;
import io.miragon.bpmrepo.core.user.domain.mapper.UserMapper;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.infrastructure.entity.UserEntity;
import io.miragon.bpmrepo.core.user.infrastructure.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        this.saveToDb(this.mapper.toEntity(user));
    }

    public void updateUser(final UserUpdateTO userUpdateTO) {
        this.verifyUserIsChangingOwnProfile(userUpdateTO.getUserId());
        this.updateOrAdoptProperties(userUpdateTO);
    }

    private void updateOrAdoptProperties(final UserUpdateTO userUpdateTO) {
        final User user = this.getCurrentUser();
        if (userUpdateTO.getUsername() != null && !userUpdateTO.getUsername().equals(user.getUserName())) {
            this.checkIfUsernameIsAvailable(userUpdateTO.getUsername());
            user.updateUserName(userUpdateTO.getUsername());
        }

        this.saveToDb(this.mapper.toEntity(user));
    }

    private void verifyUserIsChangingOwnProfile(final String userId) {
        final String currentUserId = this.getUserIdOfCurrentUser();
        if (!currentUserId.equals(userId)) {
            throw new AccessRightException("You can only change your own profile");
        }
    }

    public String getUserIdByUsername(final String username) {
        final UserEntity userEntity = this.userJpaRepository.findByUserName(username);
        this.checkIfUserExists(userEntity);
        return userEntity.getUserId();
    }

    public void checkIfUserExists(final UserEntity userEntity) {
        if (userEntity == null) {
            throw new NameNotExistentException();
        }
    }

    public void checkIfUserExists(final User user) {
        if (user == null) {
            throw new UserNotExistentException();
        }
    }

    public String getUserIdOfCurrentUser() {
        final String userName = this.userContext.getUserName();
        UserEntity userEntity = this.userJpaRepository.findByUserName(userName);
        if (userEntity == null) {
            userEntity = this.userJpaRepository.findByUserName(userName);
        }
        return userEntity.getUserId();
    }

    public void checkIfUsernameIsAvailable(final String username) {
        if (this.userJpaRepository.existsUserEntityByUserName(username)) {
            throw new UsernameAlreadyInUseException(username);
        }
    }

    public User getCurrentUser() {
        final String userName = this.userContext.getUserName();
        final UserEntity userEntity = this.userJpaRepository.findByUserName(userName);
        return this.mapper.toModel(userEntity);
    }

    public UserInfoTO getUserInfo() {
        final User user = this.getCurrentUser();
        this.checkIfUserExists(user);
        return this.mapper.toInfoTO(user);
    }

    public List<UserInfoTO> searchUsers(final String typedName) {
        //Parameters correspond to (username) -> only one search field that queries both
        final List<UserEntity> userEntities = this.userJpaRepository.findAllByUserNameStartsWithIgnoreCase(typedName);
        return userEntities.stream()
                .map(userEntity -> this.mapper.toInfoTO(this.mapper.toModel(userEntity)))
                .collect(Collectors.toList());
    }

    public void saveToDb(final UserEntity entity) {
        this.userJpaRepository.save(entity);
    }
}
