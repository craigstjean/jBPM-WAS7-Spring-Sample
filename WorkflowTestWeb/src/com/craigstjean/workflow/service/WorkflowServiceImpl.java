package com.craigstjean.workflow.service;

import java.util.ArrayList;
import java.util.List;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.task.Content;
import org.jbpm.task.identity.UserGroupCallback;
import org.jbpm.task.identity.UserGroupCallbackManager;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.utils.ContentMarshallerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class WorkflowServiceImpl implements WorkflowService {
	@Autowired
	private JbpmService jbpmService;
	
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
	@Transactional
	public Long startProcess() {
		StatefulKnowledgeSession ksession = jbpmService.getSession("com/craigstjean/workflow/bpmn/hotel.bpmn");
		ProcessInstance processInstance = ksession.startProcess("com.craigstjean.workflow.bpmn.hotel", null);
		return processInstance.getId();
	}
	
	@Override
	public List<TaskSummary> getTasksForUser(String user) {
		List<TaskSummary> taskSummaries = jbpmService.getTaskService().getTasksAssignedAsPotentialOwner(user, "en-UK");
		
		// TODO bad code, here just to show how to get the data
		for (TaskSummary taskSummary : taskSummaries) {
			if (taskSummary.getName().equals("Clerk Gives Room Number")) {
				long contentId = jbpmService.getTaskService().getTask(taskSummary.getId()).getTaskData().getDocumentContentId();
				Content content = jbpmService.getTaskService().getContent(contentId);
				Object obj = ContentMarshallerHelper.unmarshall(content.getContent(), null);
				taskSummary.setDescription(obj.toString());
			}
		}
		
		return taskSummaries;
	}
	
	@Override
	@Transactional
	public void startTask(String userId, Long taskId) {
		jbpmService.getTaskService().start(taskId, userId);
	}
	
	@Override
	@Transactional
	public void completeTask(String userId, Long taskId) {
		jbpmService.getTaskService().complete(taskId, userId, null);
	}
}
