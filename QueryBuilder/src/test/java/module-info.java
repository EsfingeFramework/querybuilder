module querybuilder.core_tests {
    requires querybuilder.core;
    requires junit.dep;

    exports ef.qb.core_tests;
    exports ef.qb.core_tests.methodparser;
    exports ef.qb.core_tests.methodparser.conversor;
    exports ef.qb.core_tests.utils;

    opens ef.qb.core_tests;
    opens ef.qb.core_tests.methodparser;
    opens ef.qb.core_tests.methodparser.conversor;
    opens ef.qb.core_tests.utils;

    uses ef.qb.core_tests.utils.InterfacePriority;
    uses ef.qb.core_tests.utils.TestInterface;

    provides ef.qb.core.Repository with
            ef.qb.core_tests.DummyRepository;

    provides ef.qb.core.executor.QueryExecutor with
            ef.qb.core_tests.DummyQueryExecutor;

    provides ef.qb.core_tests.utils.InterfacePriority with
            ef.qb.core_tests.utils.LowPriority,
            ef.qb.core_tests.utils.MediumPriority,
            ef.qb.core_tests.utils.HighPriority;

    provides ef.qb.core_tests.utils.TestInterface with
            ef.qb.core_tests.utils.TestString,
            ef.qb.core_tests.utils.TestInt;


}
