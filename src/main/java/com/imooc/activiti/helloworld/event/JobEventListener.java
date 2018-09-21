package com.imooc.activiti.helloworld.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流程event
 * Created by Administrator on 2018/9/20 0020.
 */
public class JobEventListener implements ActivitiEventListener{

    public static final Logger LOGGER = LoggerFactory.getLogger(JobEventListener.class);

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {

        ActivitiEventType eventType = activitiEvent.getType();
        String name = eventType.name();
        if(name.startsWith("TIMER")||name.startsWith("JOB"))
        {
            LOGGER.info("监听到job事件 {} \t {}",eventType,activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
