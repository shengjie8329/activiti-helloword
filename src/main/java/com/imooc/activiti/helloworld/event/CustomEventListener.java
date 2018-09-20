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
public class CustomEventListener implements ActivitiEventListener{

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomEventListener.class);

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {

        ActivitiEventType eventType = activitiEvent.getType();
        if(ActivitiEventType.CUSTOM.equals(eventType))
        {
            LOGGER.info("监听到自定义事件 {} \t {}",eventType,activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
