package cn.master.flowable.event;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;

import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.eventsubscription.service.impl.EventSubscriptionQueryImpl;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.master.flowable.FlowableDevApplication;
import cn.master.flowable.test.MyFlowableTestCase;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/03/31 11:57
 */
public class FlowableEventTest extends MyFlowableTestCase {

    @Test
    @Deployment(resources = "cn/master/flowable/event/TestMessageStart.bpmn20.xml")
    public void StartWithMessage() {

        // using startProcessInstanceByMessage triggers the message start event

        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("newInvoiceMessage");

        assertThat(processInstance.isEnded()).isFalse();

        org.flowable.task.api.Task task = taskService.createTaskQuery().singleResult();
        assertThat(task).isNotNull();

        taskService.complete(task.getId());

        assertProcessEnded(processInstance.getId());

        // using startProcessInstanceByKey also triggers the message event, if there is a single start event

        processInstance = runtimeService.startProcessInstanceByKey("singleMessageStartEvent");

        assertThat(processInstance.isEnded()).isFalse();

        task = taskService.createTaskQuery().singleResult();
        assertThat(task).isNotNull();

        taskService.complete(task.getId());

        assertProcessEnded(processInstance.getId());

        // start process instance again after clearing the process definition cache (force deployment)

        processEngineConfiguration.getProcessDefinitionCache().clear();

        processInstance = runtimeService.startProcessInstanceByMessage("newInvoiceMessage");

        assertThat(processInstance.isEnded()).isFalse();

        task = taskService.createTaskQuery().singleResult();
        assertThat(task).isNotNull();

        taskService.complete(task.getId());

        assertProcessEnded(processInstance.getId());
    }

    @Test
    @Deployment
    public void EndWithMessage() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("EndWithMessage");
        assertThat(processInstance.isEnded()).isFalse();

        Task task = taskService.createTaskQuery().singleResult();

        assertThat(task).isNotNull();
        taskService.complete(task.getId());

        List<Execution> executions = runtimeService.createExecutionQuery().messageEventSubscriptionName("endMessage").list();

        assertThat(executions).isNotNull();
        assertThat(createEventSubscriptionQuery().count()).isEqualTo(1);
        assertThat(runtimeService.createExecutionQuery().count()).isEqualTo(2);

        runtimeService.messageEventReceived("endMessage", executions.get(0).getId());

        assertProcessEnded(processInstance.getId());
    }

    private EventSubscriptionQueryImpl createEventSubscriptionQuery() {
        return new EventSubscriptionQueryImpl(processEngineConfiguration.getCommandExecutor(),
                processEngineConfiguration.getEventSubscriptionServiceConfiguration());
    }
}
