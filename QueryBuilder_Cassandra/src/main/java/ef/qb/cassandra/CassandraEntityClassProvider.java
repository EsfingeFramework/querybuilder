package ef.qb.cassandra;

import ef.qb.cassandra.entity.CassandraEntity;
import java.util.ServiceLoader;
import static java.util.ServiceLoader.load;

public class CassandraEntityClassProvider {

    public Class<?> getEntityClass(String s) {
        ServiceLoader<CassandraEntity> cassandraDBEntities = load(CassandraEntity.class);
        for (CassandraEntity e : cassandraDBEntities) {
            if (e.getClass().getSimpleName().equals(s)) {
                return e.getClass();
            }
        }

        return null;
    }
}
