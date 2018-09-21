package com.imooc.activiti.config;

import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * MDC测试
 * Created by Administrator on 2018/9/18 0018.
 */
public class ConfigJobTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigJobTest.class);

    @Test
    @Deployment(resources = {"my-process_job.bpmn20.xml"})
    public void test() throws InterruptedException {
        //由于是定时启动，因此不需要使用api来启动
        LOGGER.info("start");
        //使用 createTimerJobQuery 来查询所有已部署的定时任务
        List<Job> jobList = activitiRule.getManagementService()
                .createTimerJobQuery()
                .listPage(0, 100);
        for (Job job : jobList) {
            LOGGER.info("定时任务 = {}, 默认重试次数 = {}", job,job.getRetries());
        }
        LOGGER.info("jobList.size = {}",jobList.size());
        Thread.sleep(1000l*100l);
        LOGGER.info("end");
    }

}
