package de.muenchen.oss.digiwf.cocreation.core.sharing.infrastructure.entity;

import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Shared_Repository")
public class ShareWithRepositoryEntity {

    @EmbeddedId
    private ShareWithRepositoryId shareWithRepositoryId;

    //0: OWNER - 1:ADMIN - 2:MEMBER - 3:VIEWER
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role_", nullable = false)
    private RoleEnum role;
}
