/*
Copyright 2020 Mohammad A. Rahin                                                                                                          

Licensed under the Apache License, Version 2.0 (the "License");                                                                           
you may not use this file except in compliance with the License.                                                                          
You may obtain a copy of the License at                                                                                                   
    http://www.apache.org/licenses/LICENSE-2.0                                                                                            
Unless required by applicable law or agreed to in writing, software                                                                       
distributed under the License is distributed on an "AS IS" BASIS,                                                                         
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.                                                                  
See the License for the specific language governing permissions and                                                                       
limitations under the License.       
*/

package net.dollmar.web.apiworkbench.dao;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import net.dollmar.web.apiworkbench.entity.ApiDef;
import net.dollmar.web.apiworkbench.utils.WorkbenchException;
import net.dollmar.web.apiworkbench.utils.EntityManagerUtil;


public class ApiDefDao {

	
	private void close(EntityManager em) {
		if (em != null) {
			em.close();
		}
	}
	
	public boolean testDatabase() {
		try {
			getAllApiDefs("test");
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	
	public List<ApiDef> getAllApiDefs() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			@SuppressWarnings("unchecked")
			List<ApiDef> keys = em.createQuery("SELECT a FROM ApiDef a").getResultList();
			em.getTransaction().commit();
			
			return keys;
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw new WorkbenchException(500, "Failed to retrieve API definitions from database.", e);
		}
		finally {
			close(em);
		}
	}	
	

	public List<ApiDef> getAllApiDefs(final String apiName) {
		String queryString = "SELECT a FROM ApiDef a WHERE a.apiName = :apiName";
		EntityManager em = EntityManagerUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			TypedQuery<ApiDef> query = em.createQuery(queryString, ApiDef.class);
			query.setParameter("apiName", apiName);
			
			List<ApiDef> keys = query.getResultList();
			em.getTransaction().commit();
			
			return keys;
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw new WorkbenchException(500, "Failed to retrieve API definitions from database.", e);
		}
		finally {
			close(em);
		}
	}
	
	
	
	public ApiDef getApiDef(long rowId) {
		String queryString = "SELECT a FROM ApiDef a WHERE a.id = :rowId";
		EntityManager em = EntityManagerUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			TypedQuery<ApiDef> query = em.createQuery(queryString, ApiDef.class);
			query.setParameter("rowId", rowId);

			List<ApiDef> result = query.getResultList();
			em.getTransaction().commit();
			
			return (result == null || result.size() == 0) ? null : result.get(0);
		}
		catch (WorkbenchException se) {
			throw se;
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw new WorkbenchException(500, String.format("Failed to retrieve API definition '%d' from database.", rowId), e);
		}
		finally {
			close(em);
		}
	}
	
	
	public ApiDef getApiDef(String apiName, String version) {
		String queryString = "SELECT a FROM ApiDef a WHERE a.apiName = :apiName AND a.version = :version";
		EntityManager em = EntityManagerUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			TypedQuery<ApiDef> query = em.createQuery(queryString, ApiDef.class);
			query.setParameter("apiName", apiName);
			query.setParameter("version", version);
			
			List<ApiDef> result = query.getResultList();
			em.getTransaction().commit();
			
			return (result == null || result.size() == 0) ? null : result.get(0);
		}
		catch (WorkbenchException se) {
			throw se;
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw new WorkbenchException(500, String.format("Failed to retrieve API definition '%s:%s' from database.", 
					apiName, version), e);
		}
		finally {
			close(em);
		}
	}
	
	
	public ApiDef saveApiDef(ApiDef apiDef) {
		Objects.requireNonNull(apiDef);
		
		ApiDef existing = getApiDef(apiDef.getApiName(), apiDef.getVersion());
		if (existing != null) {
			throw new WorkbenchException(409, String.format("API definition '%s:%s' already exists", 
					apiDef.getApiName(), apiDef.getVersion()));
		}
		
		EntityManager em = EntityManagerUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			ApiDef na = em.merge(apiDef);
			em.getTransaction().commit();
			
			return na;
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw new WorkbenchException(500, String.format("Failed to persist API definition '%s:%s'.", 
					apiDef.getApiName(), apiDef.getVersion()), e);
		}
		finally {
			close(em);
		}
	}	
	
	
	
	public void deleteApiDef(long id) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			ApiDef api = (ApiDef) em.find(ApiDef.class, id);
			em.remove(api);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw new WorkbenchException(500, String.format("Failed to delete API definition '%d' data from database.",  id), e);
		}
		finally {
			close(em);
		}
	}	
	
}
