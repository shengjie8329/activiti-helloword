package com.imooc.activiti.config;

import com.imooc.activiti.helloworld.event.CustomEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * MDC测试
 * Created by Administrator on 2018/9/18 0018.
 */
public class ConfigEventListenerTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_eventListener.cfg.xml");


    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test() {

        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());
        //动态注册一个监听器,另外这个方法还可以再后面带上ActivitiEventType参数(数组)来指定监听哪些事件
        activitiRule.getRuntimeService().addEventListener(new CustomEventListener(),ActivitiEventType.CUSTOM);
        //触发一个自定义的事件
        activitiRule.getRuntimeService().dispatchEvent(new ActivitiEventImpl(ActivitiEventType.CUSTOM));
    }



}
