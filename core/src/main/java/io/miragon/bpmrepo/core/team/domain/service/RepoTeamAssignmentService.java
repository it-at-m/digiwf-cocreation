package io.miragon.bpmrepo.core.team.domain.service;


import io.miragon.bpmrepo.core.shared.exception.ObjectNotFoundException;
import io.miragon.bpmrepo.core.team.domain.mapper.RepoTeamAssignmentMapper;
import io.miragon.bpmrepo.core.team.domain.model.RepoTeamAssignment;
import io.miragon.bpmrepo.core.team.infrastructure.entity.RepoTeamAssignmentEntity;
import io.miragon.bpmrepo.core.team.infrastructure.entity.RepoTeamAssignmentId;
import io.miragon.bpmrepo.core.team.infrastructure.repository.RepoTeamAssignmentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepoTeamAssignmentService {

    private final RepoTeamAssignmentJpaRepository repoTeamAssignmentJpaRepository;
    private final RepoTeamAssignmentMapper mapper;


    public RepoTeamAssignment createAssignment(final RepoTeamAssignment repoTeamAssignment) {
        log.debug("Persisting new Assignment for Team to Repo");
        return this.saveToDb(repoTeamAssignment);
        //TODO: Maybe add a field in "Team" which contains the amount of assigned Repositories - the update of this amount has to be called from here
    }

    private RepoTeamAssignment saveToDb(final RepoTeamAssignment repoTeamAssignment) {
        final RepoTeamAssignmentId repoTeamAssignmentId = this.mapper.mapToEmbeddable(repoTeamAssignment.getRepositoryId(), repoTeamAssignment.getTeamId());
        final RepoTeamAssignmentEntity repoTeamAssignmentEntity = this.repoTeamAssignmentJpaRepository.save(this.mapper.mapToEntity(repoTeamAssignment, repoTeamAssignmentId));
        return this.mapper.mapToModel(repoTeamAssignmentEntity);
    }

    public RepoTeamAssignment updateAssignment(final RepoTeamAssignment repoTeamAssignment) {
        final RepoTeamAssignment updatedRepoTeamAssignment = this.getRepoTeamAssignment(repoTeamAssignment);
        updatedRepoTeamAssignment.updateRole(repoTeamAssignment.getRole());
        return this.saveToDb(updatedRepoTeamAssignment);
    }

    public RepoTeamAssignment getRepoTeamAssignment(final RepoTeamAssignment repoTeamAssignment) {
        final RepoTeamAssignment queriedRepoTeamAssignment = this.repoTeamAssignmentJpaRepository.findByRepoTeamAssignmentId_RepositoryIdAndRepoTeamAssignmentId_TeamId(repoTeamAssignment.getRepositoryId(), repoTeamAssignment.getTeamId())
                .map(this.mapper::mapToModel)
                .orElseThrow(() -> new ObjectNotFoundException("repoTeamAssignment.notFound"));
        return queriedRepoTeamAssignment;
    }

    public List<RepoTeamAssignment> getAllAssignmentsByTeamId(final String teamId) {
        return this.mapper.mapToModel(this.repoTeamAssignmentJpaRepository.findAllByRepoTeamAssignmentId_TeamId(teamId));
    }

}
