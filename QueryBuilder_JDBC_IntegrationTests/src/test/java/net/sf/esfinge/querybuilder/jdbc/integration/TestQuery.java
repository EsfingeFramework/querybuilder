package net.sf.esfinge.querybuilder.jdbc.integration;

import java.util.List;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.annotation.Starts;
import net.sf.esfinge.querybuilder.jdbc.testresources.Person;

public interface  TestQuery extends Repository<Person>{
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
	public List<Person> getPersonByNameEnds(String name);
	public List<Person> getPersonByNameContains(String name);
	public List<Person> getPersonByNameStartsAndAgeGreater(String name, Integer age);
	public List<Person> getPersonOrderByName();
	public List<Person> getPersonByAgeOrderByNameDesc(@Greater Integer age);
}
