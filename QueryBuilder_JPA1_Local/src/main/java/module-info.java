module querybuilder.jpaone_local {
    requires transitive querybuilder.core;
    requires querybuilder.jpaone;
    requires java.persistence;

    exports esfinge.querybuilder.jpa1_local;

    opens esfinge.querybuilder.jpa1_local;

    provides esfinge.querybuilder.core.Repository with
        esfinge.querybuilder.jpa1_local.JPALocalRepository;
}
