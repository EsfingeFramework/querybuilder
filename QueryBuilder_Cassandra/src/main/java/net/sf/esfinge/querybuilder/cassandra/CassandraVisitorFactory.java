package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.ValidationQueryVisitor;

public class CassandraVisitorFactory {
    public static QueryVisitor createQueryVisitor(){
        return new ValidationQueryVisitor(new CassandraQueryVisitor());
    }
}
