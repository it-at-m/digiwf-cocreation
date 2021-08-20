package io.miragon.bpmrepo.core.repository.domain.service;

import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.AssignmentJpaRepository;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AssignmentJpaRepository assignmentJpa;

    public void checkIfOperationIsAllowed(final String repositoryId, final RoleEnum minimumRequiredRole) {
        final String userId = this.userService.getUserIdOfCurrentUser();
        final AssignmentEntity assignmentEntity = this.assignmentJpa.findByAssignmentId_RepositoryIdAndAssignmentId_UserId(repositoryId, userId)
                .orElseThrow(() -> new AccessRightException("authorization failed - You are not assigned to this repository"));

        final RoleEnum role = assignmentEntity.getRole();
        //0: OWNER - 1: ADMIN 2: MEMBER 3: VIEWER
        if (minimumRequiredRole.ordinal() >= role.ordinal()) {
            log.debug("AUTHORIZATION: ok");
        } else {
            throw new AccessRightException(
                    "authorization failed - Required role for this operation: \"" + minimumRequiredRole + "\" - Your role is: \"" + role.toString()
                            + "\"");
        }

    }

}
