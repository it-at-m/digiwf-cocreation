package io.miragon.bpmrepo.core.user.api.resource;

import io.miragon.bpmrepo.core.security.UserContext;
import io.miragon.bpmrepo.core.user.api.mapper.UserApiMapper;
import io.miragon.bpmrepo.core.user.api.transport.UserInfoTO;
import io.miragon.bpmrepo.core.user.api.transport.UserTO;
import io.miragon.bpmrepo.core.user.api.transport.UserUpdateTO;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.model.UserInfo;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@Tag(name = "User")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserContext userContext;

    private final UserApiMapper apiMapper;

    /**
     * Create a new User
     *
     * @return the name of the created user
     */
    @PostMapping("/create")
    public ResponseEntity<UserTO> createUser() {
        log.debug("Creating new user " + this.userContext.getUserName());
        final User user = this.userService.createUser(this.userContext.getUserName());
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(user));
    }

    /**
     * Update the user (only name can be changed so far)
     *
     * @param userUpdateTO
     * @return
     */
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody @Valid final UserUpdateTO userUpdateTO) {
        log.debug(String.format("updating user with id %s", userUpdateTO.getUserId()));
        final User user = this.userService.updateUser(userUpdateTO);
        return ResponseEntity.ok().body(user);
    }

    /**
     * Returns the User that is currently sending requests
     *
     * @return Id and name of the requesting user
     */
    @GetMapping("/currentUser")
    public ResponseEntity<UserInfoTO> getUserInfo() {
        log.debug("Returning information about logged in user");
        final UserInfo userInfo = this.userService.getUserInfo();
        return ResponseEntity.ok(this.apiMapper.mapInfo(userInfo));
    }

    /**
     * Returns the namae of the user that is currently sending requests (name equals email address)
     *
     * @return name of requesting user
     */
    @GetMapping("/registeredEmail")
    public ResponseEntity<String> getUserName() {
        log.debug("Returning email registered at Flowsquad");
        return ResponseEntity.ok(this.userService.getCurrentUser().getUsername());
    }

    /**
     * Returns a list of users that matches the typed letters of a search
     *
     * @param typedName the searched string
     * @return list of Usernames and Ids
     */
    @GetMapping("/search/{typedName}")
    public ResponseEntity<List<UserInfoTO>> searchUsers(@PathVariable final String typedName) {
        log.debug("Searching for users \"{}\"", typedName);
        final List<UserInfo> userInfos = this.userService.searchUsers(typedName);
        return ResponseEntity.ok(this.apiMapper.mapInfo(userInfos));
    }

}
