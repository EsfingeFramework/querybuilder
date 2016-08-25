package org.esfinge.querybuilder.jpa1.nullvalues;

import java.util.List;

import org.esfinge.querybuilder.jpa1.testresources.Person;

import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;
import net.sf.esfinge.querybuilder.annotation.Starts;

public interface TestNullValueQueries {
	
	public List<Person> getPersonByName(@CompareToNull String name);
	public List<Person> getPersonByNameAndLastName(@Starts String name, @CompareToNull String lastname);
	public List<Person> getPersonByAgeGreater(@IgnoreWhenNull Integer age);
	public List<Person> getPersonByNameStartsAndLastNameStarts(@IgnoreWhenNull String name, @IgnoreWhenNull String lastname);

}
