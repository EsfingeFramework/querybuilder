package net.sf.esfinge.querybuilder.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ServiceLocatorTest {

    @Test
    public void getClassBasedServiceString() {
        var ti = ServiceLocator.getServiceByRelatedClass(TestInterface.class, String.class);
        assertTrue(ti instanceof TestString);
    }

    @Test
    public void getClassBasedServiceInt() {
        var ti = ServiceLocator.getServiceByRelatedClass(TestInterface.class, int.class);
        assertTrue(ti instanceof TestInt);
    }

    @Test
    public void getClassBasedServiceInteger() {
        var ti = ServiceLocator.getServiceByRelatedClass(TestInterface.class, Integer.class);
        assertTrue(ti instanceof TestInt);
    }

    @Test
    public void verifyCache() {
        var ti1 = ServiceLocator.getServiceByRelatedClass(TestInterface.class, String.class);
        var ti2 = ServiceLocator.getServiceByRelatedClass(TestInterface.class, String.class);
        assertSame(ti1, ti2);
    }

    @Test
    public void getTheHighestPriority() {
        var obj = ServiceLocator.getServiceImplementation(InterfacePriority.class);
        assertEquals(HighPriority.class, obj.getClass());
    }

    @Test
    public void getServiceListByPriority() {
        var list = ServiceLocator.getServiceImplementationList(InterfacePriority.class);
        assertEquals(HighPriority.class, list.get(0).getClass());
        assertEquals(MediumPriority.class, list.get(1).getClass());
        assertEquals(LowPriority.class, list.get(2).getClass());
    }

}
