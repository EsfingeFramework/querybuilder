module querybuilder.core_tests {
    requires querybuilder.core;
    requires junit.dep;

    exports esfinge.querybuilder.core_tests;
    exports esfinge.querybuilder.core_tests.methodparser;
    exports esfinge.querybuilder.core_tests.methodparser.conversor;
    exports esfinge.querybuilder.core_tests.utils;

    opens esfinge.querybuilder.core_tests;
    opens esfinge.querybuilder.core_tests.methodparser;
    opens esfinge.querybuilder.core_tests.methodparser.conversor;
    opens esfinge.querybuilder.core_tests.utils;

    uses esfinge.querybuilder.core_tests.utils.InterfacePriority;
    uses esfinge.querybuilder.core_tests.utils.TestInterface;

    provides esfinge.querybuilder.core.Repository with
            esfinge.querybuilder.core_tests.DummyRepository;

    provides esfinge.querybuilder.core.executor.QueryExecutor with
            esfinge.querybuilder.core_tests.DummyQueryExecutor;

    provides esfinge.querybuilder.core_tests.utils.InterfacePriority with
            esfinge.querybuilder.core_tests.utils.LowPriority,
            esfinge.querybuilder.core_tests.utils.MediumPriority,
            esfinge.querybuilder.core_tests.utils.HighPriority;

    provides esfinge.querybuilder.core_tests.utils.TestInterface with
            esfinge.querybuilder.core_tests.utils.TestString,
            esfinge.querybuilder.core_tests.utils.TestInt;


}
