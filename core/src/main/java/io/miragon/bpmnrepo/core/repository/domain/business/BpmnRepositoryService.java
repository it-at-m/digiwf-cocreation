package io.miragon.bpmnrepo.core.repository.domain.business;


import io.miragon.bpmnrepo.core.repository.api.transport.BpmnRepositoryRequestTO;
import io.miragon.bpmnrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import io.miragon.bpmnrepo.core.repository.domain.mapper.RepositoryMapper;
import io.miragon.bpmnrepo.core.repository.domain.model.BpmnRepository;
import io.miragon.bpmnrepo.core.repository.infrastructure.entity.BpmnRepositoryEntity;
import io.miragon.bpmnrepo.core.repository.infrastructure.repository.BpmnRepoJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BpmnRepositoryService {

    private final RepositoryMapper mapper;
    private final BpmnRepoJpa bpmnRepoJpa;

    public String createRepository(NewBpmnRepositoryTO newBpmnRepositoryTO){
        BpmnRepository bpmnRepository = this.mapper.toModel(newBpmnRepositoryTO);
        BpmnRepositoryEntity bpmnRepositoryEntity = this.mapper.toEntity(bpmnRepository);
        bpmnRepository = this.saveToDb(bpmnRepositoryEntity);
        return bpmnRepository.getBpmnRepositoryId();
    }



    public void updateRepository(String bpmnRepositoryId, NewBpmnRepositoryTO newBpmnRepositoryTO){
        BpmnRepositoryEntity bpmnRepositoryEntity = this.bpmnRepoJpa.getOne(bpmnRepositoryId);
        BpmnRepository bpmnRepository = this.mapper.toModel(bpmnRepositoryEntity);
        bpmnRepository.updateRepo(newBpmnRepositoryTO);
        bpmnRepositoryEntity = this.mapper.toEntity(bpmnRepository);
        this.saveToDb(bpmnRepositoryEntity);
    }

    public BpmnRepositoryRequestTO getSingleRepository(String repositoryId){
        return this.mapper.toRequestTO(this.bpmnRepoJpa.findByBpmnRepositoryId(repositoryId));
    }

    public void updateAssignedUsers(String bpmnRepositoryId, Integer assignedUsers){
        BpmnRepository bpmnRepository = this.mapper.toModel(this.bpmnRepoJpa.findByBpmnRepositoryId(bpmnRepositoryId));
        bpmnRepository.setAssignedUsers(assignedUsers);
        this.bpmnRepoJpa.save(this.mapper.toEntity(bpmnRepository));
    }

    public void updateExistingDiagrams(String bpmnRepositoryId, Integer existingDiagrams){
        BpmnRepository bpmnRepository = this.mapper.toModel(this.bpmnRepoJpa.findByBpmnRepositoryId(bpmnRepositoryId));
        bpmnRepository.setExistingDiagrams(existingDiagrams);
        this.bpmnRepoJpa.save(this.mapper.toEntity(bpmnRepository));
    }

    public void deleteRepository(String bpmnRepositoryId){
        this.bpmnRepoJpa.deleteBpmnRepositoryEntityByBpmnRepositoryId(bpmnRepositoryId);
    }


    public BpmnRepository saveToDb(final BpmnRepositoryEntity bpmnRepositoryEntity){
        return this.mapper.toModel(bpmnRepoJpa.save(bpmnRepositoryEntity));
    }
}
