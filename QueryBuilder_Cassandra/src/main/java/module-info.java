module querybuilder.cassandra {
    requires transitive querybuilder.core;
    requires cassandra.driver.core;
    requires cassandra.driver.mapping;
    requires java.logging;
    requires jackson.databind;

    exports ef.qb.cassandra;
    exports ef.qb.cassandra.cassandrautils;
    exports ef.qb.cassandra.config;
    exports ef.qb.cassandra.entity;
    exports ef.qb.cassandra.exceptions;
    exports ef.qb.cassandra.querybuilding;
    exports ef.qb.cassandra.querybuilding.resultsprocessing;
    exports ef.qb.cassandra.querybuilding.resultsprocessing.join;
    exports ef.qb.cassandra.querybuilding.resultsprocessing.ordering;
    exports ef.qb.cassandra.querybuilding.resultsprocessing.secondaryquery;
    exports ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison;
    exports ef.qb.cassandra.reflection;
    exports ef.qb.cassandra.validation;

    opens ef.qb.cassandra;
    opens ef.qb.cassandra.cassandrautils;
    opens ef.qb.cassandra.config;
    opens ef.qb.cassandra.entity;
    opens ef.qb.cassandra.exceptions;
    opens ef.qb.cassandra.querybuilding;
    opens ef.qb.cassandra.querybuilding.resultsprocessing;
    opens ef.qb.cassandra.querybuilding.resultsprocessing.join;
    opens ef.qb.cassandra.querybuilding.resultsprocessing.ordering;
    opens ef.qb.cassandra.querybuilding.resultsprocessing.secondaryquery;
    opens ef.qb.cassandra.querybuilding.resultsprocessing.specialcomparison;
    opens ef.qb.cassandra.reflection;
    opens ef.qb.cassandra.validation;

    uses ef.qb.cassandra.CassandraSessionProvider;
    uses ef.qb.cassandra.entity.CassandraEntity;

    provides ef.qb.core.Repository with
        ef.qb.cassandra.CassandraRepository;

    provides ef.qb.core.executor.QueryExecutor with
        ef.qb.cassandra.CassandraQueryExecutor;

    provides ef.qb.cassandra.CassandraSessionProvider with
            ef.qb.cassandra.DefaultCassandraSessionProvider;
}
