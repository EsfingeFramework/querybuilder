package com.example.polyglot.mongodb;

import ef.qb.core.Repository;
import ef.qb.core.annotation.CompareToNull;
import ef.qb.core.annotation.Condition;
import ef.qb.core.annotation.DomainTerm;
import ef.qb.core.annotation.GreaterOrEquals;
import ef.qb.core.annotation.IgnoreWhenNull;
import ef.qb.core.annotation.Starts;
import ef.qb.core.annotation.TargetEntity;
import ef.qb.core.methodparser.ComparisonType;
import java.util.List;
import org.esfinge.virtuallab.demo.polyglot.Person;

@DomainTerm(term = "carioca", conditions = @Condition(property = "address.city", comparison = ComparisonType.EQUALS, value = "Rio de Janeiro"))

@TargetEntity(Person.class)
public interface Example1 extends Repository<Person> {

    public List<Person> getPerson();

    public List<Person> getPersonCarioca();

    public List<Person> getPersonCariocaByName(String nome);

    public Person getPersonByNameAndLastName(@CompareToNull String name, @CompareToNull String lastname);

    public List<Person> getPersonByNameOrLastName(@IgnoreWhenNull String name, @IgnoreWhenNull String lastname);

    public List<Person> getPersonByAddressCity(String city);

    public List<Person> getPersonByNameOrLastNameAndAddressCity(String name, String lastName, String city);

    public List<Person> getPersonByName(String name);

    public List<Person> getPersonByAge(@IgnoreWhenNull Integer age);

    public List<Person> getPersonByAgeAndName(@GreaterOrEquals Integer age, String name);

    public List<Person> getPersonByAgeGreater(Integer age);

    public List<Person> getPersonByNameNotEquals(String name);

    public List<Person> getPersonByLastName(@CompareToNull String name);

    public List<Person> getPersonByNameAndAge(@Starts String name, @CompareToNull Integer age);

    public List<Person> getPersonByNameAndAgeAndLastName(@IgnoreWhenNull String name, Integer age, @IgnoreWhenNull String lastName);
}
