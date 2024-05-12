module querybuilder.jpa1 {
    requires org.apache.tomcat.jasper.el;
    requires java.persistence;
    requires querybuilder.core;

    exports esfinge.querybuilder.jpa1;

    opens esfinge.querybuilder.jpa1;

    provides esfinge.querybuilder.core.executor.QueryExecutor with
            esfinge.querybuilder.jpa1.JPAQueryExecutor;

    provides esfinge.querybuilder.core.methodparser.EntityClassProvider with
            esfinge.querybuilder.jpa1.JPAEntityClassProvider;

    provides esfinge.querybuilder.core.Repository with
            esfinge.querybuilder.jpa1.JPARepository;

}
