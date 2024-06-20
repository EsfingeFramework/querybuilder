package esfinge.querybuilder.mongodb_tests.dynamic;

import esfinge.querybuilder.core.Repository;
import esfinge.querybuilder.core.annotation.CompareToNull;
import esfinge.querybuilder.core.annotation.Condition;
import esfinge.querybuilder.core.annotation.DomainTerm;
import esfinge.querybuilder.core.annotation.IgnoreWhenNull;
import esfinge.querybuilder.core.annotation.Starts;
import esfinge.querybuilder.core.methodparser.ComparisonType;
import java.util.List;

@DomainTerm(term = "carioca", conditions = @Condition(property = "address.city", comparison = ComparisonType.EQUALS, value = "Rio de Janeiro"))

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
