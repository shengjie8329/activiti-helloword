package com.imooc.activiti.config;

import com.imooc.activiti.helloworld.DemoMain;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class ConfigTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);

    @Test
    public void testConfig1(){
        //底层用的是 StandaloneInMemProcessEngineConfiguration
        //配置文件名是默认的  activiti.cfg.xml ， 引擎的beanId 是 processEngineConfiguration
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();

        LOGGER.info("configuration = {}",configuration);
    }

    @Test
    public void testConfig2(){
        //直接使用独立的api来配置
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();

        LOGGER.info("configuration = {}",configuration);
    }
}
