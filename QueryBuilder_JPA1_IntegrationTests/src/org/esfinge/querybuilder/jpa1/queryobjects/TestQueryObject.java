package org.esfinge.querybuilder.jpa1.queryobjects;

import java.util.List;

import org.esfinge.querybuilder.jpa1.testresources.Person;

import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.Condition;
import net.sf.esfinge.querybuilder.annotation.Contains;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.annotation.QueryObject;

@DomainTerm(term="paulista", conditions=@Condition(property="address.state",value="SP"))
public interface TestQueryObject {
	
	public Person getPerson(@QueryObject SimpleQueryObject qo);
	public List<Person> getPerson(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPersonPaulista(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPerson(@QueryObject CompareNullQueryObject qo);
	public List<Person> getPersonOrderByNameAsc(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPersonOrderByNameDesc(@QueryObject ComparisonTypeQueryObject qo);

}
