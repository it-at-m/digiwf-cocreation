package io.miragon.bpmnrepo.core.diagram.domain.business;

import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramSVGUploadTO;
import io.miragon.bpmnrepo.core.diagram.domain.mapper.DiagramMapper;
import io.miragon.bpmnrepo.core.diagram.domain.model.BpmnDiagram;
import io.miragon.bpmnrepo.core.diagram.infrastructure.entity.BpmnDiagramEntity;
import io.miragon.bpmnrepo.core.diagram.infrastructure.repository.BpmnDiagramJpa;
import io.miragon.bpmnrepo.core.diagram.api.transport.BpmnDiagramTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BpmnDiagramService {

    private final BpmnDiagramJpa bpmnDiagramJpa;
    private final DiagramMapper mapper;


    public BpmnDiagramTO createDiagram(BpmnDiagramTO bpmnDiagramTO){
        BpmnDiagram bpmnDiagram = this.mapper.toModel(bpmnDiagramTO);
        return this.mapper.toTO(saveToDb(bpmnDiagram));
    }

    public BpmnDiagramTO updateDiagram(BpmnDiagramTO bpmnDiagramTO){
        BpmnDiagramEntity bpmnDiagramEntity = this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramTO.getBpmnDiagramId());
        BpmnDiagram bpmnDiagram = this.mapper.toModel(bpmnDiagramEntity);
        bpmnDiagram.updateDiagram(bpmnDiagramTO);
        return this.mapper.toTO(saveToDb(bpmnDiagram));
    }


    public List<BpmnDiagramTO> getDiagramsFromRepo(String repositoryId){
        return this.bpmnDiagramJpa.findBpmnDiagramEntitiesByBpmnRepositoryId(repositoryId).stream()
                .map(this.mapper::toTO)
                .collect(Collectors.toList());
    }


    public BpmnDiagramTO getSingleDiagram(String bpmnDiagramId){
        return this.mapper.toTO(this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId));
    }


    public void updateUpdatedDate(String bpmnDiagramId){
        BpmnDiagramEntity bpmnDiagramEntity = this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId);
        BpmnDiagram bpmnDiagram = this.mapper.toModel(bpmnDiagramEntity);
        bpmnDiagram.updateDate();
        log.debug("Updating Date");
        this.saveToDb(bpmnDiagram);
    }


    private BpmnDiagramEntity saveToDb(BpmnDiagram bpmnDiagram){
        return bpmnDiagramJpa.save(this.mapper.toEntity(bpmnDiagram));
    }


    public Integer countExistingDiagrams(String bpmnRepositoryId){
        return this.bpmnDiagramJpa.countAllByBpmnRepositoryId(bpmnRepositoryId);
    }

    public void deleteDiagram(String bpmnDiagramId){
        int deletedDiagrams = this.bpmnDiagramJpa.deleteBpmnDiagramEntitiyByBpmnDiagramId(bpmnDiagramId);
        log.info(String.format("Deleted %s Diagram", deletedDiagrams));
    }


    public void deleteAllByRepositoryId(String bpmnRepositoryId){
        //Auth check performed in Facade
        int deletedDiagrams = this.bpmnDiagramJpa.deleteAllByBpmnRepositoryId(bpmnRepositoryId);
        log.debug(String.format("Deleted %s diagrams", deletedDiagrams));

    }

    public List<BpmnDiagramTO> getRecent(List<String> assignments) {
        List<BpmnDiagramTO> bpmnDiagramTOList = new ArrayList<>();
        assignments.forEach(assignment -> {
            this.bpmnDiagramJpa.findBpmnDiagramEntitiesByBpmnRepositoryId(assignment).forEach(bpmnDiagramEntity -> {
                bpmnDiagramTOList.add(this.mapper.toTO(bpmnDiagramEntity));
            });
        });
        bpmnDiagramTOList.sort(Comparator.comparing(a -> Timestamp.valueOf(a.getUpdatedDate())));
        Collections.reverse(bpmnDiagramTOList);
        return bpmnDiagramTOList.subList(0, Math.min(bpmnDiagramTOList.size(), 10));
    }


    public void updatePreviewSVG(String bpmnDiagramId, BpmnDiagramSVGUploadTO bpmnDiagramSVGUploadTO) {
        BpmnDiagramEntity bpmnDiagramEntity = this.bpmnDiagramJpa.findBpmnDiagramEntityByBpmnDiagramIdEquals(bpmnDiagramId);
        BpmnDiagram bpmnDiagram = this.mapper.toModel(bpmnDiagramEntity);
        bpmnDiagram.setSvgPreview(bpmnDiagramSVGUploadTO.getSvgPreview());
        this.saveToDb(bpmnDiagram);
    }
}
