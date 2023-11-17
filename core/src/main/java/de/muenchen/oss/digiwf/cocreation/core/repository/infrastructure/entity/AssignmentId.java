package de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity;

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
public class AssignmentId implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "repository_id")
    private String repositoryId;
}
