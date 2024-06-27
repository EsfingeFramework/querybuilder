package com.example.polyglot;

import com.example.polyglot.entities.Address;
import com.example.polyglot.entities.Endereco;
import com.example.polyglot.entities.Person;
import com.example.polyglot.entities.Pessoa;
import com.example.polyglot.mongodb.MongoDBExample1;
import com.example.polyglot.mongodb.MongoDBExample2;
import esfinge.querybuilder.core.QueryBuilder;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        var person = new Person();
        person.setAge(30);
        person.setName("Fernando");
        person.setLastName("De Tal");
        var address = new Address();
        address.setCity("Brasília");
        address.setUf("DF");
        person.setAddress(address);

        var mongoDBExample1 = QueryBuilder.create(MongoDBExample1.class);
        mongoDBExample1.save(person);

        var pessoa = new Pessoa();
        pessoa.setAge(30);
        pessoa.setName("Fernando");
        pessoa.setLastName("De Tal");

        var endereco1 = new Endereco();
        endereco1.setCity("Brasília");
        endereco1.setUf("DF");

        var endereco2 = new Endereco();
        endereco2.setCity("Itamonte");
        endereco2.setUf("MG");

        var endereco3 = new Endereco();
        endereco3.setCity("Porto Alegre");
        endereco3.setUf("RS");

        var addresses = new ArrayList<Endereco>();
        addresses.add(endereco1);
        addresses.add(endereco2);
        addresses.add(endereco3);

        pessoa.setAddresses(addresses);

        var mongoDBExample2 = QueryBuilder.create(MongoDBExample2.class);
        mongoDBExample2.save(pessoa);

        var persons1 = mongoDBExample1.getPersonByName("Fernando");
        for (var p1 : persons1) {
            System.out.println(p1);
            //mongoDBExample1.delete(p1);
        }

        var persons2 = mongoDBExample2.getPessoaByName("Fernando");
        for (var p2 : persons2) {
            System.out.println(p2);
            //mongoDBExample2.delete(p2);
        }
    }
}
