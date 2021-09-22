package io.miragon.bpmrepo.core.sharing.infrastructure.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ShareWithRepositoryId implements Serializable {

    @Column(name = "artifact_id_", nullable = false)
    private String artifactId;

    @Column(name = "repository_id_")
    private String repositoryId;

}
