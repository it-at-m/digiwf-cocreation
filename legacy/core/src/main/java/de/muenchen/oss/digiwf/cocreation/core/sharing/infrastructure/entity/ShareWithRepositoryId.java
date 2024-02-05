package de.muenchen.oss.digiwf.cocreation.core.sharing.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

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
