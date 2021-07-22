package io.miragon.bpmrepo.spring.boot.starter.artifact;

import io.miragon.bpmrepo.core.artifact.api.plugin.FileTypesPlugin;
import io.miragon.bpmrepo.core.artifact.api.transport.FileTypesTO;

import java.util.ArrayList;
import java.util.List;

public class DefaultFileTypesPlugin implements FileTypesPlugin {

    @Override
    public List<FileTypesTO> getFileTypes() {
        final List<FileTypesTO> fileTypes = new ArrayList<>();

        FileTypesTO type = new FileTypesTO("BPMN",
//#TODO: Icon für BPMN ist momentan im Frontend gespeichert und wird von dort über den "BpmnIcon"-String geladen -> hier her verschieben
                "BpmnIcon",

                "\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\n\" +\n" +
                        "    \"<bpmn:definitions xmlns:bpmn=\\\"http://www.omg.org/spec/BPMN/20100524/MODEL\\\" xmlns:bpmndi=\\\"http://www.omg.org/spec/BPMN/20100524/DI\\\" xmlns:dc=\\\"http://www.omg.org/spec/DD/20100524/DC\\\" xmlns:di=\\\"http://www.omg.org/spec/DD/20100524/DI\\\" id=\\\"Definitions_1r2owil\\\" targetNamespace=\\\"http://bpmn.io/schema/bpmn\\\" exporter=\\\"Camunda Modeler\\\" exporterVersion=\\\"4.4.0\\\">\\n\" +\n" +
                        "    \"  <bpmn:process id=\\\"Process_0lx73aq\\\" isExecutable=\\\"true\\\">\\n\" +\n" +
                        "    \"    <bpmn:startEvent id=\\\"StartEvent_1\\\">\\n\" +\n" +
                        "    \"      <bpmn:outgoing>Flow_0by8zp2</bpmn:outgoing>\\n\" +\n" +
                        "    \"    </bpmn:startEvent>\\n\" +\n" +
                        "    \"    <bpmn:task id=\\\"Activity_0bawd4n\\\">\\n\" +\n" +
                        "    \"      <bpmn:incoming>Flow_0by8zp2</bpmn:incoming>\\n\" +\n" +
                        "    \"      <bpmn:outgoing>Flow_0r4po2g</bpmn:outgoing>\\n\" +\n" +
                        "    \"    </bpmn:task>\\n\" +\n" +
                        "    \"    <bpmn:sequenceFlow id=\\\"Flow_0by8zp2\\\" sourceRef=\\\"StartEvent_1\\\" targetRef=\\\"Activity_0bawd4n\\\" />\\n\" +\n" +
                        "    \"    <bpmn:endEvent id=\\\"Event_0lxmzdr\\\">\\n\" +\n" +
                        "    \"      <bpmn:incoming>Flow_0r4po2g</bpmn:incoming>\\n\" +\n" +
                        "    \"    </bpmn:endEvent>\\n\" +\n" +
                        "    \"    <bpmn:sequenceFlow id=\\\"Flow_0r4po2g\\\" sourceRef=\\\"Activity_0bawd4n\\\" targetRef=\\\"Event_0lxmzdr\\\" />\\n\" +\n" +
                        "    \"  </bpmn:process>\\n\" +\n" +
                        "    \"  <bpmndi:BPMNArtifact id=\\\"BPMNArtifact_1\\\">\\n\" +\n" +
                        "    \"    <bpmndi:BPMNPlane id=\\\"BPMNPlane_1\\\" bpmnElement=\\\"Process_0lx73aq\\\">\\n\" +\n" +
                        "    \"      <bpmndi:BPMNEdge id=\\\"Flow_0by8zp2_di\\\" bpmnElement=\\\"Flow_0by8zp2\\\">\\n\" +\n" +
                        "    \"        <di:waypoint x=\\\"215\\\" y=\\\"117\\\" />\\n\" +\n" +
                        "    \"        <di:waypoint x=\\\"270\\\" y=\\\"117\\\" />\\n\" +\n" +
                        "    \"      </bpmndi:BPMNEdge>\\n\" +\n" +
                        "    \"      <bpmndi:BPMNEdge id=\\\"Flow_0r4po2g_di\\\" bpmnElement=\\\"Flow_0r4po2g\\\">\\n\" +\n" +
                        "    \"        <di:waypoint x=\\\"370\\\" y=\\\"117\\\" />\\n\" +\n" +
                        "    \"        <di:waypoint x=\\\"432\\\" y=\\\"117\\\" />\\n\" +\n" +
                        "    \"      </bpmndi:BPMNEdge>\\n\" +\n" +
                        "    \"      <bpmndi:BPMNShape id=\\\"_BPMNShape_StartEvent_2\\\" bpmnElement=\\\"StartEvent_1\\\">\\n\" +\n" +
                        "    \"        <dc:Bounds x=\\\"179\\\" y=\\\"99\\\" width=\\\"36\\\" height=\\\"36\\\" />\\n\" +\n" +
                        "    \"      </bpmndi:BPMNShape>\\n\" +\n" +
                        "    \"      <bpmndi:BPMNShape id=\\\"Activity_0bawd4n_di\\\" bpmnElement=\\\"Activity_0bawd4n\\\">\\n\" +\n" +
                        "    \"        <dc:Bounds x=\\\"270\\\" y=\\\"77\\\" width=\\\"100\\\" height=\\\"80\\\" />\\n\" +\n" +
                        "    \"      </bpmndi:BPMNShape>\\n\" +\n" +
                        "    \"      <bpmndi:BPMNShape id=\\\"Event_0lxmzdr_di\\\" bpmnElement=\\\"Event_0lxmzdr\\\">\\n\" +\n" +
                        "    \"        <dc:Bounds x=\\\"432\\\" y=\\\"99\\\" width=\\\"36\\\" height=\\\"36\\\" />\\n\" +\n" +
                        "    \"      </bpmndi:BPMNShape>\\n\" +\n" +
                        "    \"    </bpmndi:BPMNPlane>\\n\" +\n" +
                        "    \"  </bpmndi:BPMNArtifact>\\n\" +\n" +
                        "    \"</bpmn:definitions>\\n\"",

                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<!-- created with bpmn-js / http://bpmn.io -->\n" +
                        "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" +
                        "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"287\" height=\"92\" viewBox=\"167 74 287 92\" version=\"1.1\"><defs><marker id=\"sequenceflow-end-white-black-egrhm21ehmhmllnvjlm26ue3z\" viewBox=\"0 0 20 20\" refX=\"11\" refY=\"10\" markerWidth=\"10\" markerHeight=\"10\" orient=\"auto\"><path d=\"M 1 5 L 11 10 L 1 15 Z\" style=\"fill: black; stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1; stroke: black;\"/></marker></defs><g class=\"djs-group\"><g class=\"djs-element djs-connection\" data-element-id=\"Flow_1btmw3a\" style=\"display: block;\"><g class=\"djs-visual\"><path d=\"m  209,120L260,120 \" style=\"fill: none; stroke-width: 2px; stroke: black; stroke-linejoin: round; marker-end: url('#sequenceflow-end-white-black-egrhm21ehmhmllnvjlm26ue3z');\"/></g><polyline points=\"209,120 260,120 \" class=\"djs-hit djs-hit-stroke\" style=\"fill: none; stroke-opacity: 0; stroke: white; stroke-width: 15px;\"/><rect x=\"203\" y=\"114\" width=\"63\" height=\"12\" class=\"djs-outline\" style=\"fill: none;\"/></g></g><g class=\"djs-group\"><g class=\"djs-element djs-connection\" data-element-id=\"Flow_0m4j7xp\" style=\"display: block;\"><g class=\"djs-visual\"><path d=\"m  360,120L412,120 \" style=\"fill: none; stroke-width: 2px; stroke: black; stroke-linejoin: round; marker-end: url('#sequenceflow-end-white-black-egrhm21ehmhmllnvjlm26ue3z');\"/></g><polyline points=\"360,120 412,120 \" class=\"djs-hit djs-hit-stroke\" style=\"fill: none; stroke-opacity: 0; stroke: white; stroke-width: 15px;\"/><rect x=\"354\" y=\"114\" width=\"64\" height=\"12\" class=\"djs-outline\" style=\"fill: none;\"/></g></g><g class=\"djs-group\"><g class=\"djs-element djs-shape\" data-element-id=\"StartEvent_1\" style=\"display: block;\" transform=\"matrix(1 0 0 1 173 102)\"><g class=\"djs-visual\"><circle cx=\"18\" cy=\"18\" r=\"18\" style=\"stroke: black; stroke-width: 2px; fill: white; fill-opacity: 0.95;\"/></g><rect class=\"djs-hit djs-hit-all\" x=\"0\" y=\"0\" width=\"36\" height=\"36\" style=\"fill: none; stroke-opacity: 0; stroke: white; stroke-width: 15px;\"/><rect x=\"-6\" y=\"-6\" width=\"48\" height=\"48\" class=\"djs-outline\" style=\"fill: none;\"/></g></g><g class=\"djs-group\"><g class=\"djs-element djs-shape\" data-element-id=\"Activity_0lwu3p2\" style=\"display: block;\" transform=\"matrix(1 0 0 1 260 80)\"><g class=\"djs-visual\"><rect x=\"0\" y=\"0\" width=\"100\" height=\"80\" rx=\"10\" ry=\"10\" style=\"stroke: black; stroke-width: 2px; fill: white; fill-opacity: 0.95;\"/><text lineHeight=\"1.2\" class=\"djs-label\" style=\"font-family: Arial, sans-serif; font-size: 12px; font-weight: normal; fill: black;\"><tspan x=\"50\" y=\"43.599999999999994\"/></text></g><rect class=\"djs-hit djs-hit-all\" x=\"0\" y=\"0\" width=\"100\" height=\"80\" style=\"fill: none; stroke-opacity: 0; stroke: white; stroke-width: 15px;\"/><rect x=\"-6\" y=\"-6\" width=\"112\" height=\"92\" class=\"djs-outline\" style=\"fill: none;\"/></g></g><g class=\"djs-group\"><g class=\"djs-element djs-shape selected\" data-element-id=\"Event_0pf42bb\" style=\"display: block;\" transform=\"matrix(1 0 0 1 412 102)\"><g class=\"djs-visual\"><circle cx=\"18\" cy=\"18\" r=\"18\" style=\"stroke: black; stroke-width: 4px; fill: white; fill-opacity: 0.95;\"/></g><rect class=\"djs-hit djs-hit-all\" x=\"0\" y=\"0\" width=\"36\" height=\"36\" style=\"fill: none; stroke-opacity: 0; stroke: white; stroke-width: 15px;\"/><rect x=\"-6\" y=\"-6\" width=\"48\" height=\"48\" class=\"djs-outline\" style=\"fill: none;\"/></g></g></svg>");
        fileTypes.add(type);

        type = new FileTypesTO("DMN",
                "M 10 10.02 h 5 V 21 h -5 Z M 17 21 h 3 c 1.1 0 2 -0.9 2 -2 v -9 h -5 v 11 Z m 3 -18 H 5 c -1.1 0 -2 0.9 -2 2 v 3 h 19 V 5 c 0 -1.1 -0.9 -2 -2 -2 Z M 3 19 c 0 1.1 0.9 2 2 2 h 3 V 10 H 3 v 9 Z",

                "\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\n\"\n" +
                        "    + \"<definitions xmlns=\\\"https://www.omg.org/spec/DMN/20191111/MODEL/\\\" xmlns:dmndi=\\\"https://www.omg.org/spec/DMN/20191111/DMNDI/\\\" xmlns:dc=\\\"http://www.omg.org/spec/DMN/20180521/DC/\\\" id=\\\"Definitions_0rtbinw\\\" name=\\\"DRD\\\" namespace=\\\"http://camunda.org/schema/1.0/dmn\\\">\\n\"\n" +
                        "    + \"  <decision id=\\\"Decision_07746u8\\\" name=\\\"Decision 1\\\">\\n\"\n" +
                        "    + \"    <decisionTable id=\\\"DecisionTable_170t96n\\\">\\n\"\n" +
                        "    + \"      <input id=\\\"Input_1\\\">\\n\"\n" +
                        "    + \"        <inputExpression id=\\\"InputExpression_1\\\" typeRef=\\\"string\\\">\\n\"\n" +
                        "    + \"          <text></text>\\n\"\n" +
                        "    + \"        </inputExpression>\\n\"\n" +
                        "    + \"      </input>\\n\"\n" +
                        "    + \"      <output id=\\\"Output_1\\\" typeRef=\\\"string\\\" />\\n\"\n" +
                        "    + \"    </decisionTable>\\n\"\n" +
                        "    + \"  </decision>\\n\"\n" +
                        "    + \"  <dmndi:DMNDI>\\n\"\n" +
                        "    + \"    <dmndi:DMNArtifact>\\n\"\n" +
                        "    + \"      <dmndi:DMNShape dmnElementRef=\\\"Decision_07746u8\\\">\\n\"\n" +
                        "    + \"        <dc:Bounds height=\\\"80\\\" width=\\\"180\\\" x=\\\"100\\\" y=\\\"100\\\" />\\n\"\n" +
                        "    + \"      </dmndi:DMNShape>\\n\"\n" +
                        "    + \"    </dmndi:DMNArtifact>\\n\"\n" +
                        "    + \"  </dmndi:DMNDI>\\n\"\n" +
                        "    + \"</definitions>\\n\"",

                "<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?>\\n\" +\n" +
                        "    \"<!-- created with dmn-js / http://bpmn.io -->\\n\" +\n" +
                        "    \"<!DOCTYPE svg PUBLIC \\\"-//W3C//DTD SVG 1.1//EN\\\" \\\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\\\">\\n\" +\n" +
                        "    \"<svg xmlns=\\\"http://www.w3.org/2000/svg\\\" xmlns:xlink=\\\"http://www.w3.org/1999/xlink\\\" width=\\\"192\\\" height=\\\"92\\\" viewBox=\\\"154 94 192 92\\\" version=\\\"1.1\\\"><defs><marker id=\\\"association-start\\\" viewBox=\\\"0 0 20 20\\\" refX=\\\"1\\\" refY=\\\"10\\\" markerWidth=\\\"10\\\" markerHeight=\\\"10\\\" orient=\\\"auto\\\"><path d=\\\"M 11 5 L 1 10 L 11 15\\\" style=\\\"stroke-width: 1.5px; stroke-linecap: round; stroke-dasharray: 10000, 1; fill: none; stroke: black;\\\"></path></marker><marker id=\\\"association-end\\\" viewBox=\\\"0 0 20 20\\\" refX=\\\"12\\\" refY=\\\"10\\\" markerWidth=\\\"10\\\" markerHeight=\\\"10\\\" orient=\\\"auto\\\"><path d=\\\"M 1 5 L 11 10 L 1 15\\\" style=\\\"stroke-width: 1.5px; stroke-linecap: round; stroke-dasharray: 10000, 1; fill: none; stroke: black;\\\"></path></marker><marker id=\\\"information-requirement-end\\\" viewBox=\\\"0 0 20 20\\\" refX=\\\"11\\\" refY=\\\"10\\\" markerWidth=\\\"20\\\" markerHeight=\\\"20\\\" orient=\\\"auto\\\"><path d=\\\"M 1 5 L 11 10 L 1 15 Z\\\" style=\\\"stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1;\\\"></path></marker><marker id=\\\"knowledge-requirement-end\\\" viewBox=\\\"0 0 20 20\\\" refX=\\\"11\\\" refY=\\\"10\\\" markerWidth=\\\"16\\\" markerHeight=\\\"16\\\" orient=\\\"auto\\\"><path d=\\\"M 1 3 L 11 10 L 1 17\\\" style=\\\"stroke-width: 2px; stroke-linecap: round; stroke-dasharray: 10000, 1; fill: none; stroke: black;\\\"></path></marker><marker id=\\\"authority-requirement-end\\\" viewBox=\\\"0 0 20 20\\\" refX=\\\"3\\\" refY=\\\"3\\\" markerWidth=\\\"18\\\" markerHeight=\\\"18\\\" orient=\\\"auto\\\"><circle cx=\\\"3\\\" cy=\\\"3\\\" r=\\\"3\\\" style=\\\"stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1;\\\"></circle></marker></defs><g class=\\\"djs-group\\\"><g class=\\\"djs-element djs-shape\\\" data-element-id=\\\"Decision_1x87kk0\\\" style=\\\"display: block;\\\" transform=\\\"matrix(1 0 0 1 160 100)\\\"><g class=\\\"djs-visual\\\"><rect x=\\\"0\\\" y=\\\"0\\\" width=\\\"180\\\" height=\\\"80\\\" rx=\\\"0\\\" ry=\\\"0\\\" style=\\\"stroke: black; stroke-width: 2px; fill: white;\\\"/><text lineHeight=\\\"1.2\\\" class=\\\"djs-label\\\" style=\\\"font-family: Arial, sans-serif; font-size: 12px; font-weight: normal;\\\"><tspan x=\\\"61.984375\\\" y=\\\"43.599999999999994\\\">Decision 1</tspan></text></g><rect class=\\\"djs-hit djs-hit-all\\\" x=\\\"0\\\" y=\\\"0\\\" width=\\\"180\\\" height=\\\"80\\\" style=\\\"fill: none; stroke-opacity: 0; stroke: white; stroke-width: 15px;\\\"/><rect x=\\\"-6\\\" y=\\\"-6\\\" width=\\\"192\\\" height=\\\"92\\\" class=\\\"djs-outline\\\" style=\\\"fill: none;\\\"/></g></g></svg>\"");
        fileTypes.add(type);

        type = new FileTypesTO("FORM",

                "M 4 14 h 4 v -4 H 4 v 4 Z m 0 5 h 4 v -4 H 4 v 4 Z M 4 9 h 4 V 5 H 4 v 4 Z m 5 5 h 12 v -4 H 9 v 4 Z m 0 5 h 12 v -4 H 9 v 4 Z M 9 5 v 4 h 12 V 5 H 9 Z", "file default - has to be entered",

                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<!-- Generator: Adobe Illustrator 19.2.1, SVG Export Plug-In . SVG Version: 6.00 Build 0)  -->\n" +
                        "<svg version=\"1.0\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\"\n" +
                        "\t viewBox=\"0 0 16 16\" style=\"enable-background:new 0 0 16 16;\" xml:space=\"preserve\">\n" +
                        "<style type=\"text/css\">\n" +
                        "\t.st0{fill:#414042;}\n" +
                        "\t.st1{fill:none;stroke:#404040;stroke-width:2;stroke-miterlimit:10;}\n" +
                        "\t.st2{fill:none;}\n" +
                        "</style>\n" +
                        "<g>\n" +
                        "\t<path class=\"st0\" d=\"M14.2,1.8v12.4H1.8V1.8H14.2 M15.2,0.8H0.8v14.4h14.4V0.8L15.2,0.8z\"/>\n" +
                        "</g>\n" +
                        "<line class=\"st1\" x1=\"3.2\" y1=\"4.3\" x2=\"12.8\" y2=\"4.3\"/>\n" +
                        "<line class=\"st1\" x1=\"3.2\" y1=\"7.8\" x2=\"12.8\" y2=\"7.8\"/>\n" +
                        "<line class=\"st1\" x1=\"3.2\" y1=\"11.6\" x2=\"12.8\" y2=\"11.6\"/>\n" +
                        "<rect class=\"st2\" width=\"15.8\" height=\"16\"/>\n" +
                        "</svg>");

        fileTypes.add(type);

        type = new FileTypesTO("CONFIGURATION", "M 9.4 16.6 L 4.8 12 l 4.6 -4.6 L 8 6 l -6 6 l 6 6 l 1.4 -1.4 Z m 5.2 0 l 4.6 -4.6 l -4.6 -4.6 L 16 6 l 6 6 l -6 6 l -1.4 -1.4 Z", "file default for Config", "<svg xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:cc=\"http://creativecommons.org/ns#\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:sodipodi=\"http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd\" xmlns:inkscape=\"http://www.inkscape.org/namespaces/inkscape\" version=\"1.1\" viewBox=\"0 0 106.254 127.08375000000001\" xml:space=\"preserve\" x=\"0px\" y=\"0px\"><g transform=\"matrix(-0.82213163,0,0,0.82213163,104.10836,9.5054284)\"><g transform=\"matrix(-0.83502688,0,0,0.83502688,168.1125,-23.806487)\"><g><path d=\"m 95.745929,62.69857 -0.01685,-12.53955 -41.90426,33.033268 c 0.06,3.9 -0.06001,8.394444 -1.5e-5,12.294444 L 95.729075,128.52 95.695368,115.86639 61.301123,89.394063 z\"/></g></g><g transform=\"matrix(0.83502688,0,0,0.83502688,-44.946342,-23.806486)\"><g><path d=\"m 95.745929,62.69857 -0.01685,-12.53955 -41.90426,33.033268 c 0.06,3.9 -0.06001,8.394444 -1.5e-5,12.294444 L 95.729075,128.52 95.695368,115.86639 61.301123,89.394063 z\"/></g></g></g><g transform=\"matrix(-0.86345181,0,0,0.86345181,105.35785,6.2992609)\"><path d=\"m 47.090198,18.003341 -9.116402,0 35.24437,65.812426 10.832403,0\"/></g></svg>");
        fileTypes.add(type);

        return fileTypes;
    }
}
