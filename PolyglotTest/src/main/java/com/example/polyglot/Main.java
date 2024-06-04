package com.example.polyglot;

import com.example.polyglot.entities.Address;
import com.example.polyglot.entities.Person;
import com.example.polyglot.mongodb.MongoDBExample;
import esfinge.querybuilder.core.QueryBuilder;

public class Main {

    public static void main(String[] args) {
//        JPAExample jpaExample = QueryBuilder.create(JPAExample.class);
//        for (var temp : jpaExample.getTemperatura()) {
//            System.out.println(temp.getId());
//            System.out.println(temp.getLatitude());
//            System.out.println(temp.getLongitude());
//            System.out.println(temp.getMaxima());
//            System.out.println(temp.getMinima());
//            System.out.println(temp.getMes());
//        }

        MongoDBExample mongoDBExample = QueryBuilder.create(MongoDBExample.class);

        var address1 = new Address();
        address1.setId(1);
        address1.setCity("São Paulo");
        address1.setState("SP");

        var address2 = new Address();
        address2.setId(2);
        address2.setCity("Rio de Janeiro");
        address2.setState("RJ");

        var address3 = new Address();
        address3.setCity("São José dos Campos");
        address3.setState("SP");

        var person1 = new Person();
        person1.setId(1);
        person1.setName("Fernando");
        person1.setLastName("Pereira");
        person1.setAge(40);
        person1.setAddress(address3);

        var person2 = new Person();
        person2.setId(2);
        person2.setName("Beltrano");
        person2.setLastName("Albuquerque");
        person2.setAge(20);
        person2.setAddress(address2);

        mongoDBExample.save(person1);
        mongoDBExample.save(person2);

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
