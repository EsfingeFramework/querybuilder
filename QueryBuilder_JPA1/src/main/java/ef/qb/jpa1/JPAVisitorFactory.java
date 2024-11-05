package ef.qb.jpa1;

import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.core.methodparser.ValidationQueryVisitor;

public class JPAVisitorFactory {

    public static QueryVisitor createQueryVisitor() {
        return new ValidationQueryVisitor(new JPAQLQueryVisitor());
    }

}
