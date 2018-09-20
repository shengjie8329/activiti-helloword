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
public class ProcessEventListener implements ActivitiEventListener{

    public static final Logger LOGGER = LoggerFactory.getLogger(ProcessEventListener.class);

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {

        ActivitiEventType eventType = activitiEvent.getType();
        if(ActivitiEventType.PROCESS_STARTED.equals(eventType))
        {
            //如果事件类型是流程启动,打印出日志
            LOGGER.info("流程启动 {} \t {}",eventType,activitiEvent.getProcessInstanceId());
        }else if(ActivitiEventType.PROCESS_COMPLETED.equals(eventType))
        {
            //如果事件类型是流程结束,打印出日志
            LOGGER.info("流程结束 {} \t {}",eventType,activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
