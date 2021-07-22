package io.miragon.bpmrepo.core.artifact.infrastructure.entity;

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

    @Column(name = "artifact_id")
    private String artifactId;

    @Column(name = "user_id")
    private String userId;

}
