package com.craigstjean.workflow.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.craigstjean.workflow.model.SystemPreference;

@Repository
public class SystemPreferenceDaoImpl implements SystemPreferenceDao {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public String getPreference(String key) {
		SystemPreference pref = em.find(SystemPreference.class, key);
		
		if (pref == null || pref.getValue() == null) {
			return "";
		}
		
		return pref.getValue();
	}
	
	@Override
	@Transactional
	public void setPreference(String key, String value) {
		SystemPreference pref = em.find(SystemPreference.class, key);
		
		if (pref == null) {
			pref = new SystemPreference(key, value);
			em.persist(pref);
		} else {
			pref.setValue(value);
			em.merge(pref);
		}
	}
}
