package com.craigstjean.workflow.service;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomNumberHandlerImpl implements WorkItemHandler {
	@Autowired
	private RoomResolver roomResolver;
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("roomNumber", roomResolver.getAvailableRoomOrDefault(-1));
		manager.completeWorkItem(workItem.getId(), results);
	}
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// No action required
	}
}
