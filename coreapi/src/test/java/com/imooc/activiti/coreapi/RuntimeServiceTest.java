package com.imooc.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 测试 Repository Service
 * Created by Administrator on 2018/9/21 0021.
 */
public class RuntimeServiceTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(RuntimeServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcess() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("key1","value1");
        /**
         * 根据key来启动,这个key 是流程定义文件里 <process id="my-process"> 的这个id
         */
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = {}",processInstance);

    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcessById() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("key1","value1");
        /**
         * 根据processDefinitionId来启动
         */
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);
        LOGGER.info("processInstance = {}",processInstance);

    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcessByBuilder() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("key1","value1");
        /**
         * 根据 processInstanceBuilder 来启动流程,可以通过builder来一步步设置
         */
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = processInstanceBuilder.businessKey("businessKey001")
                .processDefinitionKey("my-process")
                .variables(variables)
                .start();
        LOGGER.info("processInstance = {}",processInstance);

    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testVariables() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("key1","value1");
        variables.put("key2","value2");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = {}",processInstance);
        /**
         * 获取当前流程实例的 variables
         */
        Map<String, Object> variables1 = runtimeService.getVariables(processInstance.getId());
        LOGGER.info("variables = {}",variables1);
        /**
         * 设置流程变量，第一个参数是流程实例id，第二个参数是 变量名, 第三个参数是变量值
         */
        runtimeService.setVariable(processInstance.getId(),"key2","value2_1");
        runtimeService.setVariable(processInstance.getId(),"key3","value3");

        variables1 = runtimeService.getVariables(processInstance.getId());
        LOGGER.info("variables = {}",variables1);

    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testProcessInstanceQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("key1","value1");
        variables.put("key2","value2");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = {}",processInstance);

        ProcessInstance processInstance1 = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        LOGGER.info("processInstance == processInstance1: {}",processInstance.getId().equals(processInstance1.getId()));
    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testExecutionQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("key1","value1");
        variables.put("key2","value2");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = {}",processInstance);

        /**
         * 根据流程执行对象来查询
         */
        List<Execution> executions = runtimeService.createExecutionQuery()
                .listPage(0, 100);
        //查出来两条，一条是流程实例，另一条是与实例相关的流程执行对象
        for (Execution execution : executions) {
            LOGGER.info("execution = {}",execution);
        }
/**
 * 17:58:48.820 [main] INFO  c.i.a.coreapi.RuntimeServiceTest - execution = ProcessInstance[4] ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.RuntimeServiceTest.testExecutionQuery:146
 17:58:48.820 [main] INFO  c.i.a.coreapi.RuntimeServiceTest - execution = Execution[ id '7' ] - activity 'someTask - parent '4' ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.RuntimeServiceTest.testExecutionQuery:146
 */
    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-trigger.bpmn20.xml"})
    public void testTrigger() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        Execution execution = runtimeService.createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        /* 10:18:34.756 [main] INFO  c.i.a.coreapi.RuntimeServiceTest - execution = Execution[ id '5' ] - activity 'someTask - parent '4' ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.RuntimeServiceTest.testTrigger:164*/
        LOGGER.info("execution = {}",execution);

        //推动该 receiveTask 执行
        runtimeService.trigger(execution.getId());

        execution = runtimeService.createExecutionQuery()
                .activityId("someTask")
                .singleResult();

        /* 此时流程已结束，没有可执行的 execution */
        LOGGER.info("execution = {}",execution);
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-signal-received.bpmn20.xml"})
    public void testSignalReceived() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        /**
         * 查询出定义的事件
         */
        Execution execution = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("my-signal")
                .singleResult();

        LOGGER.info("execution = {}",execution);
        //触发该事件
        runtimeService.signalEventReceived("my-signal");

        /**
         * 此时流程已经完成了，事件就查询不到了
         */
        execution = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        LOGGER.info("execution = {}",execution);

    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-message-received.bpmn20.xml"})
    public void testMessageReceived() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        /**
         * 查询出定义的事件
         */
        Execution execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("my-message")
                .singleResult();

        LOGGER.info("execution = {}",execution);
        //触发该事件,注意必须传入 executionId
        runtimeService.messageEventReceived("my-message",execution.getId());

        /**
         * 此时流程已经完成了，事件就查询不到了
         */
        execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("my-message")
                .singleResult();
        LOGGER.info("execution = {}",execution);

    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-message.bpmn20.xml"})
    public void testMessageStart() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByMessage("my-message");

        LOGGER.info("processInstance = {}",processInstance);

    }

}
