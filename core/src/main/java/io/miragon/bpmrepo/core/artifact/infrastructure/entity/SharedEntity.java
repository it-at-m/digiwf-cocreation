package io.miragon.bpmrepo.core.artifact.infrastructure.entity;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "shared")
public class SharedEntity {

    @EmbeddedId
    private SharedId sharedId;

    //0: OWNER - 1:ADMIN - 2:MEMBER - 3:VIEWER
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleEnum role;
}
