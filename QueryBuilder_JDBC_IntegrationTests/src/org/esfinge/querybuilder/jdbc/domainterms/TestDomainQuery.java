package org.esfinge.querybuilder.jdbc.domainterms;

import java.util.List;

import org.esfinge.querybuilder.jdbc.testresources.Person;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Condition;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.annotation.DomainTerms;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

@DomainTerm(term="teenager",
conditions={@Condition(property="age",comparison=ComparisonType.GREATER_OR_EQUALS,value="13"),
            @Condition(property="age",comparison=ComparisonType.LESSER_OR_EQUALS,value="19")})
@DomainTerms({	
	@DomainTerm(term="old guys", conditions=@Condition(property="age",comparison=ComparisonType.GREATER_OR_EQUALS,value="65")),	
	@DomainTerm(term="paulista", conditions=@Condition(property="address.state",value="SP"))
})		 

public interface TestDomainQuery extends Repository<Person> {
	
	public List<Person> getPersonById(Integer Id);
	public List<Person> getPersonTeenager();
	public List<Person> getPersonPaulista();
	public List<Person> getPersonTeenagerPaulista();
	public List<Person> getPersonOldGuys();
	public List<Person> getPersonPaulistaByAge(@Greater Integer age);	
}