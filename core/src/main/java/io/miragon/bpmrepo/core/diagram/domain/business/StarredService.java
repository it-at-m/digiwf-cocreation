package io.miragon.bpmrepo.core.diagram.domain.business;

import io.miragon.bpmrepo.core.diagram.domain.mapper.StarredMapper;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.StarredEntity;
import io.miragon.bpmrepo.core.diagram.infrastructure.entity.StarredId;
import io.miragon.bpmrepo.core.diagram.infrastructure.repository.StarredJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StarredService {

    private final StarredJpaRepository starredJpa;
    private final StarredMapper mapper;

    public void setStarred(final String diagramId, final String userId) {
        final StarredEntity starredEntity = this.starredJpa.findById_DiagramIdAndId_UserId(diagramId, userId);
        if (starredEntity == null) {
            this.createStarred(diagramId, userId);
        } else {
            this.deleteStarred(diagramId, userId);
        }
    }

    public void createStarred(final String diagramId, final String userId) {
        final StarredId starredId = this.mapper.toEmbeddable(diagramId, userId);
        final StarredEntity starredEntity = this.mapper.toEntity(starredId);
        this.starredJpa.save(starredEntity);
    }

    public void deleteStarred(final String bpmnDiagramId, final String userId) {
        this.starredJpa.deleteById_DiagramIdAndId_UserId(bpmnDiagramId, userId);
    }

    public List<StarredEntity> getStarred(final String userId) {
        return this.starredJpa.findAllById_UserId(userId);

    }
}
