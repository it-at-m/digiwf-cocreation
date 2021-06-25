package io.miragon.bpmrepo.core.repository.infrastructure.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bpmnRepository")
public class BpmnRepositoryEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "bpmn_repository_id", unique = true, nullable = false, updatable = false, length = 36)
    private String bpmnRepositoryId;

    @Column(name = "bpmn_repository_name")
    private String bpmnRepositoryName;

    @Column(name = "bpmn_repository_description")
    private String bpmnRepositoryDescription;

    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @Column(name = "existing_diagrams", columnDefinition = "integer default 0")
    private Integer existingDiagrams;

    @Column(name = "assigned_users", columnDefinition = "integer default 1")
    private Integer assignedUsers;

}
