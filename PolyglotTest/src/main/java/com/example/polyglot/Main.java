package com.example.polyglot;

import com.example.polyglot.mongodb.MongoDBExample1;
import com.example.polyglot.mongodb.MongoDBExample2;
import esfinge.querybuilder.core.QueryBuilder;

public class Main {

    public static void main(String[] args) {

        var mongoDBExample1 = QueryBuilder.create(MongoDBExample1.class);
        for (var p : mongoDBExample1.getPersonByName("Fernando")) {
            System.out.println(p.getId());
            System.out.println(p.getName());
            System.out.println(p.getLastName());
            System.out.println(p.getAge());
            System.out.println(p.getAddress().getCity());
            System.out.println(p.getAddress().getState());
        }

        var mongoDBExample2 = QueryBuilder.create(MongoDBExample2.class);
        for (var p : mongoDBExample2.getPessoaByName("Fernando")) {
            System.out.println(p.getId());
            System.out.println(p.getName());
            System.out.println(p.getLastName());
            System.out.println(p.getAge());
            System.out.println(p.getAddress().getCity());
            System.out.println(p.getAddress().getState());
        }
    }
}
