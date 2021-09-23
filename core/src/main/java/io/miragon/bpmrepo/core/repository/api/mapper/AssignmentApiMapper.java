package io.miragon.bpmrepo.core.repository.api.mapper;

import io.miragon.bpmrepo.core.repository.api.transport.AssignmentTO;
import io.miragon.bpmrepo.core.repository.domain.model.Assignment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AssignmentApiMapper {

    AssignmentTO mapToTO(final Assignment assignment);

    Assignment mapToModel(final AssignmentTO assignmentTO);

    List<AssignmentTO> mapToTO(final List<Assignment> list);


}
