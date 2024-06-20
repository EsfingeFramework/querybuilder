package esfinge.querybuilder.jpa1_tests;

import esfinge.querybuilder.core.methodparser.QueryVisitor;
import esfinge.querybuilder.jpa1.JPAVisitorFactory;
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
