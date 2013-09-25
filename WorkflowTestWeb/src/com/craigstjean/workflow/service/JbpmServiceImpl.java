package com.craigstjean.workflow.service;

import javax.persistence.EntityManagerFactory;

import org.drools.KnowledgeBase;
import org.drools.SystemEventListener;
import org.drools.SystemEventListenerFactory;
import org.drools.impl.EnvironmentFactory;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.persistence.JpaProcessPersistenceContextManager;
import org.jbpm.persistence.jta.ContainerManagedTransactionManager;
import org.jbpm.task.Group;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.TaskServiceSession;
import org.jbpm.task.service.local.LocalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JbpmServiceImpl implements JbpmService {
	@Autowired
	private EntityManagerFactory emf;
	
	@Autowired
	private KnowledgeBase kbase;
	
	@Override
	public StatefulKnowledgeSession getSession(String flowPath) {
		Environment env = EnvironmentFactory.newEnvironment();
        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
        env.set(EnvironmentName.TRANSACTION_MANAGER, new ContainerManagedTransactionManager());
        env.set(EnvironmentName.PERSISTENCE_CONTEXT_MANAGER, new JpaProcessPersistenceContextManager(env));
        
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(null, env);
		return ksession;
	}
	
	@Override
	public LocalTaskService getTaskService() {
		SystemEventListener systemEventListener = SystemEventListenerFactory.getSystemEventListener();
		TaskService taskService = new TaskService(emf, systemEventListener);
		
		TaskServiceSession taskSession = taskService.createSession();
		// TODO here temporarily
		taskSession.addGroup(new Group("Customers"));
		taskSession.addGroup(new Group("Clerks"));
		
		LocalTaskService localTaskService = new LocalTaskService(taskService);
		return localTaskService;
	}
}
