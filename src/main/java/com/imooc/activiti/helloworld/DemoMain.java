package com.imooc.activiti.helloworld;

import com.google.common.collect.Maps;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Administrator on 2018/9/14 0014.
 */
public class DemoMain {

    public static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);

    /**
     * 导入流程定义
     * 启动类
     *
     * @param args
     */
    public static void main(String[] args) throws ParseException {

        LOGGER.info("启动我们的程序");
        //创建流程引擎
        //创建一个基于内存h2数据库的默认配置
        ProcessEngine processEngine = getProcessEngine();

        //部署流程定义文件
        ProcessDefinition processDefinition = getProcessDefinition(processEngine);

        //启动运行流程
        ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);

        //处理流程任务
        processTask(processEngine, processInstance);

        LOGGER.info("结束我们的程序");

    }

    private static void processTask(ProcessEngine processEngine, ProcessInstance processInstance) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        while( processInstance !=  null && !processInstance.isEnded())
        {
            TaskService taskService = processEngine.getTaskService();
            FormService formService = processEngine.getFormService();
            List<Task> list = taskService.createTaskQuery().list();
            LOGGER.info("待处理任务数量 {}",list.size());
            for (Task task:list)
            {
                LOGGER.info("待处理任务 [{}]", task.getName());
                Map<String, Object> variables = getMap(scanner, formService, task);
                taskService.complete(task.getId(),variables);
                //同步流程最新的任务实例
                processInstance = processEngine.getRuntimeService()
                        .createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .singleResult();
            }
        }
        scanner.close();
    }

    private static Map<String, Object> getMap(Scanner scanner, FormService formService, Task task) throws ParseException {
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        Map<String,Object> variables = Maps.newHashMap();
        for (FormProperty property : formProperties)
        {
            String line = null;
            if(StringFormType.class.isInstance(property.getType()))
            {
                LOGGER.info("请输入 {} ?",property.getName());
                line = scanner.nextLine();
                variables.put(property.getId(),line);
                LOGGER.info("您输入的内容是 [{}]",line);
            }
            else if(DateFormType.class.isInstance(property.getType()))
            {
                LOGGER.info("请输入 {} ? 格式 (yyyy-MM-dd)",property.getName());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                line = scanner.nextLine();
                Date date = simpleDateFormat.parse(line);
                variables.put(property.getId(),date);
                LOGGER.info("您输入的内容是 [{}]",line);
            }
            else
            {
                LOGGER.info("类型暂不支持 {}",property.getType());
            }
        }
        return variables;
    }

    private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());

        LOGGER.info("启动流程 [{}]", processInstance.getProcessDefinitionKey());//启动流程second_approve
        return processInstance;
    }

    private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();

        String deploymentId = deploy.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();
        //流程定义文件二级审批流程,流程IDsecond_approve:1:4 (根据部署id 和 流程id 组装出来的对象)
        LOGGER.info("流程定义文件 [{}],流程ID [{}]",processDefinition.getName(),processDefinition.getId());
        return processDefinition;
    }

    private static ProcessEngine getProcessEngine() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();

        ProcessEngine processEngine = cfg.buildProcessEngine();
        String name = processEngine.getName();
        String version = processEngine.VERSION;

        LOGGER.info("流程引擎名称{},版本号{}",name,version);
        return processEngine;
    }
}
