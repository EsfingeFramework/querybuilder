module querybuilder.jpa_one_tests {
    requires querybuilder.jpa_one;
    requires junit.dep;

    exports esfinge.querybuilder.jpa1_tests;
    exports esfinge.querybuilder.jpa1_tests.resources;

    opens esfinge.querybuilder.jpa1_tests;
    opens esfinge.querybuilder.jpa1_tests.resources;

    provides esfinge.querybuilder.jpa1.EntityManagerProvider with
            esfinge.querybuilder.jpa1_tests.TestEntityManagerProvider;
}
