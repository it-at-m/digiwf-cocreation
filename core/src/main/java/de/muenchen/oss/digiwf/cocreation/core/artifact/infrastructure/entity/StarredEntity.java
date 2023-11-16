package de.muenchen.oss.digiwf.cocreation.core.artifact.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Starred_")
public class StarredEntity {

    @EmbeddedId
    private StarredId id;
}
