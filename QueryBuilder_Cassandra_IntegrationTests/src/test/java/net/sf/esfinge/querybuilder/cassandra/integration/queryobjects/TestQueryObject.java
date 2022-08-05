package net.sf.esfinge.querybuilder.cassandra.integration.queryobjects;

import net.sf.esfinge.querybuilder.annotation.Condition;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.annotation.QueryObject;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;

import java.util.List;

@DomainTerm(term = "silva family", conditions = @Condition(property = "lastName", value = "Silva"))
public interface TestQueryObject {

    Person getPerson(@QueryObject SimpleQueryObject qo);

    List<Person> getPerson(@QueryObject ComparisonTypeQueryObject qo);

    List<Person> getPerson(@QueryObject CompareNullQueryObject qo);

    List<Person> getPerson(@QueryObject IgnoreNullQueryObject qo);

    List<Person> getPersonOrderByNameAsc(@QueryObject ComparisonTypeQueryObject qo);

    List<Person> getPersonOrderByNameDesc(@QueryObject ComparisonTypeQueryObject qo);

}
