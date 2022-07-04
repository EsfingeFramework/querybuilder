package net.sf.esfinge.querybuilder.cassandradb;

import net.sf.esfinge.querybuilder.cassandradb.entity.CassandraDBEntity;
import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;

import java.util.ServiceLoader;

public class CassandraDBEntityClassProvider implements EntityClassProvider {
    @Override
    public Class<?> getEntityClass(String s) {
        ServiceLoader<CassandraDBEntity> cassandraDBEntities = ServiceLoader.load(CassandraDBEntity.class);

        for (CassandraDBEntity e : cassandraDBEntities){
            System.out.println("current: " + e.getClass());
            if (e.getClass().getSimpleName().equals(s))
                return e.getClass();
        }

        return null;
    }
}
