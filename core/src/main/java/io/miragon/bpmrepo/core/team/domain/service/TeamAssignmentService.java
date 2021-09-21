package io.miragon.bpmrepo.core.team.domain.service;

import io.miragon.bpmrepo.core.team.domain.mapper.TeamAssignmentMapper;
import io.miragon.bpmrepo.core.team.domain.model.TeamAssignment;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamAssignmentEntity;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamAssignmentId;
import io.miragon.bpmrepo.core.team.infrastructure.repository.TeamAssignmentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


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
        this.teamService.updateAssignedUsers(teamAssignment.getTeamId(), assignedUsers);
        return createdAssignment;
    }


    public TeamAssignment createInitialTeamAssignment(final TeamAssignment teamAssignment) {
        log.debug("Creating initial Assignment for Team {}", teamAssignment.getTeamId());
        return this.saveToDb(teamAssignment);
    }


    public TeamAssignment saveToDb(final TeamAssignment teamAssignment) {
        final TeamAssignmentId teamAssignmentId = this.mapper.mapToEmbeddable(teamAssignment.getUserId(), teamAssignment.getTeamId());
        final TeamAssignmentEntity savedEntity = this.teamAssignmentJpaRepository
                .save(this.mapper.mapToEntity(teamAssignment, teamAssignmentId));
        return this.mapper.mapToModel(savedEntity);
    }
}
