module querybuilder.core.core_tests {
    requires org.apache.tomcat.jasper.el;
    requires jakarta.el;
    requires querybuilder.core;
    requires junit.dep;

    exports net.sf.esfinge.querybuilder.core_tests;
    exports net.sf.esfinge.querybuilder.core_tests.methodparser;
    exports net.sf.esfinge.querybuilder.core_tests.methodparser.conversor;
    exports net.sf.esfinge.querybuilder.core_tests.utils;

    opens net.sf.esfinge.querybuilder.core_tests;
    opens net.sf.esfinge.querybuilder.core_tests.methodparser;
    opens net.sf.esfinge.querybuilder.core_tests.methodparser.conversor;
    opens net.sf.esfinge.querybuilder.core_tests.utils;

    uses net.sf.esfinge.querybuilder.core_tests.utils.InterfacePriority;
    uses net.sf.esfinge.querybuilder.core_tests.utils.TestInterface;

    uses net.sf.esfinge.querybuilder.Repository;
    uses net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;
    uses net.sf.esfinge.querybuilder.executor.QueryExecutor;
    uses net.sf.esfinge.querybuilder.methodparser.conversor.FromStringConversor;
    uses net.sf.esfinge.querybuilder.methodparser.MethodParser;
    uses net.sf.esfinge.querybuilder.methodparser.formater.FormaterFactory;

    provides net.sf.esfinge.querybuilder.Repository with
            net.sf.esfinge.querybuilder.core_tests.DummyRepository;

    provides net.sf.esfinge.querybuilder.executor.QueryExecutor with
            net.sf.esfinge.querybuilder.core_tests.DummyQueryExecutor;

    provides net.sf.esfinge.querybuilder.methodparser.EntityClassProvider with
            net.sf.esfinge.querybuilder.core_tests.DummyEntityClassProvider;

    provides net.sf.esfinge.querybuilder.core_tests.utils.InterfacePriority with
            net.sf.esfinge.querybuilder.core_tests.utils.LowPriority,
            net.sf.esfinge.querybuilder.core_tests.utils.MediumPriority,
            net.sf.esfinge.querybuilder.core_tests.utils.HighPriority;

    provides net.sf.esfinge.querybuilder.core_tests.utils.TestInterface with
            net.sf.esfinge.querybuilder.core_tests.utils.TestString,
            net.sf.esfinge.querybuilder.core_tests.utils.TestInt;


}
