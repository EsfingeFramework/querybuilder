package net.sf.esfinge.querybuilder.cassandradb.entity;

import net.sf.esfinge.querybuilder.cassandradb.CassandraDBEntityClassProvider;

/**
 * Empty interface uses to signal that a particular Class has
 * to be signaled as an entity for tha cassandra database
 * Used by the {@link CassandraDBEntityClassProvider#getEntityClass(String s)} method
 */
public interface CassandraDBEntity {
}
