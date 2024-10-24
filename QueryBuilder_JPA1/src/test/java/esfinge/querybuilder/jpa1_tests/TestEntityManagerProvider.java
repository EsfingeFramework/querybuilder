package esfinge.querybuilder.jpa1_tests;

import esfinge.querybuilder.jpa1.EntityManagerProvider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
