module querybuilder.core {
    requires org.apache.tomcat.jasper.el;
    requires jakarta.el;
    requires cglib;

    exports esfinge.querybuilder.core;
    exports esfinge.querybuilder.core.annotation;
    exports esfinge.querybuilder.core.exception;
    exports esfinge.querybuilder.core.executor;
    exports esfinge.querybuilder.core.methodparser;
    exports esfinge.querybuilder.core.methodparser.conditions;
    exports esfinge.querybuilder.core.methodparser.conversor;
    exports esfinge.querybuilder.core.methodparser.formater;
    exports esfinge.querybuilder.core.utils;

    opens esfinge.querybuilder.core.annotation;
    opens esfinge.querybuilder.core.exception;
    opens esfinge.querybuilder.core.executor;
    opens esfinge.querybuilder.core.methodparser;
    opens esfinge.querybuilder.core.methodparser.conditions;
    opens esfinge.querybuilder.core.methodparser.conversor;
    opens esfinge.querybuilder.core.methodparser.formater;
    opens esfinge.querybuilder.core.utils;

    uses esfinge.querybuilder.core.Repository;
    uses esfinge.querybuilder.core.methodparser.EntityClassProvider;
    uses esfinge.querybuilder.core.executor.QueryExecutor;
    uses esfinge.querybuilder.core.methodparser.conversor.FromStringConversor;
    uses esfinge.querybuilder.core.methodparser.MethodParser;
    uses esfinge.querybuilder.core.methodparser.formater.FormaterFactory;

    provides esfinge.querybuilder.core.methodparser.conversor.FromStringConversor with
            esfinge.querybuilder.core.methodparser.conversor.ToBooleanConversor,
            esfinge.querybuilder.core.methodparser.conversor.ToByteConversor,
            esfinge.querybuilder.core.methodparser.conversor.ToDoubleConversor,
            esfinge.querybuilder.core.methodparser.conversor.ToFloatConversor,
            esfinge.querybuilder.core.methodparser.conversor.ToIntConversor,
            esfinge.querybuilder.core.methodparser.conversor.ToLongConversor,
            esfinge.querybuilder.core.methodparser.conversor.ToShortConversor;

    provides esfinge.querybuilder.core.methodparser.MethodParser with
            esfinge.querybuilder.core.methodparser.DSLMethodParser,
            esfinge.querybuilder.core.methodparser.QueryObjectMethodParser;

    provides esfinge.querybuilder.core.methodparser.formater.FormaterFactory with
            esfinge.querybuilder.core.methodparser.formater.RelationalFormaterFactory;

}
