package esfinge.querybuilder.jpa1;

import esfinge.querybuilder.core.methodparser.EntityClassProvider;
import esfinge.querybuilder.core.utils.ServiceLocator;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;

public class JPAEntityClassProvider implements EntityClassProvider {

    @Override
    public Class<?> getEntityClass(String name) {
        EntityManagerProvider emp = ServiceLocator.getServiceImplementation(EntityManagerProvider.class);
        EntityManagerFactory emf = emp.getEntityManagerFactory();
        for (EntityType et : emf.getMetamodel().getEntities()) {
            String entityName = et.getName().substring(et.getName().lastIndexOf(".") + 1);
            if (entityName.equalsIgnoreCase(name)) {
                return et.getJavaType();
            }
        }
        return null;
    }

}
