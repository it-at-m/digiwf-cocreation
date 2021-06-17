package io.miragon.bpmnrepo.core.shared.mapper;

import io.miragon.bpmnrepo.core.repository.infrastructure.entity.AssignmentId;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    //___________________________TO -> Embeddable______________________________-

    public AssignmentId toEmbeddable(String userId, String bpmnRepositoryId) {
        if (userId == null || bpmnRepositoryId == null) {
            return null;
        }
        return AssignmentId.builder()
                .userId(userId)
                .bpmnRepositoryId(bpmnRepositoryId)
                .build();
    }
}
