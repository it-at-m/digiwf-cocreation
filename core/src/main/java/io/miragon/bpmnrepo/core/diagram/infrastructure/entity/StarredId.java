package io.miragon.bpmnrepo.core.diagram.infrastructure.entity;

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
public class StarredId implements Serializable {

    @Column(name = "bpmn_diagram_id")
    private String bpmnDiagramId;

    @Column(name = "user_id")
    private String userId;

}
