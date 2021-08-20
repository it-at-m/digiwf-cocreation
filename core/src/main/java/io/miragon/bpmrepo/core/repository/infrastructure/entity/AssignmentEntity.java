package io.miragon.bpmrepo.core.repository.infrastructure.entity;

import io.miragon.bpmrepo.core.shared.enums.RoleEnum;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Assignment_")
public class AssignmentEntity {

    @EmbeddedId
    private AssignmentId assignmentId;

    @Column(name = "user_name_")
    private String username;

    //0: OWNER - 1:ADMIN - 2:MEMBER - 3:VIEWER
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role_")
    private RoleEnum role;
}
