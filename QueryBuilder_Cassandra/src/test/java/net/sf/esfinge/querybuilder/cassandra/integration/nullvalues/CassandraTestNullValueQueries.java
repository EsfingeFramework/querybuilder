package net.sf.esfinge.querybuilder.cassandra.integration.nullvalues;

import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;
import net.sf.esfinge.querybuilder.annotation.Starts;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;

import java.util.List;

public interface CassandraTestNullValueQueries {

	public List<Person> getPersonByName(@CompareToNull String name);
	//public List<Person> getPersonByAgeGreater(Integer age);
	public List<Person> getPersonByAgeGreater(@IgnoreWhenNull Integer age);
	public List<Person> getPersonByNameAndLastName(@IgnoreWhenNull String name, @IgnoreWhenNull String lastname);

}
