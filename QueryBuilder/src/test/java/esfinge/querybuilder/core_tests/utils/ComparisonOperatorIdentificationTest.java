package esfinge.querybuilder.core_tests.utils;

import esfinge.querybuilder.core.annotation.Greater;
import esfinge.querybuilder.core.utils.ReflectionUtils;
import static org.junit.Assert.*;
import org.junit.Test;

public class ComparisonOperatorIdentificationTest {

    @Test
    public void testComparisonOperator() {
        var isComparisonOperator = ReflectionUtils.isComparisonOperator(Greater.class);
        assertTrue("GreaterThan has the comparison operator annotation", isComparisonOperator);
    }

    @Test
    public void testNotComparisonOperator() {
        var isComparisonOperator = ReflectionUtils.isComparisonOperator(Other.class);
        assertFalse("GreaterThan has the comparison operator annotation", isComparisonOperator);
    }

}
