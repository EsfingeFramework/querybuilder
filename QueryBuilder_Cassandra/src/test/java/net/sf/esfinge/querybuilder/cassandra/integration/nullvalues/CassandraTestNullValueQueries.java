package net.sf.esfinge.querybuilder.cassandra.integration.nullvalues;

import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;

import java.util.List;

public interface CassandraTestNullValueQueries {

    List<Person> getPersonByName(@CompareToNull String name);

    List<Person> getPersonByAgeGreater(@IgnoreWhenNull Integer age);

    List<Person> getPersonByNameAndLastName(@IgnoreWhenNull String name, @IgnoreWhenNull String lastname);

}
