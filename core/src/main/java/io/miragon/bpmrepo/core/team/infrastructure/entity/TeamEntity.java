package io.miragon.bpmrepo.core.team.infrastructure.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Team_")
public class TeamEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "team_id_", unique = true, nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "name_")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "assigned_users_")
    private Integer assignedUsers;
}
