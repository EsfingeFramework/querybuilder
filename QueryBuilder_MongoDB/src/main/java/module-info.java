module querybuilder.mongodb {
    requires transitive querybuilder.core;
    requires morphia;
    requires mongo.java.driver;

    exports esfinge.querybuilder.mongodb;
    exports esfinge.querybuilder.mongodb.formaters;

    opens esfinge.querybuilder.mongodb;
    opens esfinge.querybuilder.mongodb.formaters;

    uses esfinge.querybuilder.mongodb.DatastoreProvider;

    provides esfinge.querybuilder.core.Repository with
            esfinge.querybuilder.mongodb.MongoDBRepository;

    provides esfinge.querybuilder.core.executor.QueryExecutor with
            esfinge.querybuilder.mongodb.MongoDBQueryExecutor;

    provides esfinge.querybuilder.core.methodparser.EntityClassProvider with
            esfinge.querybuilder.mongodb.MongoDBEntityClassProvider;

    provides esfinge.querybuilder.core.methodparser.formater.FormaterFactory with
            esfinge.querybuilder.mongodb.formaters.MongoDBFormaterFactory;



}
