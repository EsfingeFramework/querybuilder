package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.Starts;

import java.util.List;

public interface CassandraJoinTestQuery extends Repository<Worker> {

    List<Worker> getWorkerByAddressCity(String city);

    List<Worker> getWorkerByAddressCityAndAddressState(String city, String state);

    List<Worker> getWorkerByLastNameAndAddressState(String lastname, String state);

    List<Worker> getWorkerByAddressStateAndLastName(String state, String lastname);

    List<Worker> getWorkerByAddressCityOrAddressState(String city, String state);

    List<Worker> getWorkerByAddressCityOrLastName(String city, String lastName);

    List<Worker> getWorkerByAddressCityOrLastNameOrderById(String city, String lastName);

    List<Worker> getWorkerByAddressState(@CompareToNull String state);

    List<Worker> getWorkerByNameOrAddressCityAndAgeLesserOrLastNameOrderByName(String name, String city, Integer age, @Starts String lastName);
}
