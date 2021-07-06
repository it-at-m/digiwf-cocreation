package io.miragon.bpmrepo.core.diagram.domain.business;

import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
import io.miragon.bpmrepo.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {

    private final UserService userService;


    public void checkIfVersionIsUnlockedOrLockedByActiveUser(final Diagram diagram) {
        if (diagram.getLockedBy() == null) {
            return;
        } else {
            if (diagram.getLockedUntil().isAfter(LocalDateTime.now())) {
                this.checkIfVersionIsLockedByActiveUser(diagram.getLockedBy());
            }
        }

    }

    public void checkIfVersionIsLockedByActiveUser(final String lockedBy) {
        final User user = this.userService.getCurrentUser();
        if (!user.getUsername().equals(lockedBy)) {
            throw new AccessRightException(String.format("This diagram is currently being edited by %s. You can still download it and edit it locally", user.getUsername()));
        }

    }
}
