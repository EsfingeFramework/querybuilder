package net.sf.esfinge.querybuilder.cassandra.entity;

import net.sf.esfinge.querybuilder.cassandra.CassandraEntityClassProvider;

/**
 * Empty interface uses to signal that a particular Class has
 * to be viewed as an entity for tha cassandra database
 * Used by the {@link CassandraEntityClassProvider#getEntityClass(String s)} method
 */
public interface CassandraEntity {
}
