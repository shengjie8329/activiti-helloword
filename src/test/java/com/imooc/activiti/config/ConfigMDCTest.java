package com.imooc.activiti.config;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 * MDC测试
 * Created by Administrator on 2018/9/18 0018.
 */
public class ConfigMDCTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();


    @Test
    @Deployment(resources = {"my-process_mdcerror.bpmn20.xml"})
    public void test() {
        //开启MDC日志
        //默认正常情况是不会打印MDC日志的，只有在任务报错时才会打印
        LogMDC.setMDCEnabled(true);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        assertNotNull(processInstance);

//        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
//        assertEquals("Activiti is awesome!", task.getName());
//        activitiRule.getTaskService().complete(task.getId());

    }



}
