package io.miragon.bpmrepo.core.artifact.domain.service;

import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {

    private final UserService userService;

    public void checkIfVersionIsUnlockedOrLockedByActiveUser(final Artifact artifact) {
        if (artifact.getLockedBy() == null) {
            return;
        }

        if (artifact.getLockedUntil().isAfter(LocalDateTime.now())) {
            this.checkIfVersionIsLockedByActiveUser(artifact.getLockedBy());
        }

    }

    public void checkIfVersionIsLockedByActiveUser(final String lockedBy) {
        final User user = this.userService.getCurrentUser();
        if (!user.getUsername().equals(lockedBy)) {
            throw new AccessRightException(
                    String.format("This artifact is currently being edited by %s. You can still download it and edit it locally", user.getUsername()));
        }

    }
}
