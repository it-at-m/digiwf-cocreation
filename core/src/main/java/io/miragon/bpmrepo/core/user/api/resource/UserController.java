package io.miragon.bpmrepo.core.user.api.resource;

import io.miragon.bpmrepo.core.security.UserContext;
import io.miragon.bpmrepo.core.user.api.mapper.UserApiMapper;
import io.miragon.bpmrepo.core.user.api.transport.UserInfoTO;
import io.miragon.bpmrepo.core.user.api.transport.UserUpdateTO;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
import io.miragon.bpmrepo.core.user.domain.model.UserInfo;
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

    @PostMapping("/create")
    public ResponseEntity<Void> createUser() {
        log.debug("Creating new user " + this.userContext.getUserName());
        this.userService.createUser(this.userContext.getUserName());
        log.debug("Successfully created a new user");
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody @Valid final UserUpdateTO userUpdateTO) {
        log.debug(String.format("updating user with id %s", userUpdateTO.getUserId()));
        this.userService.updateUser(userUpdateTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/currentUser")
    public ResponseEntity<UserInfoTO> getUserInfo() {
        log.debug("Returning information about logged in user");
        final UserInfo userInfo = this.userService.getUserInfo();
        return ResponseEntity.ok(this.apiMapper.mapInfo(userInfo));
    }

    @GetMapping("/registeredEmail")
    public ResponseEntity<String> getUserName() {
        log.debug("Returning email registered at Flowsquad");
        return ResponseEntity.ok(this.userService.getCurrentUser().getUsername());
    }

    @GetMapping("/searchUsers/{typedName}")
    public ResponseEntity<List<UserInfoTO>> searchUsers(@PathVariable final String typedName) {
        log.debug(String.format("Searching for users \"%s\"", typedName));
        final List<UserInfo> userInfos = this.userService.searchUsers(typedName);
        return ResponseEntity.ok(this.apiMapper.mapInfo(userInfos));
    }

}
