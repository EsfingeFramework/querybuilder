package com.example.polyglot;

import com.example.polyglot.mongodb.MongoDBExample1;
import com.example.polyglot.mongodb.MongoDBExample2;
import esfinge.querybuilder.core.QueryBuilder;

public class Main {

    public static void main(String[] args) {

        var mongoDBExample1 = QueryBuilder.create(MongoDBExample1.class);
        var p1 = mongoDBExample1.getById(1);
        System.out.println(p1.getId());
        System.out.println(p1.getName());
        System.out.println(p1.getLastName());
        System.out.println(p1.getAge());
        System.out.println(p1.getAddress().getCity());
        System.out.println(p1.getAddress().getState());

        var mongoDBExample2 = QueryBuilder.create(MongoDBExample2.class);
        for (var p : mongoDBExample2.list()) {
            System.out.println(p.getId());
            System.out.println(p.getName());
            System.out.println(p.getLastName());
            System.out.println(p.getAge());
            System.out.println(p.getAddress().getCity());
            System.out.println(p.getAddress().getState());
        }
    }
}
