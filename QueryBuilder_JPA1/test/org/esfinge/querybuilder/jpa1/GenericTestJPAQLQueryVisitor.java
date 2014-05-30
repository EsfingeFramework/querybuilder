package org.esfinge.querybuilder.jpa1;

import org.esfinge.querybuilder.methodparser.QueryVisitor;
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