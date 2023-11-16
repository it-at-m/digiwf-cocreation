package de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity;

import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
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


    //0: OWNER - 1:ADMIN - 2:MEMBER - 3:VIEWER
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role_")
    private RoleEnum role;
}
