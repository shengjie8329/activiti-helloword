package com.imooc.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试 Repository Service
 * Created by Administrator on 2018/9/21 0021.
 */
public class TaskServiceTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskService() {
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","my test message!!!");

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info(" task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        //这个就是获取userTask 中的documentation的内容，并将变量替换掉
        LOGGER.info(" task.description = {} ",task.getDescription());

        //设置task 的变量
        taskService.setVariable(task.getId(),"key1","value1");
        //设置task 的本地变量
        taskService.setVariableLocal(task.getId(),"localKey1","localValue1");

        Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
        Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());


        //也可以根据流程实例来获取流程变量
        Map<String, Object> processVariables1 = activitiRule.getRuntimeService().getVariables(task.getExecutionId());
        //这三种变量都是不相同的
        LOGGER.info("taskServiceVariables = {}",taskServiceVariables); // task的变量 包含 自己的 + Local + process
        /* taskServiceVariables = {key1=value1, localKey1=localValue1, message=my test message!!!}*/
        LOGGER.info("taskServiceVariablesLocal = {}",taskServiceVariablesLocal); //只包含Local的
        /* taskServiceVariablesLocal = {localKey1=localValu */
        LOGGER.info("processVariables1 = {}",processVariables1); //包含 task + process
        /* processVariables1 = {key1=value1, message=my test message!!!} */

        Map<String, Object> completeVar = Maps.newHashMap();
        completeVar.put("cKey1","cValue1");
        //最简单的完成userTask的方式，不校验当前的用户
        taskService.complete(task.getId(),completeVar);

        Task task1 = taskService.createTaskQuery()
                .taskId(task.getId())
                .singleResult();
        LOGGER.info("task1 = {}",task1);

    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceUser() {
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","my test message!!!");

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info(" task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        //这个就是获取userTask 中的documentation的内容，并将变量替换掉
        LOGGER.info(" task.description = {} ",task.getDescription());
        //指定发起人
        taskService.setOwner(task.getId(),"user1");
        //指定代办人,不推荐使用setAssignee来直接设置，后面如果再设置的话会覆盖
//        taskService.setAssignee(task.getId(),"jimmy");

        /**
         * 查找所有指定的办理人中包含jimmy，但是还没有被分配的任务
         */
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateUser("jimmy")
                .taskUnassigned()
                .listPage(0, 100);

        for (Task task1 : taskList) {
            try {
                taskService.claim(task.getId(),"jimmy");
            } catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
            }
        }

        //这个是查询与当前task相关的所有 IdentityLink
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : identityLinksForTask) {
            LOGGER.info("identityLink = {}",identityLink);
        }

        //查找所有指定jimmy为代办人的任务
        List<Task> jimmys = taskService.createTaskQuery()
                .taskAssignee("jimmy")
                .listPage(0, 100);
        for (Task jimmy : jimmys) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("ckey1","cvalue1");
            taskService.complete(task.getId(),vars);
        }

        jimmys = taskService.createTaskQuery()
                .taskAssignee("jimmy")
                .listPage(0, 100);
        LOGGER.info("是否存在待办任务 {}", !CollectionUtil.isEmpty(jimmys));
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceAttachment() {
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","my test message!!!");

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info(" task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        //这个就是获取userTask 中的documentation的内容，并将变量替换掉
        LOGGER.info(" task.description = {} ",task.getDescription());
        /**
         * 第一个参数： 附件类型
         * 第二个参数： taskId
         * 第三个参数： 流程实例Id
         * 第四个参数： 附件名称
         * 第五个参数： 描述
         * 第六个参数： 可以为一个url字符串或者是一个Inputstream
         */
        taskService.createAttachment("url",task.getId(),
                task.getProcessInstanceId(),"attachmentName",
                "description","url/test.png");
        /**
         * 获取某个task对应的所有附件
         */
        List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());
        for (Attachment taskAttachment : taskAttachments) {
            LOGGER.info("taskAttachment = {}",ToStringBuilder.reflectionToString(taskAttachment, ToStringStyle.JSON_STYLE));
        }

    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceComments() {
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","my test message!!!");

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info(" task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        //这个就是获取userTask 中的documentation的内容，并将变量替换掉
        LOGGER.info(" task.description = {} ",task.getDescription());
        //指定拥有人，这个也会记录在taskEvent中
        taskService.setOwner(task.getId(),"user1");
        taskService.setAssignee(task.getId(),"jimmy");
        /**
         * 第一个参数： taskId
         * 第二个参数： 流程实例Id
         * 第三个参数： 内容
         */
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"内容");
        /**
         * 获取某个task对应的所有comment
         */
        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for (Comment taskComment : taskComments) {
            LOGGER.info("taskCommnet = {}",ToStringBuilder.reflectionToString(taskComment, ToStringStyle.JSON_STYLE));
        }

        //查询事件记录
        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event taskEvent : taskEvents) {
            LOGGER.info("taskEvent = {}",ToStringBuilder.reflectionToString(taskEvent, ToStringStyle.JSON_STYLE));
        }
    }

}
