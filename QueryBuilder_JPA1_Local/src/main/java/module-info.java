module querybuilder.jpaone_local {
    requires transitive querybuilder.core;
    requires querybuilder.jpaone;
    requires java.persistence;

    exports ef.qb.jpa1_local;

    opens ef.qb.jpa1_local;

    provides ef.qb.core.Repository with
        ef.qb.jpa1_local.JPALocalRepository;
}
