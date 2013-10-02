package com.craigstjean.workflow.service;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.KnowledgeBase;
import org.drools.SystemEventListener;
import org.drools.SystemEventListenerFactory;
import org.drools.impl.EnvironmentFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.persistence.JpaProcessPersistenceContextManager;
import org.jbpm.persistence.jta.ContainerManagedTransactionManager;
import org.jbpm.process.workitem.wsht.LocalHTWorkItemHandler;
import org.jbpm.task.Group;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.TaskServiceSession;
import org.jbpm.task.service.local.LocalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JbpmServiceImpl implements JbpmService {
	private EntityManagerFactory jbpmEmf;
	
	@Autowired
	private KnowledgeBase kbase;
	
	@Autowired
	private RoomNumberHandlerImpl roomNumberHandler;
	
	private TaskService taskService = null;
	private Map<String, StatefulKnowledgeSession> ksessions = new HashMap<String, StatefulKnowledgeSession>();
	
	@Override
	public StatefulKnowledgeSession getSession(String flowPath) {
		StatefulKnowledgeSession ksession = ksessions.get(flowPath);
		if (ksession != null) {
			return ksession;
		}
		
		ksession = JPAKnowledgeService.newStatefulKnowledgeSession(kbase, null, getEnvironment());
		
		LocalHTWorkItemHandler humanTaskHandler = new LocalHTWorkItemHandler(getTaskService(), ksession);
		humanTaskHandler.setLocal(true);
		humanTaskHandler.connect();
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanTaskHandler);
		ksession.getWorkItemManager().registerWorkItemHandler("Get Room Number", roomNumberHandler);
		
		ksessions.put(flowPath, ksession);
		
		return ksession;
	}
	
	private EntityManagerFactory getEntityManagerFactory() {
		if (jbpmEmf == null) {
			jbpmEmf = Persistence.createEntityManagerFactory("jBPMUnit");
		}
		
		return jbpmEmf;
	}

	private Environment getEnvironment() {
		Environment env = EnvironmentFactory.newEnvironment();
        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, getEntityManagerFactory());
        env.set(EnvironmentName.TRANSACTION_MANAGER, new ContainerManagedTransactionManager());
        env.set(EnvironmentName.PERSISTENCE_CONTEXT_MANAGER, new JpaProcessPersistenceContextManager(env));
		return env;
	}
	
	@Override
	public LocalTaskService getTaskService() {
		if (taskService == null) {
			SystemEventListener systemEventListener = SystemEventListenerFactory.getSystemEventListener();
			taskService = new TaskService(getEntityManagerFactory(), systemEventListener);
			
			TaskServiceSession taskSession = taskService.createSession();
			// TODO here temporarily
			taskSession.addGroup(new Group("Customers"));
			taskSession.addGroup(new Group("Clerks"));
		}
		
		LocalTaskService localTaskService = new LocalTaskService(taskService);
		localTaskService.setEnvironment(getEnvironment());
		localTaskService.connect();
		return localTaskService;
	}
}
