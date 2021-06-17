package io.miragon.bpmnrepo.core.repository.infrastructure.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AssignmentId implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "bpmn_repository_id")
    private String bpmnRepositoryId;
}
