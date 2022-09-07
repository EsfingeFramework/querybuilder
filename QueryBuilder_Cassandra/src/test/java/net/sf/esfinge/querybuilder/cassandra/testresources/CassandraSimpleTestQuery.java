package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Greater;

import java.util.List;

public interface CassandraSimpleTestQuery extends Repository<Person> {

    List<Person> getPerson();

}
