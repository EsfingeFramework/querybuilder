package org.esfinge.querybuilder.jdbc.nullvalues;

import java.util.List;

import org.esfinge.querybuilder.annotation.CompareToNull;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;
import org.esfinge.querybuilder.annotation.Starts;
import org.esfinge.querybuilder.jdbc.testresources.Person;

public interface TestNullValueQueries {
	
	public List<Person> getPersonByName(@CompareToNull String name);
	public List<Person> getPersonByNameAndLastName(@Starts String name, @CompareToNull String lastname);
	public List<Person> getPersonByAgeGreater(@IgnoreWhenNull Integer age);
	public List<Person> getPersonByNameStartsAndLastNameStarts(@IgnoreWhenNull String name, @IgnoreWhenNull String lastname);

}
