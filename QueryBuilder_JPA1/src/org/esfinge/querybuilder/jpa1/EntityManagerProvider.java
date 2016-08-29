package org.esfinge.querybuilder.jpa1;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface EntityManagerProvider {
	
	public EntityManager getEntityManager();
	
	public EntityManagerFactory getEntityManagerFactory();

}
