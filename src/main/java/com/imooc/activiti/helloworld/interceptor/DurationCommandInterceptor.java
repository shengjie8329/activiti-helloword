package com.imooc.activiti.helloworld.interceptor;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行时间的拦截器
 * Created by Administrator on 2018/9/20 0020.
 */
public class DurationCommandInterceptor extends AbstractCommandInterceptor{

    public static final Logger LOGGER = LoggerFactory.getLogger(DurationCommandInterceptor.class);

    @Override
    public <T> T execute(CommandConfig commandConfig, Command<T> command) {
        long start = System.currentTimeMillis();
        try
        {
            return this.getNext().execute(commandConfig,command);
        }
        finally
        {
            long duration = System.currentTimeMillis() - start;
            LOGGER.info("{} 执行时长 {} 毫秒",command.getClass().getSimpleName(),duration);
        }
    }
}
