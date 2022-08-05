package net.sf.esfinge.querybuilder.cassandra.testresources;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.annotation.Lesser;

import java.util.List;

public interface CassandraOrderByTestQuery extends Repository<Person> {

    List<Person> getPersonOrderByName();

    List<Person> getPersonByAgeOrderByNameDesc(@Greater Integer age);

    List<Person> getPersonByNameOrderByNameDesc(String name);

    List<Person> getPersonOrderByLastNameAndName();

    List<Person> getPersonOrderByLastNameDescAndNameAsc();

    List<Person> getPersonByAgeAndLastNameOrderByAgeAndLastNameDescAndName(@Lesser Integer age, String lastname);
}
