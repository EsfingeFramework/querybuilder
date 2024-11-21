package ef.qb.cassandra;

import com.datastax.driver.core.Session;

public interface CassandraSessionProvider {

    Session getSession();

}