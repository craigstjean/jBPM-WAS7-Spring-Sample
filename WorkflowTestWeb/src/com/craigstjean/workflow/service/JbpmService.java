package com.craigstjean.workflow.service;

import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.task.service.local.LocalTaskService;

public interface JbpmService {
	StatefulKnowledgeSession getSession(String flowPath);
	LocalTaskService getTaskService();
}
