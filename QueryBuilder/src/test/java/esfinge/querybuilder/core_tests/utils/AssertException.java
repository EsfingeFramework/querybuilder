package esfinge.querybuilder.core_tests.utils;

import junit.framework.Assert;

public abstract class AssertException {

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public AssertException(Class<? extends Throwable> eType, String msgContained) {
        try {
            run();
        } catch (Throwable e) {
            if (e.getClass() != eType) {
                assertFail("Expecting <" + eType.getName() + ">, "
                        + "but <" + e.getClass().getName() + "> was thrown.");
            }

            if (!e.getMessage().toLowerCase().contains(msgContained.toLowerCase())) {
                assertFail("Substring \"" + msgContained + "\" "
                        + "not found in \"" + e.getMessage() + "\".");
            }
            return;
        }
        assertFail("Expecting <" + eType.getName() + ">");
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public AssertException(Class<? extends Throwable> eType, Class<? extends Throwable> eCauseType) {
        try {
            run();
        } catch (Throwable e) {
            if (e.getClass() != eType) {
                assertFail("Expecting <" + eType.getName() + ">, but <" + e.getClass().getName() + "> was thrown.");
            }

            if (e.getCause().getClass() != eCauseType) {
                assertFail("Expecting cause <" + eCauseType.getName() + ">, but <" + e.getCause().getClass().getName() + "> was the cause.");
            }
            return;
        }
        assertFail("Expecting <" + eType.getName() + ">");
    }

    protected abstract void run();

    private void assertFail(String message) {
        Assert.assertTrue(message, false);
    }
}
