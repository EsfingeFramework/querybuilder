module querybuilder.jpaone_tests {
    requires querybuilder.jpaone;
    requires junit.dep;

    exports ef.qb.jpa1_tests;

    opens ef.qb.jpa1_tests;

    provides ef.qb.jpa1.EntityManagerProvider with
            ef.qb.jpa1_tests.TestEntityManagerProvider;


}
