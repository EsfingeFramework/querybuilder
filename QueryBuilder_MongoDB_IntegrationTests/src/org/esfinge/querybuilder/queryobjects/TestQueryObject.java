package org.esfinge.querybuilder.queryobjects;

import java.util.List;

import org.esfinge.querybuilder.annotation.Condition;
import org.esfinge.querybuilder.annotation.DomainTerm;
import org.esfinge.querybuilder.annotation.DomainTerms;
import org.esfinge.querybuilder.annotation.QueryObject;
import org.esfinge.querybuilder.mongodb.testresources.Person;
import org.esfinge.querybuilder.methodparser.ComparisonType;

@DomainTerms({
	@DomainTerm(term="paulista", conditions=@Condition(property="address.state",value="SP")),
	@DomainTerm(term="teste", conditions=@Condition(property="name", comparison=ComparisonType.CONTAINS, value="M"))
})
public interface TestQueryObject {
	
	public Person getPerson(@QueryObject SimpleQueryObject qo);
	public List<Person> getPerson(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPersonPaulista(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPersonTeste();
	public List<Person> getPerson(@QueryObject CompareNullQueryObject qo);
	public List<Person> getPersonOrderByNameAsc(@QueryObject ComparisonTypeQueryObject qo);
	public List<Person> getPersonOrderByNameDesc(@QueryObject ComparisonTypeQueryObject qo);

}
