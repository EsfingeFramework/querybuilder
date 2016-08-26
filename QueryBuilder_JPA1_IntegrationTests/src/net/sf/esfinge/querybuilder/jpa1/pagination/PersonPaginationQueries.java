package net.sf.esfinge.querybuilder.jpa1.pagination;

import java.util.Collection;

import net.sf.esfinge.querybuilder.annotation.Condition;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.annotation.InvariablePageSize;
import net.sf.esfinge.querybuilder.annotation.PageNumber;
import net.sf.esfinge.querybuilder.annotation.QueryBuilder;
import net.sf.esfinge.querybuilder.annotation.QueryObject;
import net.sf.esfinge.querybuilder.annotation.Starts;
import net.sf.esfinge.querybuilder.annotation.VariablePageSize;
import net.sf.esfinge.querybuilder.jpa1.testresources.Person;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

@QueryBuilder
@DomainTerm(term = "teenager",
			conditions = {@Condition(property = "age",
									 comparison = ComparisonType.GREATER_OR_EQUALS, value = "13"),
						  @Condition(property = "age",
									 comparison = ComparisonType.LESSER_OR_EQUALS, value = "18")})
public interface PersonPaginationQueries {

	@InvariablePageSize(7)
	public Collection<Person> getPerson(@PageNumber int number);
	public Collection<Person> getPerson(@PageNumber int number, @VariablePageSize int size);

	@InvariablePageSize(2)
	public Collection<Person> getPersonTeenager(@PageNumber int number);
	public Collection<Person> getPersonTeenager(@PageNumber int number, @VariablePageSize int size);

	@InvariablePageSize(4)
	public Collection<Person> getPersonOrderByName(@PageNumber int number);
	public Collection<Person> getPersonOrderByName(@PageNumber int number, @VariablePageSize int size);

	@InvariablePageSize(4)
	public Collection<Person> getPersonOrderByNameDesc(@PageNumber int number);
	public Collection<Person> getPersonOrderByNameDesc(@PageNumber int number, @VariablePageSize int size);

	@InvariablePageSize(2)
	public Collection<Person> getPersonByAddressCity(String city, @PageNumber int number);
	public Collection<Person> getPersonByAddressCity(String city, @PageNumber int number, @VariablePageSize int size);

	@InvariablePageSize(1)
	public Collection<Person> getPersonByName(@Starts String name, @PageNumber int number);
	public Collection<Person> getPersonByName(@Starts String name, @PageNumber int number, @VariablePageSize int size);

	@InvariablePageSize(3)
	public Collection<Person> getPerson(@QueryObject PersonQuery query, @PageNumber int number);
	public Collection<Person> getPerson(@QueryObject PersonQuery query, @PageNumber int number, @VariablePageSize int size);

}
