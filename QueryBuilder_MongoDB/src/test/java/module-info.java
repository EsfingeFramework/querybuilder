module querybuilder.mongodb_tests {
    requires querybuilder.mongodb;
    requires junit;

    exports esfinge.querybuilder.mongodb_tests;
    exports esfinge.querybuilder.mongodb_tests.dynamic;

    opens esfinge.querybuilder.mongodb_tests;
    opens esfinge.querybuilder.mongodb_tests.dynamic;

    uses esfinge.querybuilder.mongodb.DatastoreProvider;

    provides esfinge.querybuilder.mongodb.DatastoreProvider with
            esfinge.querybuilder.mongodb_tests.TestMongoDBDatastoreProvider; 


}
