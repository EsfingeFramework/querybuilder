package net.sf.esfinge.querybuilder.cassandra.testresources;

import jnr.ffi.annotations.In;
import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.annotation.Starts;

import java.util.List;

public interface CassandraTestQuery extends Repository<Person> {

    // SIMPLE QUERIES
    List<Person> getPerson();
    Person getPersonById(Integer id);
    Person getPersonByIdGreater(Integer id);
    List<Person> getPersonByLastName(String lastname);
    Person getPersonByNameAndLastName(String name, String lastname);
    List<Person> getPersonByAge(@Greater Integer age);
    List<Person> getPersonByAgeLesser(Integer age);

    List<Person> getPersonByIdAndNameAndLastNameAndAge(Integer id, String name, String lastname, Integer age);

    // QUERIES WITH ORDERING
    List<Person> getPersonOrderByName();
    List<Person> getPersonByAgeOrderByNameDesc(@Greater Integer age);
    List<Person> getPersonOrderByNameAndLastName();
    List<Person> getPersonOrderByNameDescAndLastNameAsc();
    List<Person> getPersonByAgeAndLastNameOrderByAgeAndLastNameDescAndName(@Greater Integer age, String lastname);

    // QUERIES WITH WRONG NAMING CONVENTION
    List<Person> getPersonByIdAndNameAndLastName(Integer id, String name);
    List<Person> getPersonByIdAndName(Integer id, String name, Integer age);


    // TODO: METHODS WITH OR CONNECTORS DON'T WORK IN CASSANDRA, IMPLEMENT AT APPLICATION LOGIC OR LEAVE IT FORBIDDEN?
    List<Person> getPersonByNameOrLastName(String name, String lastname);
    // List<Person> getPersonByAgeOrLastNameOrderByNameDesc(@Greater Integer age, String lastname);


    // TODO: METHODS WITH CUSTOM CLASS ATTRIBUTE DON'T WORK IN CASSANDRA, BECAUSE THERE IS NO SUCH THIS AS JOINS, IMPLEMENT AT APPLICATION LOGIC OR LEAVE IT FORBIDDEN?
    // List<Person> getPersonByAddressCity(String city);
    // List<Person> getPersonByLastNameAndAddressState(String lastname, String state);

    // TODO: SEARCH QUERIES WITH 'LIKE' ARE NOT SUPPORTED BY CASSANDRA, IMPLEMENT AT APPLICATION LOGIC OR LEAVE IT FORBIDDEN?
    List<Person> getPersonByLastNameNotEquals(String name);
    List<Person> getPersonByName(@Starts String name);


    // List<Person> getPersonByNameEnds(String name);
    // List<Person> getPersonByNameContains(String name);
    // List<Person> getPersonByNameStartsAndAgeGreater(String name, Integer age);


    // test methods
    List<Person> getPersonOrderById();

    List<Person> getPersonOrderByLastName();

    List<Person> getPersonOrderByLastNameAndAgeDesc();
}
