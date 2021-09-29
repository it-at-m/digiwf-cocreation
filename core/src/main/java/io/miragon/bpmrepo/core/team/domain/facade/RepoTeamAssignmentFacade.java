package io.miragon.bpmrepo.core.team.domain.facade;

import io.miragon.bpmrepo.core.repository.domain.service.AuthService;
import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmrepo.core.team.domain.model.RepoTeamAssignment;
import io.miragon.bpmrepo.core.team.domain.service.RepoTeamAssignmentService;
import io.miragon.bpmrepo.core.team.domain.service.TeamAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepoTeamAssignmentFacade {

    private final TeamAuthService teamAuthService;
    private final AuthService authService;
    private final RepoTeamAssignmentService repoTeamAssignmentService;

    public RepoTeamAssignment createAssignment(final RepoTeamAssignment repoTeamAssignment) {
        log.debug("Checking Permissions");
        //Required: Admin in Repository & Member of Team
        this.authService.checkIfOperationIsAllowed(repoTeamAssignment.getRepositoryId(), RoleEnum.ADMIN);
        this.teamAuthService.checkIfTeamOperationIsAllowed(repoTeamAssignment.getTeamId(), RoleEnum.MEMBER);
        return this.repoTeamAssignmentService.createAssignment(repoTeamAssignment);
    }

    public RepoTeamAssignment updateAssignment(final RepoTeamAssignment repoTeamAssignment) {
        log.debug("Checking Permissions");
        //Required: Admin in Repository, no role in the team
        this.authService.checkIfOperationIsAllowed(repoTeamAssignment.getRepositoryId(), RoleEnum.ADMIN);
        return this.repoTeamAssignmentService.updateAssignment(repoTeamAssignment);
    }

    public void deleteAssignment(final String teamId, final String repoId) {
        log.debug("Checking Permissions");
        log.error("Function not yet implemented");
    }

    public List<RepoTeamAssignment> getAllAssignmentsByTeamId(final String teamId) {
        log.debug("Checking Permissions");
        this.teamAuthService.checkIfTeamOperationIsAllowed(teamId, RoleEnum.VIEWER);
        return this.repoTeamAssignmentService.getAllAssignmentsByTeamId(teamId);
    }
}
