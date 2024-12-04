package ef.qb.neo4j_tests.dynamic;

import ef.qb.core.Repository;
import ef.qb.core.annotation.CompareToNull;
import ef.qb.core.annotation.Condition;
import ef.qb.core.annotation.DomainTerm;
import ef.qb.core.annotation.IgnoreWhenNull;
import ef.qb.core.annotation.Starts;
import ef.qb.core.annotation.TargetEntity;
import ef.qb.core.methodparser.ComparisonType;
import ef.qb.neo4j_tests.domain.Person;
import java.util.List;

@DomainTerm(term = "carioca", conditions = @Condition(property = "address.city", comparison = ComparisonType.EQUALS, value = "Rio de Janeiro"))
@TargetEntity(Person.class)
public interface TestQuery extends Repository<Person> {

    public List<Person> getPersonByName(String name);

    public List<Person> getPersonByAge(@IgnoreWhenNull Integer age);

    public List<Person> getPersonByLastName(@CompareToNull String lastName);

    public List<Person> getPersonByNameAndLastName(@CompareToNull String name, @CompareToNull String lastName);

    public List<Person> getPersonByNameOrLastName(@IgnoreWhenNull String name, @IgnoreWhenNull String lastName);

    public List<Person> getPersonByNameAndAgeAndLastName(@IgnoreWhenNull String name, Integer age, @IgnoreWhenNull String lastName);

    public List<Person> getPersonByNameOrAgeAndLastName(@IgnoreWhenNull String name, @IgnoreWhenNull Integer age, @IgnoreWhenNull String lastName);

    public List<Person> getPersonByNameAndAge(@Starts String name, @CompareToNull Integer age);
}
