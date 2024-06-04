module querybuilder.jpaone_tests {
    requires querybuilder.jpaone;
    requires junit.dep;

    exports esfinge.querybuilder.jpa1_tests;

    opens esfinge.querybuilder.jpa1_tests;

    provides esfinge.querybuilder.jpa1.EntityManagerProvider with
            esfinge.querybuilder.jpa1_tests.TestEntityManagerProvider;


}
