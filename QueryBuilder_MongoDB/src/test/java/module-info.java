module querybuilder.mongodb_tests {
    requires querybuilder.mongodb;
    requires junit; 

    exports ef.qb.mongodb_tests;
    exports ef.qb.mongodb_tests.dynamic;

    opens ef.qb.mongodb_tests;
    opens ef.qb.mongodb_tests.dynamic;

    uses ef.qb.mongodb.DatastoreProvider;

    provides ef.qb.mongodb.DatastoreProvider with
            ef.qb.mongodb_tests.TestMongoDBDatastoreProvider;


}
