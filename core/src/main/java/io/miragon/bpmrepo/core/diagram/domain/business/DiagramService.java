package io.miragon.bpmrepo.core.diagram.domain.business;

import io.miragon.bpmrepo.core.diagram.domain.mapper.DiagramMapper;
import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramUpdate;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.DiagramEntity;
import io.miragon.bpmrepo.core.diagram.infrastructure.repository.DiagramJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagramService {

    private final DiagramJpaRepository diagramJpaRepository;
    private final DiagramMapper mapper;

    public Diagram createDiagram(final Diagram diagram) {
        return this.saveDiagram(diagram);
    }

    public Diagram updateDiagram(final String diagramId, final DiagramUpdate diagramUpdate) {
        log.debug("Updating Daigram " + diagramUpdate);
        final Diagram diagram = this.getDiagramById(diagramId);
        diagram.updateDiagram(diagramUpdate);
        return this.saveDiagram(diagram);
    }

    public List<Diagram> getDiagramsByRepo(final String repositoryId) {
        final List<DiagramEntity> diagrams = this.diagramJpaRepository.findAllByRepositoryId(repositoryId);
        return this.mapper.mapToModel(diagrams);
    }

    public Diagram getDiagramById(final String diagramId) {
        return this.diagramJpaRepository.findById(diagramId)
                .map(this.mapper::mapToModel)
                .orElseThrow();
    }

    public void updateUpdatedDate(final String diagramId) {
        log.debug("Updating Date");

        final Diagram diagram = this.getDiagramById(diagramId);
        diagram.updateDate();
        this.saveDiagram(diagram);
    }

    private Diagram saveDiagram(final Diagram bpmnDiagram) {
        val savedDiagram = this.diagramJpaRepository.save(this.mapper.mapToEntity(bpmnDiagram));
        return this.mapper.mapToModel(savedDiagram);
    }

    public Integer countExistingDiagrams(final String repositoryId) {
        return this.diagramJpaRepository.countAllByRepositoryId(repositoryId);
    }

    public void deleteDiagram(final String diagramId) {
        this.diagramJpaRepository.deleteById(diagramId);
        log.info(String.format("Deleted %s Diagram", diagramId));
    }

    public void deleteAllByRepositoryId(final String bpmnRepositoryId) {
        //Auth check performed in Facade
        final int deletedDiagrams = this.diagramJpaRepository.deleteAllByRepositoryId(bpmnRepositoryId);
        log.debug(String.format("Deleted %s diagrams", deletedDiagrams));

    }

    public List<Diagram> getRecent(final List<String> assignments) {
        //TODO Improve performance -> save in separate db
        final List<DiagramEntity> diagrams = assignments.stream()
                .map(this.diagramJpaRepository::findAllByRepositoryId)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(a -> Timestamp.valueOf(a.getUpdatedDate())))
                .collect(Collectors.toList());
        Collections.reverse(diagrams);
        return this.mapper.mapToModel(diagrams.subList(0, Math.min(diagrams.size(), 10)));
    }

    public void updatePreviewSVG(final String diagramId, final String svgPreview) {
        final Diagram diagram = this.getDiagramById(diagramId);
        diagram.updateSvgPreview(svgPreview);
        this.saveDiagram(diagram);
    }

    public List<Diagram> searchDiagrams(final List<String> assignedRepoIds, final String typedTitle) {
        final List<DiagramEntity> assignedDiagrams = this.diagramJpaRepository.findAllByRepositoryIdInAndNameStartsWith(assignedRepoIds, typedTitle);
        return this.mapper.mapToModel(assignedDiagrams);
    }
}
