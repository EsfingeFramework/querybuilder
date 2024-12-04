package ef.qb.cassandra_tests.unit.querybuilding.resultsprocessing;

import ef.qb.cassandra_tests.unit.reflection.TestClass;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;

public class BasicProcessorTest {

    protected List<TestClass> objectList;

    @Before
    public void init() {
        objectList = new ArrayList<>();

        var obj1 = new TestClass(1, "Pedro", "Silva");
        var obj2 = new TestClass(2, "Marcos", "Ferreira");
        var obj3 = new TestClass(3, "Antonio", "Marques");
        var obj4 = new TestClass(4, "Marcos", "Silva");
        var obj5 = new TestClass(5, "Silvia", "Bressan");

        objectList.add(obj1);
        objectList.add(obj2);
        objectList.add(obj3);
        objectList.add(obj4);
        objectList.add(obj5);
    }

}
