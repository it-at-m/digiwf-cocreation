package io.miragon.bpmrepo.core.team.domain.service;

import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import io.miragon.bpmrepo.core.team.domain.mapper.TeamAssignmentMapper;
import io.miragon.bpmrepo.core.team.domain.model.TeamAssignment;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamAssignmentEntity;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamAssignmentId;
import io.miragon.bpmrepo.core.team.infrastructure.repository.TeamAssignmentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class TeamAssignmentService {

    private final TeamAssignmentMapper mapper;
    private final TeamAssignmentJpaRepository teamAssignmentJpaRepository;
    private final TeamService teamService;


    public TeamAssignment createTeamAssignment(final TeamAssignment teamAssignment) {
        log.debug("Persisting new assignment for Team");
        final TeamAssignment createdAssignment = this.saveToDb(teamAssignment);
        final Integer assignedUsers = this.teamAssignmentJpaRepository.countByTeamAssignmentId_TeamId(teamAssignment.getTeamId());
        this.teamService.updateAssignedUsers(createdAssignment.getTeamId(), assignedUsers);
        return createdAssignment;
    }


    public TeamAssignment createInitialTeamAssignment(final TeamAssignment teamAssignment) {
        log.debug("Creating initial Assignment for Team {}", teamAssignment.getTeamId());
        final TeamAssignment createdAssignment = this.saveToDb(teamAssignment);
        this.teamService.updateAssignedUsers(createdAssignment.getTeamId(), 1);
        return createdAssignment;
    }

    public List<TeamAssignment> getAllAssignments(final String userId) {
        return this.mapper.mapToModel(this.teamAssignmentJpaRepository.findAllByTeamAssignmentId_UserId(userId));
    }

    public List<TeamAssignment> getAllAssignedUsers(final String teamId) {
        return this.mapper.mapToModel(this.teamAssignmentJpaRepository.findAllByTeamAssignmentId_TeamId(teamId));
    }

    public TeamAssignment updateTeamAssignment(final TeamAssignment updatedTeamAssignment) {
        log.debug("Persisting role update");
        final TeamAssignment teamAssignment = this.teamAssignmentJpaRepository.findByTeamAssignmentId_TeamIdAndTeamAssignmentId_UserId(updatedTeamAssignment.getTeamId(), updatedTeamAssignment.getUserId())
                .map(this.mapper::mapToModel)
                .orElseThrow(() -> new ObjectNotFoundException("teamAssignment.notFound"));
        teamAssignment.updateRole(updatedTeamAssignment.getRole());
        return this.saveToDb(teamAssignment);
    }

    public TeamAssignment saveToDb(final TeamAssignment teamAssignment) {
        final TeamAssignmentId teamAssignmentId = this.mapper.mapToEmbeddable(teamAssignment.getUserId(), teamAssignment.getTeamId());
        final TeamAssignmentEntity savedEntity = this.teamAssignmentJpaRepository
                .save(this.mapper.mapToEntity(teamAssignment, teamAssignmentId));
        return this.mapper.mapToModel(savedEntity);
    }

    public void deleteAssignment(final String teamId, final String userId) {
        log.debug("Deleting Assignment");
        this.teamAssignmentJpaRepository.deleteByTeamAssignmentId_TeamIdAndTeamAssignmentId_UserId(teamId, userId);
    }
}
