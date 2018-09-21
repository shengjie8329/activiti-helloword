package com.imooc.activiti.config;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
 * MDC测试
 * Created by Administrator on 2018/9/18 0018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class ConfigSpringTest {

    @Rule
    @Autowired
    public ActivitiRule activitiRule;

    @Autowired
    public RuntimeService runtimeService;

    @Autowired
    public TaskService taskService;

    @Test
    @Deployment(resources = {"my-process_springel.bpmn20.xml"})
    public void test() {
        //开启MDC日志
        //默认正常情况是不会打印MDC日志的，只有在任务报错时才会打印
        LogMDC.setMDCEnabled(true);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        Task task = taskService.createTaskQuery().singleResult();
        taskService.complete(task.getId());

    }



}
