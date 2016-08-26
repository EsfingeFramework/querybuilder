package org.esfinge.querybuilder.jdbc.queryobject;

import java.util.List;

import net.sf.esfinge.querybuilder.annotation.Condition;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.annotation.QueryObject;
import net.sf.esfinge.querybuilder.jdbc.testresources.Person;

@DomainTerm(term="paulista", conditions=@Condition(property="address.state",value="SP"))
public interface TestQueryObject {
	
	public Person getPerson(@QueryObject SimpleQueryObject qo);
	public List<Person> getPerson(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPersonPaulista(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPerson(@QueryObject CompareNullQueryObject qo);
	public List<Person> getPersonOrderByNameAsc(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPersonOrderByNameDesc(@QueryObject ComparisonTypeQueryObject qo);

}
