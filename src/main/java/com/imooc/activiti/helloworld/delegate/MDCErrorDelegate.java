package com.imooc.activiti.helloworld.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/9/19 0019.
 */
public class MDCErrorDelegate implements JavaDelegate{

    public static final Logger LOGGER = LoggerFactory.getLogger(MDCErrorDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        LOGGER.info("run MDCErrorDelegate");
//        throw new RuntimeException("only test");
    }
}
