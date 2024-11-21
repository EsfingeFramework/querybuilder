package ef.qb.cassandra.validation;

import ef.qb.core.methodparser.QueryVisitor;

public class CassandraVisitorFactory {

    public static QueryVisitor createQueryVisitor() {
        return new CassandraValidationQueryVisitor(new CassandraChainQueryVisitor(0));
    }
}
