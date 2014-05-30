package org.esfige.querybuilder.mongodb.dynamic;

import java.util.List;

import org.esfinge.querybuilder.Repository;
import org.esfinge.querybuilder.annotation.CompareToNull;
import org.esfinge.querybuilder.annotation.Condition;
import org.esfinge.querybuilder.annotation.DomainTerm;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;
import org.esfinge.querybuilder.annotation.Starts;
import org.esfinge.querybuilder.methodparser.ComparisonType;

@DomainTerm(term="carioca", conditions=@Condition(property="address.city",comparison=ComparisonType.EQUALS,value="Rio de Janeiro"))

public interface TestQuery extends Repository<Person>{
	public List<Person> getPersonByName(String name);
	public List<Person> getPersonByAge(@IgnoreWhenNull Integer age);
	public List<Person> getPersonByLastName(@CompareToNull String lastName);
	public List<Person> getPersonByNameAndLastName(@CompareToNull String name, @CompareToNull String lastName);
	public List<Person> getPersonByNameOrLastName(@IgnoreWhenNull String name, @IgnoreWhenNull String lastName);
	public List<Person> getPersonByNameAndAgeAndLastName(@IgnoreWhenNull String name, Integer age, @IgnoreWhenNull String lastName);
	public List<Person> getPersonByNameOrAgeAndLastName(@IgnoreWhenNull String name, @IgnoreWhenNull Integer age, @IgnoreWhenNull String lastName);
	public List<Person> getPersonByNameAndAge(@Starts String name, @CompareToNull Integer age);
}
