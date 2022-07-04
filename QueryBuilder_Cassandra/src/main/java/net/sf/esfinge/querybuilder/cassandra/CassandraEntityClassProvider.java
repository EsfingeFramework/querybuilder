package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.cassandra.entity.CassandraEntity;
import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;

import java.util.ServiceLoader;

public class CassandraEntityClassProvider implements EntityClassProvider {
    @Override
    public Class<?> getEntityClass(String s) {
        ServiceLoader<CassandraEntity> cassandraDBEntities = ServiceLoader.load(CassandraEntity.class);

        for (CassandraEntity e : cassandraDBEntities) {
            System.out.println("current: " + e.getClass());
            if (e.getClass().getSimpleName().equals(s))
                return e.getClass();
        }

        return null;
    }
}
