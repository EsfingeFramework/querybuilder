package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.Session;

public interface CassandraSessionProvider {

    Session getSession();

}