package io.miragon.bpmrepo.core.diagram.domain.model;

import io.miragon.bpmrepo.core.diagram.domain.enums.SaveTypeEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiagramVersion {
    private String id;
    private String comment;
    private String diagramId;
    private String repositoryId;
    private Integer release;
    private Integer milestone;
    private String xml;
    private SaveTypeEnum saveType;
    private LocalDateTime updatedDate;

    public void updateVersion(final DiagramVersion diagramVersion) {
        if (StringUtils.isNotBlank((diagramVersion.getComment()))) {
            this.comment = diagramVersion.getComment();
        }

        this.release = this.generateReleaseNumber(diagramVersion);
        this.milestone = this.generateMilestoneNumber(diagramVersion);
        this.xml = diagramVersion.getXml();
        this.updatedDate = LocalDateTime.now();
    }

    public void initXml() {
        this.xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" id=\"Definitions_0cumbkd\" targetNamespace=\"http://bpmn.io/schema/bpmn\" exporter=\"Camunda Modeler\" exporterVersion=\"4.4.0\">\n"
                + "  <bpmn:process id=\"Process_0fycv87\" isExecutable=\"true\">\n"
                + "    <bpmn:startEvent id=\"StartEvent_1\">\n"
                + "      <bpmn:outgoing>Flow_0cdqhp0</bpmn:outgoing>\n"
                + "    </bpmn:startEvent>\n"
                + "    <bpmn:task id=\"Activity_153gm57\">\n"
                + "      <bpmn:incoming>Flow_0cdqhp0</bpmn:incoming>\n"
                + "      <bpmn:outgoing>Flow_1kcocqi</bpmn:outgoing>\n"
                + "    </bpmn:task>\n"
                + "    <bpmn:sequenceFlow id=\"Flow_0cdqhp0\" sourceRef=\"StartEvent_1\" targetRef=\"Activity_153gm57\" />\n"
                + "    <bpmn:endEvent id=\"Event_0l0dpse\">\n"
                + "      <bpmn:incoming>Flow_1kcocqi</bpmn:incoming>\n"
                + "    </bpmn:endEvent>\n"
                + "    <bpmn:sequenceFlow id=\"Flow_1kcocqi\" sourceRef=\"Activity_153gm57\" targetRef=\"Event_0l0dpse\" />\n"
                + "  </bpmn:process>\n"
                + "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n"
                + "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Process_0fycv87\">\n"
                + "      <bpmndi:BPMNEdge id=\"Flow_0cdqhp0_di\" bpmnElement=\"Flow_0cdqhp0\">\n"
                + "        <di:waypoint x=\"215\" y=\"117\" />\n"
                + "        <di:waypoint x=\"270\" y=\"117\" />\n"
                + "      </bpmndi:BPMNEdge>\n"
                + "      <bpmndi:BPMNEdge id=\"Flow_1kcocqi_di\" bpmnElement=\"Flow_1kcocqi\">\n"
                + "        <di:waypoint x=\"370\" y=\"117\" />\n"
                + "        <di:waypoint x=\"432\" y=\"117\" />\n"
                + "      </bpmndi:BPMNEdge>\n"
                + "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n"
                + "        <dc:Bounds x=\"179\" y=\"99\" width=\"36\" height=\"36\" />\n"
                + "      </bpmndi:BPMNShape>\n"
                + "      <bpmndi:BPMNShape id=\"Activity_153gm57_di\" bpmnElement=\"Activity_153gm57\">\n"
                + "        <dc:Bounds x=\"270\" y=\"77\" width=\"100\" height=\"80\" />\n"
                + "      </bpmndi:BPMNShape>\n"
                + "      <bpmndi:BPMNShape id=\"Event_0l0dpse_di\" bpmnElement=\"Event_0l0dpse\">\n"
                + "        <dc:Bounds x=\"432\" y=\"99\" width=\"36\" height=\"36\" />\n"
                + "      </bpmndi:BPMNShape>\n"
                + "    </bpmndi:BPMNPlane>\n"
                + "  </bpmndi:BPMNDiagram>\n"
                + "</bpmn:definitions>\n";
    }

    public void updateRelease(final Integer release) {
        this.release = release;
    }

    public void updateMilestone(final Integer milestone) {
        this.milestone = milestone;
    }

    public Integer generateReleaseNumber(final DiagramVersion diagramVersion) {
        log.warn("Generating Release Number");
        if (diagramVersion.getSaveType() != null) {
            if (diagramVersion.getSaveType().equals(SaveTypeEnum.RELEASE)) {
                if (diagramVersion.getRelease() != null) {
                    return diagramVersion.getRelease() + 1;
                } else {
                    return 1;
                }
            } else {
                if (diagramVersion.getRelease() != null) {
                    return diagramVersion.getRelease();
                } else {
                    return 1;
                }
            }
        } else {
            return 1;
        }
    }

    public Integer generateMilestoneNumber(final DiagramVersion diagramVersion) {
        if (diagramVersion.getSaveType() != null) {
            if (diagramVersion.getSaveType().equals(SaveTypeEnum.AUTOSAVE)) {
                if (diagramVersion.getMilestone() != null) {
                    return diagramVersion.getMilestone();
                } else {
                    return 0;
                }
            }
            if (diagramVersion.getSaveType().equals(SaveTypeEnum.MILESTONE)) {
                if (diagramVersion.getMilestone() != null) {
                    return diagramVersion.getMilestone() + 1;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
        return 0;
    }

}
