package io.miragon.bpmrepo.core.diagram.api.transport;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagramTO {

    @Nullable
    private String id;

    @NotNull
    private String repositoryId;

    @NotEmpty
    private String name;

    @NotNull
    private String description;

    @Nullable
    private LocalDateTime createdDate;

    @Nullable
    private LocalDateTime updatedDate;

    @Nullable
    private String svgPreview;

    @Nullable
    private String fileType;

    //
    //
    //    public BpmnDiagramTO(String bpmnRepositoryId, BpmnDiagramUploadTO bpmnDiagramUploadTO){
    //        this.bpmnDiagramId = bpmnDiagramUploadTO.getBpmnDiagramId();
    //        this.bpmnRepositoryId = bpmnRepositoryId;
    //        this.bpmnDiagramName = bpmnDiagramUploadTO.getBpmnDiagramName();
    //        this.bpmnDiagramDescription = bpmnDiagramUploadTO.getBpmnDiagramDescription();
    //        this.fileType = bpmnDiagramUploadTO.getFileType();
    //        this.svgPreview = (bpmnDiagramUploadTO.getSvgPreview() != null) ? bpmnDiagramUploadTO.getSvgPreview() : "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
    //                                                                                                                "<!-- created with bpmn-js / http://bpmn.io -->\n" +
    //                                                                                                                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" +
    //                                                                                                                "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"0\" height=\"0\" viewBox=\"0 0 0 0\" version=\"1.1\"></svg>";
    //    }

}
