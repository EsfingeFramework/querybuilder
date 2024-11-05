package ef.qb.core_tests.utils;

import ef.qb.core.utils.ServiceLocator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

public class ServiceLocatorTest {

    @Ignore
    @Test
    public void getClassBasedServiceString() {
        var ti = ServiceLocator.getServiceByRelatedClass(TestInterface.class, String.class);
        assertTrue(ti instanceof TestString);
    }

    @Ignore
    @Test
    public void getClassBasedServiceInt() {
        var ti = ServiceLocator.getServiceByRelatedClass(TestInterface.class, int.class);
        assertTrue(ti instanceof TestInt);
    }

    @Ignore
    @Test
    public void getClassBasedServiceInteger() {
        var ti = ServiceLocator.getServiceByRelatedClass(TestInterface.class, Integer.class);
        assertTrue(ti instanceof TestInt);
    }

    @Ignore
    @Test
    public void verifyCache() {
        var ti1 = ServiceLocator.getServiceByRelatedClass(TestInterface.class, String.class);
        var ti2 = ServiceLocator.getServiceByRelatedClass(TestInterface.class, String.class);
        assertSame(ti1, ti2);
    }

    @Ignore
    @Test
    public void getTheHighestPriority() {
        var obj = ServiceLocator.getServiceImplementation(InterfacePriority.class);
        assertEquals(HighPriority.class, obj.getClass());
    }

    @Ignore
    @Test
    public void getServiceListByPriority() {
        var list = ServiceLocator.getServiceImplementationList(InterfacePriority.class);
        assertEquals(HighPriority.class, list.get(0).getClass());
        assertEquals(MediumPriority.class, list.get(1).getClass());
        assertEquals(LowPriority.class, list.get(2).getClass());
    }
}
