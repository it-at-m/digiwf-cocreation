package io.miragon.bpmnrepo.core.diagram.infrastructure.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bpmnDiagram")
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"bpmn_diagram_id", "bpmn_repository_id"}))
public class BpmnDiagramEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "bpmn_diagram_id", unique = true, nullable = false, updatable = false, length = 36)
    private String bpmnDiagramId;

    @Column(name = "bpmn_repository_id")
    private String bpmnRepositoryId;

    @Column(name = "bpmn_diagram_name")
    private String bpmnDiagramName;

    @Column(name = "bpmd_diagram_description")
    private String bpmnDiagramDescription;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "svg_preview", columnDefinition = "TEXT")
    private String svgPreview;

    @Column(name = "file_type")
    private String fileType;

}
