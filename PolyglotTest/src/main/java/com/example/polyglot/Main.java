package com.example.polyglot;

import com.example.polyglot.jpa1.JPAExample;
import com.example.polyglot.mongodb.MongoDBExample;
import esfinge.querybuilder.core.QueryBuilder;

public class Main {

    public static void main(String[] args) {
        JPAExample jpaExample = QueryBuilder.create(JPAExample.class);
        for (var temp : jpaExample.getTemperatura()) {
            System.out.println(temp.getId());
            System.out.println(temp.getLatitude());
            System.out.println(temp.getLongitude());
            System.out.println(temp.getMaxima());
            System.out.println(temp.getMinima());
            System.out.println(temp.getMes());
        }

        MongoDBExample mongoDBExample = QueryBuilder.create(MongoDBExample.class);
        for (var person : mongoDBExample.getPerson()) {
            System.out.println(person.getId());
            System.out.println(person.getName());
            System.out.println(person.getLastName());
            System.out.println(person.getAge());
            System.out.println(person.getAddress().getCity());
            System.out.println(person.getAddress().getState());
        }
    }
}
