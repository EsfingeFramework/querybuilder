package org.esfinge.querybuilder.model.persistence;

import java.util.List;

import org.esfinge.querybuilder.Repository;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;
import org.esfinge.querybuilder.model.Person;

public interface PersonOperations extends Repository<Person> {

	public List<Person> getPerson();

	public List<Person> getPersonOrderByPersonIdDesc();

	public List<Person> getPersonByPersonNameStartsAndPersonSurNameStarts(
			@IgnoreWhenNull String name, @IgnoreWhenNull String surname);

}
