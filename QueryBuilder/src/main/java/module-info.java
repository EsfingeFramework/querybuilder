module querybuilder.core {
    requires org.apache.tomcat.jasper.el;
    requires jakarta.el;
    requires cglib;

    exports net.sf.esfinge.querybuilder;
    exports net.sf.esfinge.querybuilder.annotation;
    exports net.sf.esfinge.querybuilder.exception;
    exports net.sf.esfinge.querybuilder.executor;
    exports net.sf.esfinge.querybuilder.methodparser;
    exports net.sf.esfinge.querybuilder.methodparser.conditions;
    exports net.sf.esfinge.querybuilder.methodparser.conversor;
    exports net.sf.esfinge.querybuilder.methodparser.formater;
    exports net.sf.esfinge.querybuilder.utils;

    opens net.sf.esfinge.querybuilder.annotation;
    opens net.sf.esfinge.querybuilder.exception;
    opens net.sf.esfinge.querybuilder.executor;
    opens net.sf.esfinge.querybuilder.methodparser;
    opens net.sf.esfinge.querybuilder.methodparser.conditions;
    opens net.sf.esfinge.querybuilder.methodparser.conversor;
    opens net.sf.esfinge.querybuilder.methodparser.formater;
    opens net.sf.esfinge.querybuilder.utils;

    uses net.sf.esfinge.querybuilder.Repository;
    uses net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;
    uses net.sf.esfinge.querybuilder.executor.QueryExecutor;
    uses net.sf.esfinge.querybuilder.methodparser.conversor.FromStringConversor;
    uses net.sf.esfinge.querybuilder.methodparser.MethodParser;
    uses net.sf.esfinge.querybuilder.methodparser.formater.FormaterFactory;

    provides net.sf.esfinge.querybuilder.methodparser.conversor.FromStringConversor with
            net.sf.esfinge.querybuilder.methodparser.conversor.ToBooleanConversor,
            net.sf.esfinge.querybuilder.methodparser.conversor.ToByteConversor,
            net.sf.esfinge.querybuilder.methodparser.conversor.ToDoubleConversor,
            net.sf.esfinge.querybuilder.methodparser.conversor.ToFloatConversor,
            net.sf.esfinge.querybuilder.methodparser.conversor.ToIntConversor,
            net.sf.esfinge.querybuilder.methodparser.conversor.ToLongConversor,
            net.sf.esfinge.querybuilder.methodparser.conversor.ToShortConversor;

    provides net.sf.esfinge.querybuilder.methodparser.MethodParser with
            net.sf.esfinge.querybuilder.methodparser.DSLMethodParser,
            net.sf.esfinge.querybuilder.methodparser.QueryObjectMethodParser;

    provides net.sf.esfinge.querybuilder.methodparser.formater.FormaterFactory with
            net.sf.esfinge.querybuilder.methodparser.formater.RelationalFormaterFactory;

}
