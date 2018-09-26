package com.imooc.activiti.coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.*;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 测试 Repository Service
 * Created by Administrator on 2018/9/21 0021.
 */
public class RepositoryServiceTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(RepositoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void test() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("测试部署资源")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        LOGGER.info(" deploy = {}",deploy);

        //测试多次部署
        DeploymentBuilder deployment1 = repositoryService.createDeployment();
        deployment1.name("测试部署资源2")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        deployment1.deploy();

        //部署查询器
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> deployments = deploymentQuery
//                .deploymentId(deploy.getId())
                .orderByDeploymenTime().asc()
//                .singleResult();
                .listPage(0, 100);
        for (Deployment deployment : deployments) {
            LOGGER.info("deployment = {}",deployment);
        }
        LOGGER.info("deploymentList.size = {}",deployments.size());

        List<ProcessDefinition> processDefinitions = repositoryService
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().asc()
//                .deploymentId(deployment.getId())
                .listPage(0, 100);

        for (ProcessDefinition processDefinition : processDefinitions) {
            LOGGER.info("processDefinition = {}, version = {}, key = {}, id = {}",
                    processDefinition,
                    processDefinition.getVersion(),
                    processDefinition.getKey(),
                    processDefinition.getId());
        }


    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testSuspend(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .singleResult();

        LOGGER.info("processDefinition.id = {}",processDefinition.getId());

        //尝试挂起某个流程定义。挂起的流程不能再启动
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());

        //尝试启动该流程
        startProcessByDefId(processDefinition);

        //重新启用该流程定义
        repositoryService.activateProcessDefinitionById(processDefinition.getId());
        startProcessByDefId(processDefinition);
    }

    private void startProcessByDefId(ProcessDefinition processDefinition) {
        try
        {
            LOGGER.info("启动");
            activitiRule.getRuntimeService()
                    .startProcessInstanceById(processDefinition.getId());
            LOGGER.info("启动成功");
        }
        catch (Exception e)
        {
            LOGGER.info("启动失败");
            LOGGER.info(e.getMessage(),e);
        }
    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCandidateStarter() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .singleResult();

        LOGGER.info("processDefinition.id = {}", processDefinition.getId());

        //为流程定义指定 启动人 或启动组，注意，activiti框架在流程启动时并不会自动校验，必须根据业务需求查询出启动人 或 组后自己来校验
        //为该流程指定一个启动人，只有他才能启动流程
        repositoryService.addCandidateStarterUser(processDefinition.getId(),"user");
        //为该流程指定启动组
        repositoryService.addCandidateStarterGroup(processDefinition.getId(),"groupM");

        //查询出流程对应的关系人或组
        List<IdentityLink> identityLinkList = repositoryService
                .getIdentityLinksForProcessDefinition(processDefinition.getId());
        for (IdentityLink identityLink : identityLinkList) {
            LOGGER.info("identityLink = {}",identityLink);
        }

        //删除启动人或启动组
        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(),"groupM");
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(),"user");
    }

}
