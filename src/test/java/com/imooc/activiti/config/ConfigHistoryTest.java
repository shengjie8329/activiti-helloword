package com.imooc.activiti.config;

import com.google.common.collect.Maps;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * MDC测试
 * Created by Administrator on 2018/9/18 0018.
 */
public class ConfigHistoryTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigHistoryTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_history.cfg.xml");


    @Test
    @Deployment(resources = {"my-process_mdcerror.bpmn20.xml"})
    public void test() {

        //启动流程
        Map<String,Object> params = Maps.newHashMap();
        params.put("keyStart1","value1");
        params.put("keyStart2","value2");
        //启动流程并设置参数
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process",params);
        //修改变量
        List<Execution> executions = activitiRule.getRuntimeService().createExecutionQuery().listPage(0, 100);
        for (Execution execution : executions) {
            LOGGER.info("execution = {}",execution);
        }
        LOGGER.info("execution size = {}", executions.size());
        String id = executions.iterator().next().getId();
        activitiRule.getRuntimeService().setVariable(id,"keyStart1","value_");

        //提交表单
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Map<String,String> properties = Maps.newHashMap();
        properties.put("formKey1","valuef1");
        properties.put("formKey2","valuef2");
        activitiRule.getFormService().submitTaskFormData(task.getId(),properties);



//        assertEquals("Activiti is awesome!", task.getName());
//        activitiRule.getTaskService().complete(task.getId());




        //输出历史内容

        //输出历史活动
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info("historicActivityInstance = {}",historicActivityInstance);
        }
        LOGGER.info("historicActivityInstances size = {}", historicActivityInstances.size());

        //输出历史表单
        List<HistoricTaskInstance> historicTaskInstances = activitiRule.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .listPage(0, 100);
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            LOGGER.info("historicTaskInstance = {}",historicTaskInstance);
        }
        LOGGER.info("historicTaskInstances size = {}", historicTaskInstances.size());

        List<HistoricDetail> historicDetailsForm = activitiRule.getHistoryService()
                .createHistoricDetailQuery()
                .formProperties()
                .listPage(0, 100);
        for (HistoricDetail historicDetailForm : historicDetailsForm) {
            LOGGER.info("historicDetailForm = {}",historicDetailForm);
        }
        LOGGER.info("historicDetailsForm size= {}", historicDetailsForm.size());

        //输出历史详情
        List<HistoricDetail> historicDetails = activitiRule.getHistoryService()
                .createHistoricDetailQuery()
                .listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            LOGGER.info("historicDetail = {}",historicDetail);
        }
        LOGGER.info("historicDetails size= {}", historicDetails.size());

    }



}
