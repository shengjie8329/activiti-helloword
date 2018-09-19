package com.imooc.activiti.config;

import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * MDC测试
 * Created by Administrator on 2018/9/18 0018.
 */
public class ConfigMDCInteceptorTest {

    @Rule
    public ActivitiRule activitiRule2 = new ActivitiRule("activiti_mdc.cfg.xml");


    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test2() {
        //使用拦截器来打印MDC信息
        ProcessInstance processInstance = activitiRule2.getRuntimeService().startProcessInstanceByKey("my-process");
        assertNotNull(processInstance);

        Task task = activitiRule2.getTaskService().createTaskQuery().singleResult();
        assertEquals("Activiti is awesome!", task.getName());
        activitiRule2.getTaskService().complete(task.getId());

    }

}
