package io.miragon.bpmrepo.core.user.api.resource;

import io.miragon.bpmrepo.core.security.UserContext;
import io.miragon.bpmrepo.core.user.api.mapper.UserApiMapper;
import io.miragon.bpmrepo.core.user.api.transport.UserInfoTO;
import io.miragon.bpmrepo.core.user.api.transport.UserTO;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.model.UserInfo;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Create a new User")
    @PostMapping("/create")
    public ResponseEntity<UserTO> createUser() {
        log.debug("Creating new user " + this.userContext.getUserName());
        final User user = this.userService.createUser(this.userContext.getUserName());
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(user));
    }

    /**
     * Returns the User that is currently sending requests
     *
     * @return Id and name of the requesting user
     */
    @Operation(summary = "Returns the User that is currently sending requests")
    @GetMapping("/currentUser")
    public ResponseEntity<UserInfoTO> getUserInfo() {
        log.debug("Returning information about logged in user");
        final UserInfo userInfo = this.userService.getUserInfo();
        return ResponseEntity.ok(this.apiMapper.mapToTO(userInfo));
    }

    /**
     * Get a list of users by providing their Ids
     *
     * @param userIds userIds
     * @return a list of users
     */
    @Operation(summary = "Get a list of users by providing their Ids")
    @PostMapping("/multiple")
    public ResponseEntity<List<UserInfoTO>> getMultipleUsers(@RequestBody final List<String> userIds) {
        log.debug("Returning multiple users");
        final List<UserInfo> userInfos = this.userService.getMultipleUsers(userIds);
        return ResponseEntity.ok().body(this.apiMapper.mapToTO(userInfos));
    }

    /**
     * Returns the name of the user that is currently sending requests (name equals email address)
     *
     * @return name of requesting user
     */
    @Operation(summary = "Returns the namae of the user that is currently sending requests (name equals email address)")
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
    @Operation(summary = "Returns a list of users that matches the typed letters of a search")
    @GetMapping("/search/{typedName}")
    public ResponseEntity<List<UserInfoTO>> searchUsers(@PathVariable final String typedName) {
        log.debug("Searching for users \"{}\"", typedName);
        final List<UserInfo> userInfos = this.userService.searchUsers(typedName);
        return ResponseEntity.ok(this.apiMapper.mapToTO(userInfos));
    }

}
