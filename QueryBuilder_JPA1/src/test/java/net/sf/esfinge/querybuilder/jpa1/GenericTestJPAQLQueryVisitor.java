package net.sf.esfinge.querybuilder.jpa1;

import org.junit.Before;

import net.sf.esfinge.querybuilder.jpa1.JPAVisitorFactory;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;

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