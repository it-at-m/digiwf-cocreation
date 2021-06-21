package io.miragon.bpmnrepo.core.diagram.domain.business;


import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramVersionTO;
import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import io.miragon.bpmnrepo.core.diagram.domain.mapper.VersionMapper;
import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagramVersion;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramVersionEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramVersionJpa;
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

    public String updateVersion(final BpmnDiagramVersionTO bpmnDiagramVersionTO) {
        final BpmnDiagramVersionEntity bpmnDiagramVersionEntity = this.bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(bpmnDiagramVersionTO.getBpmnDiagramId());
        final BpmnDiagramVersion bpmnDiagramVersion = this.mapper.toModel(bpmnDiagramVersionEntity);
        bpmnDiagramVersion.updateVersion(bpmnDiagramVersionTO);
        final String bpmnDiagramVersionId = this.saveToDb(bpmnDiagramVersion);
        return bpmnDiagramVersionId;
    }

    public String createNewVersion(final BpmnDiagramVersionTO bpmnDiagramVersionTO) {
        log.warn("in service Creating new version, diagramId: " + bpmnDiagramVersionTO.getBpmnDiagramId());
        final BpmnDiagramVersionEntity bpmnDiagramVersionEntity = this.bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(bpmnDiagramVersionTO.getBpmnDiagramId());
        bpmnDiagramVersionTO.setBpmnDiagramVersionRelease(bpmnDiagramVersionEntity.getBpmnDiagramVersionRelease());
        log.warn("Release: " + bpmnDiagramVersionTO.getBpmnDiagramVersionRelease().toString());
        bpmnDiagramVersionTO.setBpmnDiagramVersionMilestone(bpmnDiagramVersionEntity.getBpmnDiagramVersionMilestone());
        log.warn("Milearonw: " + bpmnDiagramVersionTO.getBpmnDiagramVersionMilestone().toString());
        final BpmnDiagramVersion bpmnDiagramVersion = new BpmnDiagramVersion(bpmnDiagramVersionTO);
        log.warn("Generating new milestone and release numbers. Savetype: " + bpmnDiagramVersion.getSaveType().toString());
        log.warn("Release and Milestone: " + bpmnDiagramVersion.getBpmnDiagramVersionRelease() + "." + bpmnDiagramVersion.getBpmnDiagramVersionMilestone());
        final String bpmnDiagramVersionId = this.saveToDb(bpmnDiagramVersion);
        return bpmnDiagramVersionId;
    }

    public String createInitialVersion(final BpmnDiagramVersionTO bpmnDiagramVersionTO) {
        final BpmnDiagramVersion bpmnDiagramVersion = new BpmnDiagramVersion(bpmnDiagramVersionTO);
        final String bpmnDiagramVersionId = this.saveToDb(bpmnDiagramVersion);
        return bpmnDiagramVersionId;
    }


    public List<BpmnDiagramVersionTO> getAllVersions(final String bpmnDiagramId) {
        //1. Query all Versions by providing the diagram id
        //2. for all versions: map them to a TO
        return this.bpmnDiagramVersionJpa.findAllByBpmnDiagramId(bpmnDiagramId).stream()
                .map(this.mapper::toTO)
                .collect(Collectors.toList());
    }

    public BpmnDiagramVersionTO getLatestVersion(final String bpmnDiagramId) {
        final BpmnDiagramVersionEntity bpmnDiagramVersionEntity = this.bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(bpmnDiagramId);
        return this.mapper.toTO(bpmnDiagramVersionEntity);
    }

    public BpmnDiagramVersionTO getSingleVersion(final String bpmnDiagramVersionId) {
        return this.mapper.toTO(this.bpmnDiagramVersionJpa.findAllByBpmnDiagramVersionIdEquals(bpmnDiagramVersionId));
    }

    private String saveToDb(final BpmnDiagramVersion bpmnDiagramVersion) {
        System.out.println(bpmnDiagramVersion);
        final BpmnDiagramVersionEntity bpmnDiagramVersionEntity = this.mapper.toEntity(bpmnDiagramVersion);
        log.warn(bpmnDiagramVersionEntity.getBpmnDiagramId());
        this.bpmnDiagramVersionJpa.save(bpmnDiagramVersionEntity);
        log.debug("Saving successful");
        final BpmnDiagramVersionEntity createdBpmnDiagramVersionEntity = this.bpmnDiagramVersionJpa.findFirstByBpmnDiagramIdAndBpmnRepositoryIdOrderByBpmnDiagramVersionReleaseDescBpmnDiagramVersionMilestoneDesc(bpmnDiagramVersion.getBpmnDiagramId(), bpmnDiagramVersion.getBpmnRepositoryId());
        return (createdBpmnDiagramVersionEntity.getBpmnDiagramVersionId());
    }


    public void deleteAllByDiagramId(final String bpmnDiagramId) {
        //Auth Check in Facade
        final int deletedVersions = this.bpmnDiagramVersionJpa.deleteAllByBpmnDiagramId(bpmnDiagramId);
        log.debug(String.format("Deleted %s versions", deletedVersions));
    }

    public void deleteAllByRepositoryId(final String bpmnRepositoryId) {
        //Auth check in Facade
        final int deletedVersions = this.bpmnDiagramVersionJpa.deleteAllByBpmnRepositoryId(bpmnRepositoryId);
        log.debug(String.format("Deleted %s versions", deletedVersions));
    }

    public void deleteAutosavedVersions(final String bpmnRepositoryId, final String bpmnDiagramId) {
        this.bpmnDiagramVersionJpa.deleteAllByBpmnRepositoryIdAndBpmnDiagramIdAndSaveType(bpmnRepositoryId, bpmnDiagramId, SaveTypeEnum.AUTOSAVE);

    }
}
