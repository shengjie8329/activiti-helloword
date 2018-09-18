package com.imooc.activiti.config;

import com.imooc.activiti.helloworld.DemoMain;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class ConfigDBTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigDBTest.class);


    @Test
    public void testConfig1() {
        //底层用的是 StandaloneInMemProcessEngineConfiguration
        //配置文件名是默认的  activiti.cfg.xml ， 引擎的beanId 是 processEngineConfiguration
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();

        LOGGER.info("configuration = {}", configuration);
        ProcessEngine processEngine = configuration.buildProcessEngine();

        /**
         * 实际对应的数据库操作， 默认的策略是 create-drop
         * this.databaseSchemaUpdate = "create-drop";
         15:55:18.832 [main] INFO  o.a.engine.impl.db.DbSqlSession - performing create on engine with resource org/activiti/db/create/activiti.h2.create.engine.sql
         15:55:18.886 [main] INFO  o.a.engine.impl.db.DbSqlSession - performing create on history with resource org/activiti/db/create/activiti.h2.create.history.sql
         15:55:18.903 [main] INFO  o.a.engine.impl.db.DbSqlSession - performing create on identity with resource org/activiti/db/create/activiti.h2.create.identity.sql
         15:55:18.907 [main] INFO  o.a.engine.impl.ProcessEngineImpl - ProcessEngine default created
         15:55:18.926 [main] INFO  c.imooc.activiti.config.ConfigDBTest - 获取流程引擎 default
         15:55:18.927 [main] INFO  o.a.engine.impl.db.DbSqlSession - performing drop on engine with resource org/activiti/db/drop/activiti.h2.drop.engine.sql
         15:55:18.939 [main] INFO  o.a.engine.impl.db.DbSqlSession - performing drop on history with resource org/activiti/db/drop/activiti.h2.drop.history.sql
         15:55:18.941 [main] INFO  o.a.engine.impl.db.DbSqlSession - performing drop on identity with resource org/activiti/db/drop/activiti.h2.drop.identity.sql

         */

        LOGGER.info("获取流程引擎 {}", processEngine.getName());
        processEngine.close();

    }

    @Test
    public void testConfig2() {
        //底层用的是 StandaloneInMemProcessEngineConfiguration
        //配置文件名是默认的  activiti.cfg.xml ， 引擎的beanId 是 processEngineConfiguration
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");

        LOGGER.info("configuration = {}", configuration);
        ProcessEngine processEngine = configuration.buildProcessEngine();

        LOGGER.info("获取流程引擎 {}", processEngine.getName());
        processEngine.close();

    }
}
