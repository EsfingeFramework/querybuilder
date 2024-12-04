module querybuilder.neo4j_tests {
    requires querybuilder.neo4j;
    requires junit; 

    exports ef.qb.neo4j_tests;
    exports ef.qb.neo4j_tests.domain;
    exports ef.qb.neo4j_tests.dynamic;

    opens ef.qb.neo4j_tests;
    opens ef.qb.neo4j_tests.domain;
    opens ef.qb.neo4j_tests.dynamic;

    uses ef.qb.neo4j.DatastoreProvider;

    provides ef.qb.neo4j.DatastoreProvider with
            ef.qb.neo4j_tests.TestDatastoreProvider;


}
