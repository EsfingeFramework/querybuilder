package net.sf.esfinge.querybuilder.cassandra.unit.querybuilding.resultsprocessing;

import net.sf.esfinge.querybuilder.cassandra.unit.reflection.TestClass;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class BasicProcessorTest {
    protected List<TestClass> objectList;

    @Before
    public void init() {
        objectList = new ArrayList<>();

        TestClass obj1 = new TestClass(1, "Pedro", "Silva");
        TestClass obj2 = new TestClass(2, "Marcos", "Ferreira");
        TestClass obj3 = new TestClass(3, "Antonio", "Marques");
        TestClass obj4 = new TestClass(4, "Marcos", "Silva");
        TestClass obj5 = new TestClass(5, "Silvia", "Bressan");

        objectList.add(obj1);
        objectList.add(obj2);
        objectList.add(obj3);
        objectList.add(obj4);
        objectList.add(obj5);
    }

}
