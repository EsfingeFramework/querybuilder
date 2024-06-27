package com.example.polyglot;

import com.example.polyglot.entities.Address;
import com.example.polyglot.entities.Person;
import com.example.polyglot.mongodb.MongoDBExample1;
import esfinge.querybuilder.core.QueryBuilder;

public class Main {

    public static void main(String[] args) {

        var person = new Person();
        person.setAge(30);
        person.setName("Jonh");
        person.setLastName("Smith");
        var address = new Address();
        address.setCity("Rio de Janeiro");
        address.setUf("RJ");
        person.setAddress(address);

        var mongoDBExample1 = QueryBuilder.create(MongoDBExample1.class);
        mongoDBExample1.save(person);

//        var mongoDBExample1 = QueryBuilder.create(MongoDBExample1.class);
//        var p1 = mongoDBExample1.getById(1);
//        System.out.println(p1.getId());
//        System.out.println(p1.getName());
//        System.out.println(p1.getLastName());
//        System.out.println(p1.getAge());
//        System.out.println(p1.getAddress().getCity());
//        System.out.println(p1.getAddress().getState());
//
//        var mongoDBExample2 = QueryBuilder.create(MongoDBExample2.class);
//        for (var p : mongoDBExample2.getPessoa()) {
//            System.out.println(p.getId());
//            System.out.println(p.getName());
//            System.out.println(p.getLastName());
//            System.out.println(p.getAge());
//            System.out.println(p.getAddress().getCity());
//            System.out.println(p.getAddress().getState());
//        }
    }
}
