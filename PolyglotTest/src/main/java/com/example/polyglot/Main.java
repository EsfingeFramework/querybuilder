package com.example.polyglot;

import com.example.polyglot.mongodb.MongoDBExample;
import esfinge.querybuilder.core.QueryBuilder;

public class Main {

    public static void main(String[] args) {

        MongoDBExample mongoDBExample = QueryBuilder.create(MongoDBExample.class);
        for (var person : mongoDBExample.getPersonByName("Fernando")) {
            System.out.println(person.getId());
            System.out.println(person.getName());
            System.out.println(person.getLastName());
            System.out.println(person.getAge());
            System.out.println(person.getAddress().getCity());
            System.out.println(person.getAddress().getState());
        }
    }
}
