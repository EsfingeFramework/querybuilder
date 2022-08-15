package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.annotation.GreaterOrEquals;
import net.sf.esfinge.querybuilder.annotation.Lesser;

import java.util.List;

public interface CassandraSecondaryQueryTestQuery extends Repository<Person> {

    List<Person> getPersonByNameOrLastName(String name, String lastname);

    List<Person> getPersonByNameOrLastNameOrAge(String name, String lastname, Integer age);

    List<Person> getPersonByIdOrNameOrLastNameOrAge(Integer id, String name, String lastname, Integer age);

    List<Person> getPersonByNameOrLastNameAndAgeGreater(String name, String lastname, Integer age);

    List<Person> getPersonByNameAndLastNameOrAge(String name, String lastname, @Lesser Integer age);

    List<Person> getPersonByAgeOrLastNameOrderByNameDesc(Integer age, String lastname);

    List<Person> getPersonByAgeOrNameStarts(@Greater Integer age, String name);

    List<Person> getPersonByNameOrAge(String name, @CompareToNull Integer age);

    List<Person> getPersonByNameStartsOrAgeAndLastNameNotEqualsOrIdOrderById(String name, @CompareToNull Integer age, String lastName, @GreaterOrEquals Integer id);
}
