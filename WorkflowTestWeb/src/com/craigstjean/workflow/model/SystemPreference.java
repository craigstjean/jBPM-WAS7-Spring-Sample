package com.craigstjean.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_SYS_PREF")
public class SystemPreference {
	@Id
	@Column(name = "KEY", length = 20, nullable = false)
	private String key;
	
	@Column(name = "VALUE", length = 100, nullable = false)
	private String value;
	
	public SystemPreference() {
		
	}
	
	public SystemPreference(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
