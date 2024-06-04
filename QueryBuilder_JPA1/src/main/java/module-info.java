module querybuilder.jpaone {
    requires transitive querybuilder.core;
    requires java.persistence;

    exports esfinge.querybuilder.jpa1;

    opens esfinge.querybuilder.jpa1;

    uses esfinge.querybuilder.jpa1.EntityManagerProvider;

    provides esfinge.querybuilder.core.Repository with
        esfinge.querybuilder.jpa1.JPARepository;

    provides esfinge.querybuilder.core.executor.QueryExecutor with
        esfinge.querybuilder.jpa1.JPAQueryExecutor;
}
