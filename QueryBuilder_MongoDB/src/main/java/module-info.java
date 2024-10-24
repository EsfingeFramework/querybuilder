module querybuilder.mongodb {
    requires transitive querybuilder.core;
    requires morphia.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;

    exports esfinge.querybuilder.mongodb;
    exports esfinge.querybuilder.mongodb.formaters;

    opens esfinge.querybuilder.mongodb;
    opens esfinge.querybuilder.mongodb.formaters;

    uses esfinge.querybuilder.mongodb.DatastoreProvider;

    provides esfinge.querybuilder.core.Repository with
        esfinge.querybuilder.mongodb.MongoDBRepository;

    provides esfinge.querybuilder.core.executor.QueryExecutor with
        esfinge.querybuilder.mongodb.MongoDBQueryExecutor;

    provides esfinge.querybuilder.core.methodparser.formater.FormaterFactory with
        esfinge.querybuilder.mongodb.formaters.MongoDBFormaterFactory;
}
