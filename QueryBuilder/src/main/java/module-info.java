module querybuilder.core {
    requires org.apache.tomcat.jasper.el;
    requires static cglib.nodep;

    exports ef.qb.core;
    exports ef.qb.core.annotation;
    exports ef.qb.core.exception;
    exports ef.qb.core.executor;
    exports ef.qb.core.methodparser;
    exports ef.qb.core.methodparser.conditions;
    exports ef.qb.core.methodparser.conversor;
    exports ef.qb.core.methodparser.formater;
    exports ef.qb.core.repository;
    exports ef.qb.core.utils;

    opens ef.qb.core.annotation;
    opens ef.qb.core.exception;
    opens ef.qb.core.executor;
    opens ef.qb.core.methodparser;
    opens ef.qb.core.methodparser.conditions;
    opens ef.qb.core.methodparser.conversor;
    opens ef.qb.core.methodparser.formater;
    opens ef.qb.core.repository;
    opens ef.qb.core.utils;

    uses ef.qb.core.Repository;
    uses ef.qb.core.executor.QueryExecutor;
    uses ef.qb.core.methodparser.conversor.FromStringConversor;
    uses ef.qb.core.methodparser.MethodParser;
    uses ef.qb.core.methodparser.formater.FormaterFactory;

    provides ef.qb.core.methodparser.conversor.FromStringConversor with
            ef.qb.core.methodparser.conversor.ToBooleanConversor,
            ef.qb.core.methodparser.conversor.ToByteConversor,
            ef.qb.core.methodparser.conversor.ToDoubleConversor,
            ef.qb.core.methodparser.conversor.ToFloatConversor,
            ef.qb.core.methodparser.conversor.ToIntConversor,
            ef.qb.core.methodparser.conversor.ToLongConversor,
            ef.qb.core.methodparser.conversor.ToShortConversor;

    provides ef.qb.core.methodparser.MethodParser with
            ef.qb.core.methodparser.DSLMethodParser,
            ef.qb.core.methodparser.QueryObjectMethodParser;

    provides ef.qb.core.methodparser.formater.FormaterFactory with
            ef.qb.core.methodparser.formater.RelationalFormaterFactory;

}
