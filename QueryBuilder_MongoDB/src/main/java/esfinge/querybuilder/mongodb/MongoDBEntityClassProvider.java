package esfinge.querybuilder.mongodb;

import esfinge.querybuilder.core.methodparser.EntityClassProvider;
import esfinge.querybuilder.core.utils.ServiceLocator;
import org.mongodb.morphia.mapping.MappedClass;

public class MongoDBEntityClassProvider implements EntityClassProvider {

    @Override
    public Class<?> getEntityClass(String name) {
        var dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
        var morphia = dsp.getMorphia();
        var keyset = morphia.getMapper().getMCMap().keySet();
        MappedClass mappedClass = null;

        for (var className : keyset) {
            if (name.equalsIgnoreCase(className) || name.equalsIgnoreCase(className.substring(className.lastIndexOf(".") + 1))) {
                mappedClass = morphia.getMapper().getMCMap().get(className);
            }
        }

        if (mappedClass == null) {
            return null;
        } else {
            return mappedClass.getClazz();
        }

    }

}
