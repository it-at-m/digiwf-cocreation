package io.miragon.bpmnrepo.core.repository.domain.business;

import io.miragon.bpmnrepo.core.repository.api.transport.BpmnRepositoryRequestTO;
import io.miragon.bpmnrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmnrepo.core.repository.domain.mapper.RepositoryMapper;
import io.miragon.bpmnrepo.core.repository.domain.model.BpmnRepository;
import io.miragon.bpmnrepo.core.repository.infrastructure.entity.BpmnRepositoryEntity;
import io.miragon.bpmnrepo.core.repository.infrastructure.repository.BpmnRepoJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BpmnRepositoryService {

    private final RepositoryMapper mapper;
    private final BpmnRepoJpaRepository bpmnRepoJpa;

    public String createRepository(final NewBpmnRepositoryTO newBpmnRepositoryTO) {
        BpmnRepository bpmnRepository = this.mapper.toModel(newBpmnRepositoryTO);
        final BpmnRepositoryEntity bpmnRepositoryEntity = this.mapper.toEntity(bpmnRepository);
        bpmnRepository = this.saveToDb(bpmnRepositoryEntity);
        return bpmnRepository.getBpmnRepositoryId();
    }

    public void updateRepository(final String bpmnRepositoryId, final NewBpmnRepositoryTO newBpmnRepositoryTO) {
        BpmnRepositoryEntity bpmnRepositoryEntity = this.bpmnRepoJpa.getOne(bpmnRepositoryId);
        final BpmnRepository bpmnRepository = this.mapper.toModel(bpmnRepositoryEntity);
        bpmnRepository.updateRepo(newBpmnRepositoryTO);
        bpmnRepositoryEntity = this.mapper.toEntity(bpmnRepository);
        this.saveToDb(bpmnRepositoryEntity);
    }

    public BpmnRepositoryRequestTO getSingleRepository(final String repositoryId) {
        return this.mapper.toRequestTO(this.bpmnRepoJpa.findByBpmnRepositoryId(repositoryId));
    }

    public void updateAssignedUsers(final String bpmnRepositoryId, final Integer assignedUsers) {
        final BpmnRepository bpmnRepository = this.mapper.toModel(this.bpmnRepoJpa.findByBpmnRepositoryId(bpmnRepositoryId));
        bpmnRepository.setAssignedUsers(assignedUsers);
        this.bpmnRepoJpa.save(this.mapper.toEntity(bpmnRepository));
    }

    public void updateExistingDiagrams(final String bpmnRepositoryId, final Integer existingDiagrams) {
        final BpmnRepository bpmnRepository = this.mapper.toModel(this.bpmnRepoJpa.findByBpmnRepositoryId(bpmnRepositoryId));
        bpmnRepository.setExistingDiagrams(existingDiagrams);
        this.bpmnRepoJpa.save(this.mapper.toEntity(bpmnRepository));
    }

    public void deleteRepository(final String bpmnRepositoryId) {
        this.bpmnRepoJpa.deleteBpmnRepositoryEntityByBpmnRepositoryId(bpmnRepositoryId);
    }

    public BpmnRepository saveToDb(final BpmnRepositoryEntity bpmnRepositoryEntity) {
        return this.mapper.toModel(this.bpmnRepoJpa.save(bpmnRepositoryEntity));
    }
}
