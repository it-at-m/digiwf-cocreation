package io.miragon.bpmrepo.core.artifact.domain.service;

import io.miragon.bpmrepo.core.artifact.domain.exception.LockedException;
import io.miragon.bpmrepo.core.artifact.domain.model.Artifact;
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

    public void checkIfMilestoneIsUnlockedOrLockedByActiveUser(final Artifact artifact) {
        if (artifact.getLockedBy() == null) {
            return;
        }

        if (artifact.getLockedUntil().isAfter(LocalDateTime.now())) {
            this.checkIfMilestoneIsLockedByActiveUser(artifact.getLockedBy());
        }
    }

    public void checkIfMilestoneIsLockedByActiveUser(final Artifact artifact) {
        if (artifact.getLockedBy() == null) {
            throw new LockedException("exception.locked");
        }

        if (artifact.getLockedUntil().isAfter(LocalDateTime.now())) {
            this.checkIfMilestoneIsLockedByActiveUser(artifact.getLockedBy());
        }
    }

    private void checkIfMilestoneIsLockedByActiveUser(final String lockedBy) {
        final User user = this.userService.getCurrentUser();
        if (!user.getUsername().equals(lockedBy)) {
            throw new LockedException("exception.locked");
        }

    }
}
