package io.miragon.bpmnrepo.core.diagram.domain.business;


import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagramVersion;
import io.miragon.bpmnrepo.core.diagram.domain.mapper.VersionMapper;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramVersionEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramVersionJpa;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BpmnDiagramVersionService {

    private final BpmnDiagramVersionJpa bpmnDiagramVersionJpa;
    private final VersionMapper mapper;

    public String updateVersion(BpmnDiagramVersionTO bpmnDiagramVersionTO) {
        BpmnDiagramVersionEntity bpmnDiagramVersionEntity =  this.bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(bpmnDiagramVersionTO.getBpmnDiagramId());
        BpmnDiagramVersion bpmnDiagramVersion = this.mapper.toModel(bpmnDiagramVersionEntity);
        bpmnDiagramVersion.updateVersion(bpmnDiagramVersionTO);
        String bpmnDiagramVersionId = this.saveToDb(bpmnDiagramVersion);
        return bpmnDiagramVersionId;
    }


    public String createInitialVersion(BpmnDiagramVersionTO bpmnDiagramVersionTO){
        BpmnDiagramVersion bpmnDiagramVersion = new BpmnDiagramVersion(bpmnDiagramVersionTO);
        String bpmnDiagramVersionId = this.saveToDb(bpmnDiagramVersion);
        return bpmnDiagramVersionId;
    }


    public List<BpmnDiagramVersionTO> getAllVersions(String bpmnDiagramId){
        //1. Query all Versions by providing the diagram id
        //2. for all versions: map them to a TO
        return this.bpmnDiagramVersionJpa.findAllByBpmnDiagramId(bpmnDiagramId).stream()
                    .map(this.mapper::toTO)
                    .collect(Collectors.toList());
    }

    public BpmnDiagramVersionTO getLatestVersion(String bpmnDiagramId){
        BpmnDiagramVersionEntity bpmnDiagramVersionEntity = this.bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(bpmnDiagramId);
        return this.mapper.toTO(bpmnDiagramVersionEntity);
    }

    public BpmnDiagramVersionTO getSingleVersion(String bpmnDiagramVersionId){
            return this.mapper.toTO(bpmnDiagramVersionJpa.findAllByBpmnDiagramVersionIdEquals(bpmnDiagramVersionId));
    }

    private String saveToDb(BpmnDiagramVersion bpmnDiagramVersion){
        bpmnDiagramVersionJpa.save(this.mapper.toEntity(bpmnDiagramVersion));
        log.debug("Saving successful");
        BpmnDiagramVersionEntity bpmnDiagramVersionEntity = this.bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdAndBpmnRepositoryIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(bpmnDiagramVersion.getBpmnDiagramId(), bpmnDiagramVersion.getBpmnRepositoryId());
        return (bpmnDiagramVersionEntity.getBpmnDiagramId());
    }


    public void deleteAllByDiagramId(String bpmnDiagramId){
        //Auth Check in Facade
        int deletedVersions = this.bpmnDiagramVersionJpa.deleteAllByBpmnDiagramId(bpmnDiagramId);
        log.debug(String.format("Deleted %s versions", deletedVersions));
    }

    public void deleteAllByRepositoryId(String bpmnRepositoryId){
        //Auth check in Facade
        int deletedVersions = this.bpmnDiagramVersionJpa.deleteAllByBpmnRepositoryId(bpmnRepositoryId);
        log.debug(String.format("Deleted %s versions", deletedVersions));
    }

    public void deleteAutosavedVersions(String bpmnRepositoryId, String bpmnDiagramId) {
        this.bpmnDiagramVersionJpa.deleteAllByBpmnRepositoryIdAndBpmnDiagramIdAndSaveType(bpmnRepositoryId, bpmnDiagramId, SaveTypeEnum.AUTOSAVE);

    }
}
