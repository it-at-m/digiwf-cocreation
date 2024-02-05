package de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
