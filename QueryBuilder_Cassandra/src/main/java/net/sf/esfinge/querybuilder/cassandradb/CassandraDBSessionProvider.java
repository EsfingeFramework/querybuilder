package net.sf.esfinge.querybuilder.cassandradb;

import com.datastax.driver.core.Session;

public interface CassandraDBSessionProvider {

    Session getSession();

}