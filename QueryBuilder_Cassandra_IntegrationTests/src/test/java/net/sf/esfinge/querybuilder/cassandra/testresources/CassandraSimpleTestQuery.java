package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Greater;

import java.util.List;

public interface CassandraSimpleTestQuery extends Repository<Person> {

    List<Person> getPerson();

    Person getPersonById(Integer id);

    List<Person> getPersonByAgeGreater(Integer age);

    Person getPersonByAgeLesserOrEquals(Integer age);

    List<Person> getPersonByLastName(String lastname);

    Person getPersonByNameAndLastName(String name, String lastname);

    List<Person> getPersonByAge(@Greater Integer age);

    List<Person> getPersonByAgeLesser(Integer age);

    List<Person> getPersonByIdAndNameAndLastNameAndAge(Integer id, String name, String lastname, Integer age);

    // QUERIES WITH WRONG NAMING CONVENTION
    List<Person> getPersonByIdAndNameAndLastName(Integer id, String name);

    List<Person> getPersonByIdAndName(Integer id, String name, Integer age);


}
