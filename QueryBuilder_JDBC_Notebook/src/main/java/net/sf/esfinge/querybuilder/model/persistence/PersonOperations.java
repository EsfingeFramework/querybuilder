package net.sf.esfinge.querybuilder.model.persistence;

import java.util.List;

import net.sf.esfinge.querybuilder.model.Person;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;

public interface PersonOperations extends Repository<Person> {

	public List<Person> getPerson();

	public List<Person> getPersonOrderByPersonIdDesc();

	public List<Person> getPersonByPersonNameStartsAndPersonSurNameStarts(
			@IgnoreWhenNull String name, @IgnoreWhenNull String surname);

}
