package io.miragon.bpmnrepo.core.diagram.domain.business;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramSVGUploadTO;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import io.miragon.bpmnrepo.core.diagram.domain.mapper.DiagramMapper;
import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagram;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BpmnDiagramService {

    private final BpmnDiagramJpaRepository bpmnDiagramJpa;
    private final DiagramMapper mapper;

    public BpmnDiagramTO createDiagram(final BpmnDiagramTO bpmnDiagramTO) {
        final BpmnDiagram bpmnDiagram = this.mapper.toModel(bpmnDiagramTO);
        return this.mapper.toTO(this.saveToDb(bpmnDiagram));
    }

    public BpmnDiagramTO updateDiagram(final BpmnDiagramTO bpmnDiagramTO) {
        final BpmnDiagramEntity bpmnDiagramEntity = this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramTO.getBpmnDiagramId());
        final BpmnDiagram bpmnDiagram = this.mapper.toModel(bpmnDiagramEntity);
        bpmnDiagram.updateDiagram(bpmnDiagramTO);
        return this.mapper.toTO(this.saveToDb(bpmnDiagram));
    }

    public List<BpmnDiagramTO> getDiagramsFromRepo(final String repositoryId) {
        return this.bpmnDiagramJpa.findBpmnDiagramEntitiesByBpmnRepositoryId(repositoryId).stream()
                .map(this.mapper::toTO)
                .collect(Collectors.toList());
    }

    public BpmnDiagramTO getSingleDiagram(final String bpmnDiagramId) {
        return this.mapper.toTO(this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId));
    }

    public void updateUpdatedDate(final String bpmnDiagramId) {
        final BpmnDiagramEntity bpmnDiagramEntity = this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId);
        final BpmnDiagram bpmnDiagram = this.mapper.toModel(bpmnDiagramEntity);
        bpmnDiagram.updateDate();
        log.debug("Updating Date");
        this.saveToDb(bpmnDiagram);
    }

    private BpmnDiagramEntity saveToDb(final BpmnDiagram bpmnDiagram) {
        return this.bpmnDiagramJpa.save(this.mapper.toEntity(bpmnDiagram));
    }

    public Integer countExistingDiagrams(final String bpmnRepositoryId) {
        return this.bpmnDiagramJpa.countAllByBpmnRepositoryId(bpmnRepositoryId);
    }

    public void deleteDiagram(final String bpmnDiagramId) {
        final int deletedDiagrams = this.bpmnDiagramJpa.deleteBpmnDiagramEntityByBpmnDiagramId(bpmnDiagramId);
        log.info(String.format("Deleted %s Diagram", deletedDiagrams));
    }

    public void deleteAllByRepositoryId(final String bpmnRepositoryId) {
        //Auth check performed in Facade
        final int deletedDiagrams = this.bpmnDiagramJpa.deleteAllByBpmnRepositoryId(bpmnRepositoryId);
        log.debug(String.format("Deleted %s diagrams", deletedDiagrams));

    }

    public List<BpmnDiagramTO> getRecent(final List<String> assignments) {
        final List<BpmnDiagramTO> bpmnDiagramTOList = new ArrayList<>();
        assignments.forEach(assignment -> {
            this.bpmnDiagramJpa.findBpmnDiagramEntitiesByBpmnRepositoryId(assignment).forEach(bpmnDiagramEntity -> {
                bpmnDiagramTOList.add(this.mapper.toTO(bpmnDiagramEntity));
            });
        });
        bpmnDiagramTOList.sort(Comparator.comparing(a -> Timestamp.valueOf(a.getUpdatedDate())));
        Collections.reverse(bpmnDiagramTOList);
        return bpmnDiagramTOList.subList(0, Math.min(bpmnDiagramTOList.size(), 10));
    }

    public void updatePreviewSVG(final String bpmnDiagramId, final BpmnDiagramSVGUploadTO bpmnDiagramSVGUploadTO) {
        final BpmnDiagramEntity bpmnDiagramEntity = this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId);
        final BpmnDiagram bpmnDiagram = this.mapper.toModel(bpmnDiagramEntity);
        bpmnDiagram.setSvgPreview(bpmnDiagramSVGUploadTO.getSvgPreview());
        this.saveToDb(bpmnDiagram);
    }

    public List<BpmnDiagramTO> searchDiagrams(final List<String> assignedRepoIds, final String typedTitle) {
        final List<BpmnDiagramEntity> assignedDiagrams = this.bpmnDiagramJpa.findBpmnDiagramEntitiesByBpmnRepositoryIdIn(assignedRepoIds);
        final List<BpmnDiagramTO> matchingDiagrams = new ArrayList<>();
        for (final BpmnDiagramEntity diagram : assignedDiagrams) {
            if (diagram.getBpmnDiagramName().startsWith(typedTitle)) {
                matchingDiagrams.add(this.mapper.toTO(diagram));
            }
        }
        return matchingDiagrams;
    }
}
