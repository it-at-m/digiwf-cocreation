package io.miragon.bpmnrepo.core.user.api.resource;


import io.miragon.bpmnrepo.core.security.UserContext;
import io.miragon.bpmnrepo.core.user.api.transport.UserInfoTO;
import io.miragon.bpmnrepo.core.user.api.transport.UserUpdateTO;
import io.miragon.bpmnrepo.core.user.domain.business.UserService;
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
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserContext userContext;

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
        final UserInfoTO userInfoTO = this.userService.getUserInfo();
        return ResponseEntity.ok().body(userInfoTO);
    }

    @GetMapping("/registeredEmail")
    public ResponseEntity<String> getUserName() {
        log.debug("Returning email registered at Flowsquad");
        return ResponseEntity.ok().body(this.userService.getUserInfo().getUserName());
    }

    @GetMapping("/searchUsers/{typedName}")
    public ResponseEntity<List<UserInfoTO>> searchUsers(@PathVariable final String typedName) {
        log.debug(String.format("Searching for users \"%s\"", typedName));
        return ResponseEntity.ok().body(this.userService.searchUsers(typedName));
    }

}
