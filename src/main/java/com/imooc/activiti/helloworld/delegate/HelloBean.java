package com.imooc.activiti.helloworld.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/9/21 0021.
 */
public class HelloBean {

    public static final Logger LOGGER = LoggerFactory.getLogger(HelloBean.class);

    public void sayHello(){
        LOGGER.info("sayHello");
    }

}
