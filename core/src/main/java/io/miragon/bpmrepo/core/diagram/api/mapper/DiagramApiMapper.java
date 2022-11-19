package io.miragon.bpmrepo.core.diagram.api.mapper;

import io.miragon.bpmrepo.core.diagram.api.transport.DiagramTO;
import io.miragon.bpmrepo.core.diagram.api.transport.DiagramUpdateTO;
import io.miragon.bpmrepo.core.diagram.api.transport.NewDiagramTO;
import io.miragon.bpmrepo.core.diagram.domain.model.Diagram;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramUpdate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DiagramApiMapper {

    Diagram mapToModel(final NewDiagramTO to);

    DiagramTO mapToTO(final io.miragon.bpmrepo.core.diagram.domain.model.Diagram model);

    List<DiagramTO> mapToTO(final List<io.miragon.bpmrepo.core.diagram.domain.model.Diagram> list);

    DiagramUpdate mapUpdateToModel(final DiagramUpdateTO to);

}
