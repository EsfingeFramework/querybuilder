package net.sf.esfinge.querybuilder.jpa1;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;

import net.sf.esfinge.querybuilder.exception.EntityClassNotFoundException;
import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class JPAEntityClassProvider implements EntityClassProvider {

	@Override
	public Class<?> getEntityClass(String name) {
		EntityManagerProvider emp = ServiceLocator.getServiceImplementation(EntityManagerProvider.class);
		EntityManagerFactory emf = emp.getEntityManagerFactory();
		for (EntityType et : emf.getMetamodel().getEntities()){
			String entityName = et.getName().substring(et.getName().lastIndexOf(".")+1);
			if(entityName.equalsIgnoreCase(name))
				return et.getJavaType();
		}
		return null;
	}

}
