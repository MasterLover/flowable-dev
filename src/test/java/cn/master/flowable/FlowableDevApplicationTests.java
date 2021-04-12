package cn.master.flowable;

import java.io.InputStream;
import java.util.List;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.test.EnsureCleanDb;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.master.flowable.test.MyFlowableTestCase;
@EnsureCleanDb(dropDb = true)
class FlowableDevApplicationTests extends MyFlowableTestCase {

    @Test
    @Deployment(resources = "cn/master/flowable/event/TestMessageStart.bpmn20.xml")
    void contextLoads() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        list.forEach(processDefinition -> {
            System.out.println(processDefinition.toString());
        });
    }

    @Test
    @Deployment(resources = "cn/master/flowable/event/TestMessageStart.bpmn20.xml")
    public void StartWithMessage() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        list.forEach(processDefinition -> {
            System.out.println(processDefinition.toString());
        });
        ProcessInstance startProcess = runtimeService.startProcessInstanceByMessage("newInvoice");
        System.out.println(startProcess.toString());
    }

    @Test
    void name() throws Exception {
        BpmnModel bpmnModel = readJsonFile();
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
        repositoryService.createDeployment()
                .addBytes("test", bpmnBytes).disableBpmnValidation().disableSchemaValidation()
                .deploy();
    }

    protected BpmnModel readJsonFile() throws Exception {
        InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream("model.json");
        JsonNode modelNode = new ObjectMapper().readTree(jsonStream);
        return new BpmnJsonConverter().convertToBpmnModel(modelNode);
    }
}
