package org.esfinge.querybuilder.mongodb;

import java.util.Set;

import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.MappedClass;

import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class MongoDBEntityClassProvider implements EntityClassProvider {

	@Override
	public Class<?> getEntityClass(String name) {
		DatastoreProvider dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
		
		Morphia morphia = dsp.getMorphia();
		
		Set<String> keyset = morphia.getMapper().getMCMap().keySet();
		MappedClass mappedClass = null;
		
		for(String className : keyset){
			if(name.equalsIgnoreCase(className) || name.equalsIgnoreCase(className.substring(className.lastIndexOf(".") + 1)))
				mappedClass = morphia.getMapper().getMCMap().get(className);
		}
		
		if(mappedClass == null)
			return null;
		else{
			return mappedClass.getClazz();
		}
		
	}

}
