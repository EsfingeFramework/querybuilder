package com.example.polyglot.mongodb;

import com.example.polyglot.entities.Pessoa;
import esfinge.querybuilder.core.Repository;
import esfinge.querybuilder.core.annotation.CompareToNull;
import esfinge.querybuilder.core.annotation.Condition;
import esfinge.querybuilder.core.annotation.DomainTerm;
import esfinge.querybuilder.core.annotation.GreaterOrEquals;
import esfinge.querybuilder.core.annotation.IgnoreWhenNull;
import esfinge.querybuilder.core.annotation.Starts;
import esfinge.querybuilder.core.annotation.TargetEntity;
import esfinge.querybuilder.core.methodparser.ComparisonType;
import java.util.List;

@DomainTerm(term = "carioca", conditions = @Condition(property = "address.city", comparison = ComparisonType.EQUALS, value = "Rio de Janeiro"))

@TargetEntity(Pessoa.class)
public interface MongoDBExample2 extends Repository<Pessoa> {

    public List<Pessoa> getPessoa();

    public List<Pessoa> getPessoaCarioca();

    public List<Pessoa> getPessoaCariocaByName(String nome);

    public Pessoa getPessoaByNameAndLastName(@CompareToNull String name, @CompareToNull String lastname);

    public List<Pessoa> getPessoaByNameOrLastName(@IgnoreWhenNull String name, @IgnoreWhenNull String lastname);

    public List<Pessoa> getPessoaByAddressCity(String city);

    public List<Pessoa> getPessoaByNameOrLastNameAndAddressCity(String name, String lastName, String city);

    public List<Pessoa> getPessoaByName(String name);

    public List<Pessoa> getPessoaByAge(@IgnoreWhenNull Integer age);

    public List<Pessoa> getPessoaByAgeAndName(@GreaterOrEquals Integer age, String name);

    public List<Pessoa> getPessoaByAgeGreater(Integer age);

    public List<Pessoa> getPessoaByNameNotEquals(String name);

    public List<Pessoa> getPessoaByLastName(@CompareToNull String name);

    public List<Pessoa> getPessoaByNameAndAge(@Starts String name, @CompareToNull Integer age);

    public List<Pessoa> getPessoaByNameAndAgeAndLastName(@IgnoreWhenNull String name, Integer age, @IgnoreWhenNull String lastName);
}
