package io.miragon.bpmrepo.core.team.domain.facade;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.team.api.transport.TeamAssignmentTO;
import io.miragon.bpmrepo.core.team.domain.mapper.TeamAssignmentMapper;
import io.miragon.bpmrepo.core.team.domain.model.TeamAssignment;
import io.miragon.bpmrepo.core.team.domain.service.TeamAssignmentService;
import io.miragon.bpmrepo.core.team.domain.service.TeamAuthService;
import io.miragon.bpmrepo.core.user.domain.model.User;
import io.miragon.bpmrepo.core.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamAssignmentFacade {
    private final TeamAssignmentMapper mapper;
    private final TeamAuthService authService;
    private final UserService userService;
    private final TeamAssignmentService teamAssignmentService;

    public TeamAssignment createTeamAssignment(final TeamAssignmentTO teamAssignmentTO) {
        log.debug("Creating Assignment for Team {}", teamAssignmentTO.getTeamId());
        this.authService.checkIfTeamOperationIsAllowed(teamAssignmentTO.getTeamId(), RoleEnum.ADMIN);
        this.authService.checkIfAssignedRoleIsLowerThanOwnRole(teamAssignmentTO.getTeamId(), teamAssignmentTO.getRole());

        final TeamAssignment teamAssignment = this.mapper.mapToModel(teamAssignmentTO);
        return this.teamAssignmentService.createTeamAssignment(teamAssignment);
    }

    public TeamAssignment createInitialTeamAssignment(final String teamId, final User user) {
        final TeamAssignment teamAssignment = new TeamAssignment(user.getId(), user.getUsername(), teamId, RoleEnum.OWNER);
        return this.teamAssignmentService.createInitialTeamAssignment(teamAssignment);
    }


}
