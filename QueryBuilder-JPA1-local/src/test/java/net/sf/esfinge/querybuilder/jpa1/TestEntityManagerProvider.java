package net.sf.esfinge.querybuilder.jpa1;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.sf.esfinge.querybuilder.jpa1.EntityManagerProvider;

public class TestEntityManagerProvider implements EntityManagerProvider {

	@Override
	public EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("database_test");
	}

}
