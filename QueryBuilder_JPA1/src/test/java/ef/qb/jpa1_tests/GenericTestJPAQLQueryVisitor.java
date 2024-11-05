package ef.qb.jpa1_tests;

import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.jpa1.JPAVisitorFactory;
import org.junit.Before;

public class GenericTestJPAQLQueryVisitor {

    protected QueryVisitor visitor;

    public GenericTestJPAQLQueryVisitor() {
        super();
    }

    @Before
    public void initialize() {
        visitor = JPAVisitorFactory.createQueryVisitor();
    }

}
