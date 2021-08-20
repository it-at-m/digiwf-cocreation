package io.miragon.bpmrepo.core.artifact.infrastructure.entity;

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
@Entity(name = "Version_Deployment_")
public class DeploymentEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "deployment_id_", unique = true, nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "target_")
    private String target;

    @Column(name = "user_")
    private String user;

    @Column(name = "timestamp_")
    private LocalDateTime timestamp;

}
