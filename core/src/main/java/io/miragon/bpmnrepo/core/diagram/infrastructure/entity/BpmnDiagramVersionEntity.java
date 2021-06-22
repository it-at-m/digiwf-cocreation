package io.miragon.bpmnrepo.core.diagram.infrastructure.entity;

import io.miragon.bpmnrepo.core.diagram.domain.enums.SaveTypeEnum;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bpmnDiagramVersion")
public class BpmnDiagramVersionEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "bpmn_diagram_version_id", unique = true, nullable = false, updatable = false, length = 36)
    private String bpmnDiagramVersionId;

    @Column(name = "bpmn_diagram_version_comment")
    private String bpmnDiagramVersionComment;

    @Column(name = "bpmn_diagram_version_release", nullable = false)
    private Integer bpmnDiagramVersionRelease;

    @Column(name = "bpmn_diagram_version_milestone", nullable = false)
    private Integer bpmnDiagramVersionMilestone;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "save_type", nullable = false)
    private SaveTypeEnum saveType;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "bpmn_diagram_version_file", columnDefinition = "TEXT")
    private String bpmnDiagramVersionFile;

    @Column(name = "bpmn_diagram_id", nullable = false)
    private String bpmnDiagramId;

    @Column(name = "bpmn_repository_id", nullable = false)
    private String bpmnRepositoryId;

}
