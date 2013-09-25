package com.craigstjean.workflow.service;

import java.util.List;

import org.jbpm.task.query.TaskSummary;

public interface WorkflowService {
	Long startProcess();
	List<TaskSummary> getTasksForUser(String user);
	void startTask(String userId, Long taskId);
	void completeTask(String userId, Long taskId);
}
