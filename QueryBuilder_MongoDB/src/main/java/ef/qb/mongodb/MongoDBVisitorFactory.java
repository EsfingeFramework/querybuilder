package ef.qb.mongodb;

import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.core.methodparser.ValidationQueryVisitor;

public class MongoDBVisitorFactory {

    public static QueryVisitor createQueryVisitor(QueryInfo info, Object args[]) {
        return new ValidationQueryVisitor(new MongoDBQueryVisitor(info, args));
    }

}
