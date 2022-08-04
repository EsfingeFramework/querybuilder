package net.sf.esfinge.querybuilder.cassandra.validation;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.ValidationQueryVisitor;

public class CassandraVisitorFactory {
    public static QueryVisitor createQueryVisitor() {
        return new CassandraValidationQueryVisitor(new CassandraQueryVisitor());
    }
}
