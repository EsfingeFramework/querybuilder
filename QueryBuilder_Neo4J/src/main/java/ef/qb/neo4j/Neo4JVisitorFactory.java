package ef.qb.neo4j;

import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.core.methodparser.ValidationQueryVisitor;

public class Neo4JVisitorFactory {

    public static QueryVisitor createQueryVisitor(QueryInfo info, Object args[]) {
        return new ValidationQueryVisitor(new Neo4JQueryVisitor(info, args));
    }

}
