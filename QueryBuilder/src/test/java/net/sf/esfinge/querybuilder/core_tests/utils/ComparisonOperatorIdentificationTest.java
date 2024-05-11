package net.sf.esfinge.querybuilder.core_tests.utils;

import net.sf.esfinge.querybuilder.annotation.Greater;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;
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
