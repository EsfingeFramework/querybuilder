package ef.qb.jpa1_tests;

import ef.qb.core.annotation.ServicePriority;
import ef.qb.jpa1.EntityManagerProvider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ServicePriority(1)
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
