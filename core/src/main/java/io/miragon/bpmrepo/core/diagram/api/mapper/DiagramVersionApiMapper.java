package io.miragon.bpmrepo.core.diagram.api.mapper;

import io.miragon.bpmrepo.core.diagram.api.transport.DiagramVersionTO;
import io.miragon.bpmrepo.core.diagram.api.transport.DiagramVersionUploadTO;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramVersion;
import io.miragon.bpmrepo.core.diagram.domain.model.DiagramVersionUpload;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DiagramVersionApiMapper {

    DiagramVersionUpload mapUploadToModel(final DiagramVersionUploadTO to);

    DiagramVersionTO mapToTO(final DiagramVersion model);

    List<DiagramVersionTO> mapToTO(final List<DiagramVersion> list);

}
