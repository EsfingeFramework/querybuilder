module querybuilder.core_tests {
    requires org.apache.tomcat.jasper.el;
    requires jakarta.el;
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

    uses esfinge.querybuilder.core.Repository;
    uses esfinge.querybuilder.core.methodparser.EntityClassProvider;
    uses esfinge.querybuilder.core.executor.QueryExecutor;
    uses esfinge.querybuilder.core.methodparser.conversor.FromStringConversor;
    uses esfinge.querybuilder.core.methodparser.MethodParser;
    uses esfinge.querybuilder.core.methodparser.formater.FormaterFactory;

    provides esfinge.querybuilder.core.Repository with
            esfinge.querybuilder.core_tests.DummyRepository;

    provides esfinge.querybuilder.core.executor.QueryExecutor with
            esfinge.querybuilder.core_tests.DummyQueryExecutor;

    provides esfinge.querybuilder.core.methodparser.EntityClassProvider with
            esfinge.querybuilder.core_tests.DummyEntityClassProvider;

    provides esfinge.querybuilder.core_tests.utils.InterfacePriority with
            esfinge.querybuilder.core_tests.utils.LowPriority,
            esfinge.querybuilder.core_tests.utils.MediumPriority,
            esfinge.querybuilder.core_tests.utils.HighPriority;

    provides esfinge.querybuilder.core_tests.utils.TestInterface with
            esfinge.querybuilder.core_tests.utils.TestString,
            esfinge.querybuilder.core_tests.utils.TestInt;


}
