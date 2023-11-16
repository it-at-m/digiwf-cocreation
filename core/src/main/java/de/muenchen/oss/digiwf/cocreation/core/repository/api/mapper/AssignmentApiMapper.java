package de.muenchen.oss.digiwf.cocreation.core.repository.api.mapper;

import de.muenchen.oss.digiwf.cocreation.core.repository.domain.model.Assignment;
import de.muenchen.oss.digiwf.cocreation.core.repository.api.transport.AssignmentTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AssignmentApiMapper {

    AssignmentTO mapToTO(final Assignment assignment);

    Assignment mapToModel(final AssignmentTO assignmentTO);

    List<AssignmentTO> mapToTO(final List<Assignment> list);


}
