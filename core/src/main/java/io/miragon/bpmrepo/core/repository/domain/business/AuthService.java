package io.miragon.bpmrepo.core.repository.domain.business;

import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmrepo.core.repository.infrastructure.repository.AssignmentJpa;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmrepo.core.user.domain.business.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AssignmentJpa assignmentJpa;

    public void checkIfOperationIsAllowed(final String bpmnRepositoryId, final RoleEnum minimumRequiredRole) {
        final String userId = this.userService.getUserIdOfCurrentUser();

        final AssignmentEntity assignmentEntity = this.assignmentJpa.findByAssignmentId_BpmnRepositoryIdAndAssignmentId_UserId(bpmnRepositoryId, userId);
        //ExceptionHandling: if assignmentEntity is null, the user is completely missing the assignment -> no role in the repository at all
        if (assignmentEntity == null) {
            throw new AccessRightException("authorization failed - You are not assigned to this repository");
        } else {
            final RoleEnum roleEnum = assignmentEntity.getRoleEnum();
            //0: OWNER - 1: ADMIN 2: MEMBER 3: VIEWER
            if (minimumRequiredRole.ordinal() >= roleEnum.ordinal()) {
                log.debug("AUTHORIZATION: ok");
            } else {
                throw new AccessRightException(
                        "authorization failed - Required role for this operation: \"" + minimumRequiredRole + "\" - Your role is: \"" + roleEnum.toString()
                                + "\"");
            }

        }
    }
}
