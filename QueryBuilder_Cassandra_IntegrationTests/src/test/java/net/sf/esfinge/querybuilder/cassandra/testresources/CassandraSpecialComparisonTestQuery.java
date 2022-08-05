package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Starts;

import java.util.List;

public interface CassandraSpecialComparisonTestQuery extends Repository<Person> {

    List<Person> getPersonByLastNameNotEquals(String name);

    List<Person> getPersonByName(@Starts String name);

    List<Person> getPersonByNameEnds(String name);

    List<Person> getPersonByNameContains(String name);

    List<Person> getPersonByNameStartsAndAgeGreater(String name, Integer age);

    List<Person> getPersonByNameStartsAndAgeGreaterAndLastNameNotEquals(String name, Integer age, String lastName);
}
