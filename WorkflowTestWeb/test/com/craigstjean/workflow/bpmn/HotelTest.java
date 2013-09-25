package com.craigstjean.workflow.bpmn;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItem;
import org.jbpm.test.JbpmJUnitTestCase;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.craigstjean.workflow.service.RoomResolver;

public class HotelTest extends JbpmJUnitTestCase {
	@Mock
	private RoomResolver roomResolver;
	
	@Test
	public void testHasRooms() {
		MockitoAnnotations.initMocks(this);
		when(roomResolver.getAvailableRoomOrDefault(-1)).thenReturn(-1);
		
		StatefulKnowledgeSession ksession = createKnowledgeSession("com/craigstjean/workflow/bpmn/hotel.bpmn");
		
		TestWorkItemHandler testHandler = new TestWorkItemHandler();
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task", testHandler);
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("roomResolver", roomResolver);
		ProcessInstance processInstance = ksession.startProcess("com.craigstjean.workflow.bpmn.hotel", variables);
		//assertProcessInstanceAborted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "StartProcess", "Customer Requests Room");
		
		WorkItem workItem = testHandler.getWorkItem();
		assertNotNull(workItem);
		assertEquals("Human Task", workItem.getName());
		
		ksession.getWorkItemManager().completeWorkItem(workItem.getId(), null);
		//assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "Clerk Looks for Rooms");
	}
}
