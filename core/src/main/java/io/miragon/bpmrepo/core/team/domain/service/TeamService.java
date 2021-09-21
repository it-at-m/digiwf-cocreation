package io.miragon.bpmrepo.core.team.domain.service;

import io.miragon.bpmrepo.core.team.domain.mapper.TeamMapper;
import io.miragon.bpmrepo.core.team.domain.model.NewTeam;
import io.miragon.bpmrepo.core.team.domain.model.Team;
import io.miragon.bpmrepo.core.team.infrastructure.entity.TeamEntity;
import io.miragon.bpmrepo.core.team.infrastructure.repository.TeamJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamMapper mapper;
    private final TeamJpaRepository teamJpaRepository;


    public Team createTeam(final NewTeam newTeam) {
        log.debug("Persisting new Team");
        final Team team = new Team(newTeam);
        return this.saveToDb(team);
    }


    public Team saveToDb(final Team team) {
        final TeamEntity savedTeam = this.teamJpaRepository.save(this.mapper.mapToEntity(team));
        return this.mapper.mapToModel(savedTeam);
    }

    public void updateAssignedUsers(final String teamId, final Integer assignedUsers) {
        final Team team = this.getTeam(teamId);
        team.updateAssignedUsers(assignedUsers);
        this.saveToDb(team);
    }

    public Team getTeam(final String teamId) {
        log.debug("Querying Team");
        return this.mapper.mapToModel(this.teamJpaRepository.getOne(teamId));
    }
}


