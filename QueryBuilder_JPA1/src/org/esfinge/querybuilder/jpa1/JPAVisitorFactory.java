package org.esfinge.querybuilder.jpa1;

import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.esfinge.querybuilder.methodparser.ValidationQueryVisitor;

public class JPAVisitorFactory {
	
	public static QueryVisitor createQueryVisitor(){
		return new ValidationQueryVisitor(new JPAQLQueryVisitor());
	}

}
