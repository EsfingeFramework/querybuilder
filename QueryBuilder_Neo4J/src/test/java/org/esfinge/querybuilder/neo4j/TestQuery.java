package org.esfinge.querybuilder.neo4j;

import java.util.List;

import org.esfinge.querybuilder.Repository;
import org.esfinge.querybuilder.annotation.CompareToNull;
import org.esfinge.querybuilder.annotation.Condition;
import org.esfinge.querybuilder.annotation.DomainTerm;
import org.esfinge.querybuilder.annotation.GreaterOrEquals;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;
import org.esfinge.querybuilder.annotation.Starts;
import org.esfinge.querybuilder.methodparser.ComparisonType;
import org.esfinge.querybuilder.neo4j.dynamic.Person;

@DomainTerm(term="carioca", conditions=@Condition(property="address.city",comparison=ComparisonType.EQUALS,value="Rio de Janeiro"))

public interface TestQuery extends Repository<Person>{
	public List<Person> getPerson();
	public List<Person> getPersonCarioca();
	public List<Person> getPersonCariocaByName(String nome);
	public Person getPersonByNameAndLastName(@CompareToNull String name, @CompareToNull String lastname);
	public List<Person> getPersonByNameOrLastName(@IgnoreWhenNull String name, @IgnoreWhenNull String lastname);
	public List<Person> getPersonByAddressCity(String city);
	public List<Person> getPersonByNameOrLastNameAndAddressCity(String name, String lastName, String city);
	public List<Person> getPersonByName(String name);
	public List<Person> getPersonByAge(@IgnoreWhenNull Integer age);
	public List<Person> getPersonByAgeAndName(@GreaterOrEquals Integer age, String name);
	public List<Person> getPersonByAgeGreater(Integer age);
	public List<Person> getPersonByNameNotEquals(String name);
	public List<Person> getPersonByLastName(@CompareToNull String name);
	public List<Person> getPersonByNameAndAge(@Starts String name, @CompareToNull Integer age);
	public List<Person> getPersonByNameAndAgeAndLastName(@IgnoreWhenNull String name, Integer age, @IgnoreWhenNull String lastName);
}
