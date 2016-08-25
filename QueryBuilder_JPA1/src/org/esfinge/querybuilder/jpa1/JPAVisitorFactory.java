package org.esfinge.querybuilder.jpa1;

import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.ValidationQueryVisitor;

public class JPAVisitorFactory {
	
	public static QueryVisitor createQueryVisitor(){
		return new ValidationQueryVisitor(new JPAQLQueryVisitor());
	}

}
