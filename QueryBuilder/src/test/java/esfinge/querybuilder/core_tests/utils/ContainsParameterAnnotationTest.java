package esfinge.querybuilder.core_tests.utils;

import esfinge.querybuilder.core.annotation.CompareToNull;
import esfinge.querybuilder.core.annotation.IgnoreWhenNull;
import esfinge.querybuilder.core.annotation.Starts;
import esfinge.querybuilder.core.exception.ParameterAnnotationNotFoundException;
import esfinge.querybuilder.core.utils.ReflectionUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ContainsParameterAnnotationTest {

    public interface MockClass {

        public void method1(@CompareToNull int param1, @IgnoreWhenNull String param2);

        public void method2(@CompareToNull @IgnoreWhenNull int param);
    }

    @Test
    public void containsParameterAnnotation() throws Exception {
        var method = MockClass.class.getMethod("method1", int.class, String.class);
        var response = ReflectionUtils.containsParameterAnnotation(method, CompareToNull.class);
        assertTrue(response);
    }

    @Test
    public void notContainsParameterAnnotation() throws Exception {
        var method = MockClass.class.getMethod("method1", int.class, String.class);
        var response = ReflectionUtils.containsParameterAnnotation(method, Starts.class);
        assertFalse(response);
    }

    @Test
    public void getParameterAnnotationIndex() throws Exception {
        var methodOne = MockClass.class.getMethod("method1", int.class, String.class);
        var methodOneCompareToNullParamIndex = ReflectionUtils.getParameterAnnotationIndex(methodOne, CompareToNull.class);
        assertEquals(0, methodOneCompareToNullParamIndex);
        var methodOneIgnoreWhenNullParamIndex = ReflectionUtils.getParameterAnnotationIndex(methodOne, IgnoreWhenNull.class);
        assertEquals(1, methodOneIgnoreWhenNullParamIndex);

        var methodTwo = MockClass.class.getMethod("method2", int.class);
        var methodTwoCompareToNullParamIndex = ReflectionUtils.getParameterAnnotationIndex(methodTwo, CompareToNull.class);
        assertEquals(0, methodTwoCompareToNullParamIndex);
        var methodTwoIgnoreWhenNullParamIndex = ReflectionUtils.getParameterAnnotationIndex(methodTwo, IgnoreWhenNull.class);
        assertEquals(0, methodTwoIgnoreWhenNullParamIndex);
    }

    @Test
    public void getParameterAnnotationIndexException() throws Exception {
        final var m = MockClass.class.getMethod("method1", int.class, String.class);
        new AssertException(ParameterAnnotationNotFoundException.class, "The method method1 has no parameter with esfinge.querybuilder.core.annotation.Starts annotation") {
            @Override
            protected void run() {
                ReflectionUtils.getParameterAnnotationIndex(m, Starts.class);
            }
        };
    }

    @Test
    public void haveAndNotHaveParam1() throws Exception {
        var method = MockClass.class.getMethod("method1", int.class, String.class);
        var bol1 = ReflectionUtils.containsParameterAnnotation(method, 0, CompareToNull.class);
        var bol2 = ReflectionUtils.containsParameterAnnotation(method, 0, IgnoreWhenNull.class);
        assertTrue("Parameter 1 contains @CompareToNull", bol1);
        assertFalse("Parameter 1 does not contain @IgnoreWhenNull", bol2);
    }

    @Test
    public void haveAndNotHaveParam2() throws Exception {
        var method = MockClass.class.getMethod("method1", int.class, String.class);
        var bol1 = ReflectionUtils.containsParameterAnnotation(method, 1, CompareToNull.class);
        var bol2 = ReflectionUtils.containsParameterAnnotation(method, 1, IgnoreWhenNull.class);
        assertFalse("Parameter 2 does not contain @CompareToNull", bol1);
        assertTrue("Parameter 2 contains @IgnoreWhenNull", bol2);
    }

    @Test
    public void haveBoth() throws Exception {
        var method = MockClass.class.getMethod("method2", int.class);
        var bol1 = ReflectionUtils.containsParameterAnnotation(method, 0, CompareToNull.class);
        var bol2 = ReflectionUtils.containsParameterAnnotation(method, 0, IgnoreWhenNull.class);
        assertTrue("Parameter contains @CompareToNull", bol1);
        assertTrue("Parameter contains @IgnoreWhenNull", bol2);
    }

}
