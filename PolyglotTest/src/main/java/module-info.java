module polyglot {
    requires querybuilder.jpaone;
    requires querybuilder.mongodb;
    requires morphia;
    requires lombok;

    exports com.example.polyglot;
    exports com.example.polyglot.entities;
    exports com.example.polyglot.jpa1;
    exports com.example.polyglot.mongodb;

    opens com.example.polyglot;
    opens com.example.polyglot.entities;
    opens com.example.polyglot.jpa1;
    opens com.example.polyglot.mongodb;

    uses esfinge.querybuilder.jpa1.EntityManagerProvider;
    uses esfinge.querybuilder.mongodb.DatastoreProvider;

    provides esfinge.querybuilder.jpa1.EntityManagerProvider with
        com.example.polyglot.jpa1.PolyglotEntityManagerProvider;

    provides esfinge.querybuilder.mongodb.DatastoreProvider with
        com.example.polyglot.mongodb.PolyglotMongoDBDatastoreProvider;
}
