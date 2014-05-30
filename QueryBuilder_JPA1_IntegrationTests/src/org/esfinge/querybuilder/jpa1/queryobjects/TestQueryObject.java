package org.esfinge.querybuilder.jpa1.queryobjects;

import java.util.List;

import org.esfinge.querybuilder.annotation.CompareToNull;
import org.esfinge.querybuilder.annotation.Condition;
import org.esfinge.querybuilder.annotation.Contains;
import org.esfinge.querybuilder.annotation.DomainTerm;
import org.esfinge.querybuilder.annotation.QueryObject;
import org.esfinge.querybuilder.jpa1.testresources.Person;

@DomainTerm(term="paulista", conditions=@Condition(property="address.state",value="SP"))
public interface TestQueryObject {
	
	public Person getPerson(@QueryObject SimpleQueryObject qo);
	public List<Person> getPerson(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPersonPaulista(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPerson(@QueryObject CompareNullQueryObject qo);
	public List<Person> getPersonOrderByNameAsc(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPersonOrderByNameDesc(@QueryObject ComparisonTypeQueryObject qo);

}
