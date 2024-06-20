package esfinge.querybuilder.core_tests.utils;

import esfinge.querybuilder.core.utils.NameUtils;
import static org.junit.Assert.*;
import org.junit.Test;

public class NameUtilsTest {

    @Test
    public void testEndsWithOperator() {
        assertTrue(NameUtils.endsWithComparisonOperator("nameEquals"));
        assertTrue(NameUtils.endsWithComparisonOperator("nameGreater"));
        assertTrue(NameUtils.endsWithComparisonOperator("nameLesser"));
        assertTrue(NameUtils.endsWithComparisonOperator("nameGreaterOrEquals"));
        assertTrue(NameUtils.endsWithComparisonOperator("nameLesserOrEquals"));
        assertTrue(NameUtils.endsWithComparisonOperator("nameNotEquals"));
        assertTrue(NameUtils.endsWithComparisonOperator("nameContains"));
        assertTrue(NameUtils.endsWithComparisonOperator("nameStarts"));
        assertTrue(NameUtils.endsWithComparisonOperator("nameEnds"));
        assertFalse(NameUtils.endsWithComparisonOperator("name"));
    }

}
