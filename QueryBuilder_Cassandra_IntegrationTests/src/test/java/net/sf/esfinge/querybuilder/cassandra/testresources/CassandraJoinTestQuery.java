package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.Starts;

import java.util.List;

public interface CassandraJoinTestQuery extends Repository<Person> {

    List<Person> getPersonByAddressCity(String city);

    List<Person> getPersonByAddressCityAndAddressState(String city, String state);

    List<Person> getPersonByLastNameAndAddressState(String lastname, String state);

    List<Person> getPersonByAddressStateAndLastName(String state, String lastname);

    List<Person> getPersonByAddressCityOrAddressState(String city, String state);

    List<Person> getPersonByAddressCityOrLastName(String city, String lastName);

    List<Person> getPersonByAddressCityOrLastNameOrderById(String city, String lastName);

    List<Person> getPersonByAddressState(@CompareToNull String state);

    List<Person> getPersonByNameOrAddressCityAndAgeLesserOrLastNameOrderByName(String name, String city, Integer age, @Starts String lastName);
}
