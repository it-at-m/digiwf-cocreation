package de.muenchen.oss.digiwf.cocreation.core.user.domain.service;

import de.muenchen.oss.digiwf.cocreation.core.security.UserContext;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.NameConflictException;
import de.muenchen.oss.digiwf.cocreation.core.shared.exception.ObjectNotFoundException;
import de.muenchen.oss.digiwf.cocreation.core.user.infrastructure.repository.UserJpaRepository;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.mapper.UserMapper;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.model.User;
import de.muenchen.oss.digiwf.cocreation.core.user.domain.model.UserInfo;
import de.muenchen.oss.digiwf.cocreation.core.user.infrastructure.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public Optional<String> getUserIdByUsername(final String username) {
        log.debug("Querying User by Username");
        return this.userJpaRepository.findByUsername(username).map(UserEntity::getId);
    }

    public String getUserIdOfCurrentUser() {
        log.debug("Querying current userId");
        final String username = this.userContext.getUserName();
        return this.getUserIdByUsername(username).orElseThrow(() -> new ObjectNotFoundException("exception.currentUserNotFound"));
    }

    public void checkIfUsernameIsAvailable(final String username) {
        if (this.userJpaRepository.existsUserEntityByUsername(username)) {
            throw new NameConflictException("exception.usernameInUse");
        }
    }

    public User getCurrentUser() {
        log.debug("Querying current user");
        final String username = this.userContext.getUserName();
        return this.userJpaRepository.findByUsername(username)
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
        final UserEntity savedUser = this.userJpaRepository.save(this.mapper.mapToEntity(user));
        return this.mapper.mapToModel(savedUser);
    }

    public List<UserInfo> getMultipleUsers(final List<String> userIds) {
        log.debug("Querying list of users");
        final List<UserEntity> userEntities = this.userJpaRepository.findAllById(userIds);
        return this.mapper.mapToInfo(userEntities);

    }
}
