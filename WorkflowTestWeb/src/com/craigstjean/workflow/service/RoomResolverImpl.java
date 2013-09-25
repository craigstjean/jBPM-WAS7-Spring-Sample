package com.craigstjean.workflow.service;

import org.springframework.stereotype.Component;

@Component
public class RoomResolverImpl implements RoomResolver {
	@Override
	public int getAvailableRoomOrDefault(int defaultRoomNumber) {
		return 2;
	}
}
