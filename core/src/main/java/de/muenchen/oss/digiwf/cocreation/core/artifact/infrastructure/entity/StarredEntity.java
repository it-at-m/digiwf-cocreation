package de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Starred_")
public class StarredEntity {

    @EmbeddedId
    private StarredId id;
}
