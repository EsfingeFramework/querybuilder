module querybuilder.jpaone {
    requires transitive querybuilder.core;
    requires java.persistence;

    exports ef.qb.jpa1;

    opens ef.qb.jpa1;

    uses ef.qb.jpa1.EntityManagerProvider;

    provides ef.qb.core.Repository with
        ef.qb.jpa1.JPARepository;

    provides ef.qb.core.executor.QueryExecutor with
        ef.qb.jpa1.JPAQueryExecutor;

    provides ef.qb.jpa1.EntityManagerProvider with
            ef.qb.jpa1.DefaultEntityManagerProvider;
}
