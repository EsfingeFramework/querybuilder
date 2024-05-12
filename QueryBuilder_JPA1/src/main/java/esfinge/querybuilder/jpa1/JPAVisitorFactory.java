package esfinge.querybuilder.jpa1;

import esfinge.querybuilder.core.methodparser.QueryVisitor;
import esfinge.querybuilder.core.methodparser.ValidationQueryVisitor;

public class JPAVisitorFactory {

    public static QueryVisitor createQueryVisitor() {
        return new ValidationQueryVisitor(new JPAQLQueryVisitor());
    }

}
