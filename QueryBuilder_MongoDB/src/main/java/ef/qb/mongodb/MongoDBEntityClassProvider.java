package ef.qb.mongodb;

import dev.morphia.Datastore;
import dev.morphia.mapping.codec.pojo.EntityModel;
import ef.qb.core.utils.ServiceLocator;
import java.util.List;

public class MongoDBEntityClassProvider {

    private MongoDBEntityClassProvider() {
    }

    public static Class<?> getEntityClass(String name) {
        DatastoreProvider dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
        Datastore datastore = dsp.getDatastore();
        List<EntityModel> mappedEntities = datastore.getMapper().getMappedEntities();
        for (EntityModel entityModel : mappedEntities) {
            Class<?> entityClass = entityModel.getType();
            String className = entityClass.getName();
            String simpleClassName = entityClass.getSimpleName();
            if (name.equalsIgnoreCase(className) || name.equalsIgnoreCase(simpleClassName)) {
                return entityClass;
            }
        }

        return null;
    }
}
