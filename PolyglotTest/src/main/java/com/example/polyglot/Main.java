package com.example.polyglot;

import com.example.polyglot.mongodb.Example1;
import esfinge.querybuilder.core.QueryBuilder;
import org.esfinge.virtuallab.demo.polyglot.Address;
import org.esfinge.virtuallab.demo.polyglot.Person;

public class Main {

    public static void main(String[] args) {

        var p1 = new Person();
        p1.setAge(40);
        p1.setName("Fernando");
        p1.setLastName("de Oliveira Pereira");
        var a1 = new Address();
        a1.setCity("Caçapava");
        a1.setUf("SP");
        p1.setAddress(a1);

        var p2 = new Person();
        p2.setAge(40);
        p2.setName("André");
        p2.setLastName("Aparecido de Souza Ivo");
        var a2 = new Address();
        a2.setCity("São José dos Campos");
        a2.setUf("SP");
        p2.setAddress(a2);

        var p3 = new Person();
        p3.setAge(44);
        p3.setName("Daniel");
        p3.setLastName(" Silveira de Oliveira");
        var a3 = new Address();
        a3.setCity("Brasília");
        a3.setUf("DF");
        p3.setAddress(a3);

        var p4 = new Person();
        p4.setAge(41);
        p4.setName("Eduardo");
        p4.setLastName("Pacheco da Luz");
        var a4 = new Address();
        a4.setCity("Belo Horizonte");
        a4.setUf("MG");
        p4.setAddress(a4);

        var p5 = new Person();
        p5.setAge(29);
        p5.setName("Klaifer");
        p5.setLastName("Garcia");
        var a5 = new Address();
        a5.setCity("Goiânia");
        a5.setUf("Goiás");
        p5.setAddress(a5);

        var example1 = QueryBuilder.create(Example1.class);
        example1.save(p1);
        example1.save(p2);
        example1.save(p3);
        example1.save(p4);
        example1.save(p5);

        var persons1 = example1.getPerson();
        for (var p : persons1) {
            System.out.println(p);
            //example1.delete(p);
        }
    }
}
