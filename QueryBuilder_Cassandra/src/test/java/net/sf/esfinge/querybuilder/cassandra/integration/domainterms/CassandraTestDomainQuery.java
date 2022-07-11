package net.sf.esfinge.querybuilder.cassandra.integration.domainterms;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.annotation.Condition;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.annotation.DomainTerms;
import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

import java.util.List;

@DomainTerm(term="teenager",
		conditions={@Condition(property="age",comparison=ComparisonType.GREATER_OR_EQUALS,value="13"),
		            @Condition(property="age",comparison=ComparisonType.LESSER_OR_EQUALS,value="30")})
@DomainTerms({
	@DomainTerm(term="adult guys", conditions=@Condition(property="age",comparison=ComparisonType.GREATER_OR_EQUALS,value="25")),
	@DomainTerm(term="silva family", conditions=@Condition(property="lastName",value="Silva"))
})		            
public interface CassandraTestDomainQuery extends Repository<Person> {
	public List<Person> getPersonTeenager();
	public List<Person> getPersonSilvaFamilyAdultGuys();
	public List<Person> getPersonAdultGuys();
	public List<Person> getPersonSilvaFamilyByAge(@Greater Integer age);
}
