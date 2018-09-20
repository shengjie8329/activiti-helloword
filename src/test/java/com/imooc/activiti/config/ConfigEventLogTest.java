package com.imooc.activiti.config;

import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * MDC测试
 * Created by Administrator on 2018/9/18 0018.
 */
public class ConfigEventLogTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_eventlog.cfg.xml");

    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigEventLogTest.class);

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test() {

        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());

        //通过managementService获取 eventLog
        List<EventLogEntry> eventLogEntries = activitiRule.getManagementService()
                .getEventLogEntriesByProcessInstanceId(
                        processInstance.getProcessInstanceId());

        /** 打印出流程运行期间的eventlog
         13:50:30.582 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = PROCESSINSTANCE_START , eventLog.data = {"timeStamp":1537422630404,"processDefinitionId":"my-process:1:3","createTime":1537422630404,"id":"4"} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.582 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = ACTIVITY_STARTED , eventLog.data = {"timeStamp":1537422630411,"activityId":"start","processDefinitionId":"my-process:1:3","processInstanceId":"4","executionId":"5","behaviorClass":"org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior","activityType":"startEvent"} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.582 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = ACTIVITY_COMPLETED , eventLog.data = {"timeStamp":1537422630413,"activityId":"start","processDefinitionId":"my-process:1:3","processInstanceId":"4","executionId":"5","behaviorClass":"org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior","activityType":"startEvent"} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.582 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = SEQUENCEFLOW_TAKEN , eventLog.data = {"targetActivityId":"someTask","timeStamp":1537422630414,"targetActivityBehaviorClass":"org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior","sourceActivityType":"org.activiti.bpmn.model.StartEvent","targetActivityName":"Activiti is awesome!","id":"flow1","sourceActivityBehaviorClass":"org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior","targetActivityType":"org.activiti.bpmn.model.UserTask","sourceActivityId":"start"} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.582 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = ACTIVITY_STARTED , eventLog.data = {"timeStamp":1537422630415,"activityId":"someTask","processDefinitionId":"my-process:1:3","processInstanceId":"4","executionId":"5","behaviorClass":"org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior","activityName":"Activiti is awesome!","activityType":"userTask"} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.582 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = TASK_CREATED , eventLog.data = {"timeStamp":1537422630428,"taskDefinitionKey":"someTask","processDefinitionId":"my-process:1:3","processInstanceId":"4","executionId":"5","createTime":1537422630415,"name":"Activiti is awesome!","id":"8","priority":50} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.582 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = TASK_COMPLETED , eventLog.data = {"duration":130,"timeStamp":1537422630545,"taskDefinitionKey":"someTask","processDefinitionId":"my-process:1:3","processInstanceId":"4","executionId":"5","createTime":1537422630415,"name":"Activiti is awesome!","id":"8","priority":50} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.582 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = ACTIVITY_COMPLETED , eventLog.data = {"timeStamp":1537422630554,"activityId":"someTask","processDefinitionId":"my-process:1:3","processInstanceId":"4","executionId":"5","behaviorClass":"org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior","activityName":"Activiti is awesome!","activityType":"userTask"} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.583 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = SEQUENCEFLOW_TAKEN , eventLog.data = {"targetActivityId":"end","timeStamp":1537422630554,"sourceActivityName":"Activiti is awesome!","targetActivityBehaviorClass":"org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior","sourceActivityType":"org.activiti.bpmn.model.UserTask","id":"flow2","sourceActivityBehaviorClass":"org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior","targetActivityType":"org.activiti.bpmn.model.EndEvent","sourceActivityId":"someTask"} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.584 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = ACTIVITY_STARTED , eventLog.data = {"timeStamp":1537422630554,"activityId":"end","processDefinitionId":"my-process:1:3","processInstanceId":"4","executionId":"5","behaviorClass":"org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior","activityType":"endEvent"} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.584 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = ACTIVITY_COMPLETED , eventLog.data = {"timeStamp":1537422630555,"activityId":"end","processDefinitionId":"my-process:1:3","processInstanceId":"4","executionId":"5","behaviorClass":"org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior","activityType":"endEvent"} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.584 [main] INFO  c.i.a.config.ConfigEventLogTest -  eventLog.type = PROCESSINSTANCE_END , eventLog.data = {"timeStamp":1537422630562,"processDefinitionId":"my-process:1:3","id":"4","endTime":1537422630562} ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:42
         13:50:30.584 [main] INFO  c.i.a.config.ConfigEventLogTest - eventLogEntries.size = 12  ProcessDefinitionId= executionId= mdcProcessInstanceID= mdcBusinessKey= c.i.a.c.ConfigEventLogTest.test:44

         */
        for (EventLogEntry eventLogEntry : eventLogEntries) {
            LOGGER.info(" eventLog.type = {} , eventLog.data = {}",eventLogEntry.getType(), new String(eventLogEntry.getData()));
        }
        LOGGER.info("eventLogEntries.size = {} ",eventLogEntries.size());
    }



}
