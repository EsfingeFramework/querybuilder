package org.esfinge.querybuilder.jpa1.domainterms;

import java.util.List;

import org.esfinge.querybuilder.Repository;
import org.esfinge.querybuilder.annotation.Condition;
import org.esfinge.querybuilder.annotation.DomainTerm;
import org.esfinge.querybuilder.annotation.DomainTerms;
import org.esfinge.querybuilder.annotation.Greater;
import org.esfinge.querybuilder.annotation.Lesser;
import org.esfinge.querybuilder.jpa1.testresources.Person;
import org.esfinge.querybuilder.methodparser.ComparisonType;

@DomainTerm(term="teenager",
		conditions={@Condition(property="age",comparison=ComparisonType.GREATER_OR_EQUALS,value="13"),
		            @Condition(property="age",comparison=ComparisonType.LESSER_OR_EQUALS,value="19")})
@DomainTerms({
	@DomainTerm(term="old guys", conditions=@Condition(property="age",comparison=ComparisonType.GREATER_OR_EQUALS,value="65")),
	@DomainTerm(term="paulista", conditions=@Condition(property="address.state",value="SP"))
})		            
public interface TestDomainQuery extends Repository<Person>{
	public List<Person> getPersonTeenager();
	public List<Person> getPersonPaulista();
	public List<Person> getPersonTeenagerPaulista();
	public List<Person> getPersonOldGuys();
	public List<Person> getPersonPaulistaByAge(@Greater Integer age);
}
