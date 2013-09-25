package com.craigstjean.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.workitem.wsht.GenericHTWorkItemHandler;
import org.jbpm.task.TaskService;
import org.jbpm.task.identity.UserGroupCallback;
import org.jbpm.task.identity.UserGroupCallbackManager;
import org.jbpm.task.query.TaskSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowServiceImpl implements WorkflowService {
	@Autowired
	private StatefulKnowledgeSession ksession;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private GenericHTWorkItemHandler workItemHandler;
	
	@Autowired
	private RoomResolver roomResolver;
	
	public WorkflowServiceImpl() {
		UserGroupCallbackManager.getInstance().setCallback(new UserGroupCallback() {
			@Override
			public boolean existsUser(String userId) {
				return true;
			}
			
			@Override
			public boolean existsGroup(String groupId) {
				return true;
			}
			
			@Override
			public List<String> getGroupsForUser(String userId, List<String> taskGroupIds, List<String> knownGroupIds) {
				if (userId.endsWith("Customer")) {
					List<String> result = new ArrayList<String>();
					result.add("Customers");
					return result;
				} else if (userId.endsWith("Clerk")) {
					List<String> result = new ArrayList<String>();
					result.add("Clerks");
					return result;
				}
				
				List<String> result = new ArrayList<String>();
				result.add("NONE");
				return result;
			}
		});
	}
	
	@Override
	public Long startProcess() {
		// TODO Are these right?  Cannot use jbpm:work-item-handlers in Spring
		// because LocalHTWorkItemHandler takes ksession in the constructor
		workItemHandler.setClient(taskService);
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);
		// End TODO
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("roomResolver", roomResolver);
		ProcessInstance processInstance = ksession.startProcess("com.craigstjean.workflow.bpmn.hotel", variables);
		return processInstance.getId();
	}
	
	@Override
	public List<TaskSummary> getTasksForUser(String user) {
		return taskService.getTasksAssignedAsPotentialOwner(user, "en-US");
	}
	
	@Override
	public void startTask(String userId, Long taskId) {
		taskService.start(taskId, userId);
	}
	
	@Override
	public void completeTask(String userId, Long taskId) {
		taskService.complete(taskId, userId, null);
	}
}
