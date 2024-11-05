package ef.qb.jpa1_tests;

import ef.qb.core.Repository;
import ef.qb.core.annotation.Greater;
import ef.qb.core.annotation.Starts;
import ef.qb.jpa1_tests.resources.Person;
import java.util.List;

public interface TestQuery extends Repository<Person> {

    public List<Person> getPerson();

    public Person getPersonById(Integer id);

    public List<Person> getPersonByLastName(String lastname);

    public Person getPersonByNameAndLastName(String name, String lastname);

    public List<Person> getPersonByNameOrLastName(String name, String lastname);

    public List<Person> getPersonByAddressCity(String city);

    public List<Person> getPersonByLastNameAndAddressState(String lastname, String state);

    public List<Person> getPersonByAge(@Greater Integer age);

    public List<Person> getPersonByAgeLesser(Integer age);

    public List<Person> getPersonByLastNameNotEquals(String name);

    public List<Person> getPersonByName(@Starts String name);
}
