package com.imooc.activiti.helloworld.interceptor;

import org.activiti.engine.debug.ExecutionTreeUtil;
import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.activiti.engine.logging.LogMDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/9/19 0019.
 */
public class MDCCommandInvoker extends DebugCommandInvoker{

    public static final Logger LOGGER = LoggerFactory.getLogger(MDCCommandInvoker.class);

    @Override
    public void executeOperation(Runnable runnable) {
        boolean mdcEnabled = LogMDC.isMDCEnabled();
        //开启MDC日志
        LogMDC.setMDCEnabled(true);
        if(runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation)runnable;
            //如果是可操作的对象，则将MDC信息放到上下文中间
            if(operation.getExecution() != null) {
                LogMDC.putMDCExecution(operation.getExecution());
            }
        }
        super.executeOperation(runnable);
        LogMDC.clear();
        if(!mdcEnabled)
        {
            LogMDC.setMDCEnabled(false);
        }
    }

}
