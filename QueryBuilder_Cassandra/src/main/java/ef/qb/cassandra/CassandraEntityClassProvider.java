package ef.qb.cassandra;

import ef.qb.cassandra.entity.CassandraEntity;
import static java.util.ServiceLoader.load;

public class CassandraEntityClassProvider {

    public Class<?> getEntityClass(String s) {
        var cassandraDBEntities = load(CassandraEntity.class);
        for (var e : cassandraDBEntities) {
            if (e.getClass().getSimpleName().equals(s)) {
                return e.getClass();
            }
        }

        return null;
    }
}
