package com.craigstjean.workflow.service;

public interface SystemPreferenceDao {
	void setPreference(String key, String value);
	String getPreference(String key);
}
