package org.esfinge.querybuilder.neo4j.dynamic;

import java.util.List;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.Condition;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;
import net.sf.esfinge.querybuilder.annotation.Starts;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

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
