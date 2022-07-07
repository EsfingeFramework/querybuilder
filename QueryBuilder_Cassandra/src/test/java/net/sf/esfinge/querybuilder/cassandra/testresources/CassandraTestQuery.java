package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.annotation.Starts;

import java.util.List;

public interface CassandraTestQuery extends Repository<Person> {
    List<Person> getPerson();
    Person getPersonById(Integer id);
    List<Person> getPersonByLastName(String lastname);
    Person getPersonByNameAndLastName(String name, String lastname);
    List<Person> getPersonByNameOrLastName(String name, String lastname);
    List<Person> getPersonByAddressCity(String city);
    List<Person> getPersonByLastNameAndAddressState(String lastname, String state);
    List<Person> getPersonByAge(@Greater Integer age);
    List<Person> getPersonByAgeLesser(Integer age);
    List<Person> getPersonByLastNameNotEquals(String name);
    List<Person> getPersonByName(@Starts String name);
    List<Person> getPersonByNameEnds(String name);
    List<Person> getPersonByNameContains(String name);
    List<Person> getPersonByNameStartsAndAgeGreater(String name, Integer age);
    List<Person> getPersonOrderByName();
    List<Person> getPersonByAgeOrderByNameDesc(@Greater Integer age);
}
