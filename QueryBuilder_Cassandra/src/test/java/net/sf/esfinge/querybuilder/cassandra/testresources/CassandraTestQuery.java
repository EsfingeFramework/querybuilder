package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.annotation.Starts;

import java.util.List;

public interface CassandraTestQuery extends Repository<Person> {

    List<Person> getPerson();
    Person getPersonById(@Greater Integer id);
    List<Person> getPersonByLastName(String lastname);
    Person getPersonByNameAndLastName(String name, String lastname);
    List<Person> getPersonByAge(@Greater Integer age);
    List<Person> getPersonByAgeLesser(Integer age);
    List<Person> getPersonOrderByName();
    List<Person> getPersonByAgeOrderByNameDesc(@Greater Integer age);
    List<Person> getPersonByAgeAndLastNameOrderByNameDesc(@Greater Integer age, String lastname);

    // TODO: METHODS WITH OR CONNECTORS DON'T WORK IN CASSANDRA, NEED TO IMPLEMENT AT APPLICATION LOGIC
    //List<Person> getPersonByNameOrLastName(String name, String lastname);
    //List<Person> getPersonByAgeOrLastNameOrderByNameDesc(@Greater Integer age, String lastname);


    // TODO: METHODS WITH CUSTOM CLASS ATTRIBUTE DON'T WORK IN CASSANDRA, BECAUSE THERE IS NO SUCH THIS AS JOINS, NEED TO IMPLEMENT AT APPLICATION LOGIC
    //List<Person> getPersonByAddressCity(String city);
    //List<Person> getPersonByLastNameAndAddressState(String lastname, String state);

    // TODO: SEARCH QUERIES WITH 'LIKE' ARE NOT SUPPORTED BY CASSANDRA, NEED TO IMPLEMENT AT APPLICATION LOGIC
    //List<Person> getPersonByLastNameNotEquals(String name);
    //List<Person> getPersonByName(@Starts String name);
    //List<Person> getPersonByNameEnds(String name);
    //List<Person> getPersonByNameContains(String name);
    //List<Person> getPersonByNameStartsAndAgeGreater(String name, Integer age);
}
