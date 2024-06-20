package esfinge.querybuilder.mongodb;

import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryVisitor;
import esfinge.querybuilder.core.methodparser.ValidationQueryVisitor;

public class MongoDBVisitorFactory {

    public static QueryVisitor createQueryVisitor(QueryInfo info, Object args[]) {
        return new ValidationQueryVisitor(new MongoDBQueryVisitor(info, args));
    }

}
