module polyglot {
    requires querybuilder.jpaone;
    requires querybuilder.jpaone_local;
    requires querybuilder.mongodb;
    requires morphia.core;
    requires lombok;

    exports com.example.polyglot;
    exports org.esfinge.virtuallab.demo.polyglot;
    exports com.example.polyglot.jpa1;
    exports com.example.polyglot.mongodb;

    opens com.example.polyglot;
    opens org.esfinge.virtuallab.demo.polyglot;
    opens com.example.polyglot.jpa1;
    opens com.example.polyglot.mongodb;

    uses ef.qb.jpa1.EntityManagerProvider;
    uses ef.qb.mongodb.DatastoreProvider;

    provides ef.qb.jpa1.EntityManagerProvider with
        com.example.polyglot.jpa1.PolyglotEntityManagerProvider;

    provides ef.qb.mongodb.DatastoreProvider with
        com.example.polyglot.mongodb.PolyglotMongoDBDatastoreProvider;
}
