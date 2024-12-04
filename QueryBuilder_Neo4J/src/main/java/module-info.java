module querybuilder.neo4j {
    requires transitive querybuilder.core;
    requires org.neo4j.ogm.core;
    requires org.neo4j.ogm.drivers.api;

    exports ef.qb.neo4j;
    exports ef.qb.neo4j.formaters;

    opens ef.qb.neo4j;
    opens ef.qb.neo4j.formaters;

    uses ef.qb.neo4j.DatastoreProvider;

    provides ef.qb.core.Repository with
        ef.qb.neo4j.Neo4JRepository;

    provides ef.qb.core.executor.QueryExecutor with
        ef.qb.neo4j.Neo4JQueryExecutor;

    provides ef.qb.core.methodparser.formater.FormaterFactory with
        ef.qb.neo4j.formaters.Neo4JFormaterFactory;

    provides ef.qb.neo4j.DatastoreProvider with
            ef.qb.neo4j.DefaultDatastoreProvider;
}
