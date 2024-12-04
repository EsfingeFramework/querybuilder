module querybuilder.mongodb {
    requires transitive querybuilder.core;
    requires morphia.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;

    exports ef.qb.mongodb;
    exports ef.qb.mongodb.formaters;

    opens ef.qb.mongodb;
    opens ef.qb.mongodb.formaters;

    uses ef.qb.mongodb.DatastoreProvider;

    provides ef.qb.core.Repository with
        ef.qb.mongodb.MongoDBRepository;

    provides ef.qb.core.executor.QueryExecutor with
        ef.qb.mongodb.MongoDBQueryExecutor;

    provides ef.qb.core.methodparser.formater.FormaterFactory with
        ef.qb.mongodb.formaters.MongoDBFormaterFactory;

     provides ef.qb.mongodb.DatastoreProvider with
            ef.qb.mongodb.DefaultDatastoreProvider;
}
