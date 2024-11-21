module querybuilder.cassandra_tests {
    requires transitive querybuilder.cassandra;
    requires org.junit.jupiter.api;
    requires junit;

    exports ef.qb.cassandra_tests.testresources;
    exports ef.qb.cassandra_tests.unit;
    exports ef.qb.cassandra_tests.unit.config;
    exports ef.qb.cassandra_tests.unit.querybuilding;
    exports ef.qb.cassandra_tests.unit.querybuilding.resultsprocessing;
    exports ef.qb.cassandra_tests.unit.queryvisitor;
    exports ef.qb.cassandra_tests.unit.reflection;

    opens ef.qb.cassandra_tests.testresources;
    opens ef.qb.cassandra_tests.unit;
    opens ef.qb.cassandra_tests.unit.config;
    opens ef.qb.cassandra_tests.unit.querybuilding;
    opens ef.qb.cassandra_tests.unit.querybuilding.resultsprocessing;
    opens ef.qb.cassandra_tests.unit.queryvisitor;
    opens ef.qb.cassandra_tests.unit.reflection;

    uses ef.qb.cassandra.entity.CassandraEntity;

    provides ef.qb.cassandra.entity.CassandraEntity with
            ef.qb.cassandra_tests.testresources.Address,
            ef.qb.cassandra_tests.testresources.Person;


}
