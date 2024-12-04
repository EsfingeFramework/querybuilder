package ef.qb.jpa1;

import ef.qb.core.annotation.ServicePriority;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ServicePriority(0)
public class DefaultEntityManagerProvider implements EntityManagerProvider {

    @Override
    public EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("DefaultPU");
    }

}
