package org.esfinge.querybuilder.neo4j;

import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.ValidationQueryVisitor;

public class Neo4JVisitorFactory {
	
	public static QueryVisitor createQueryVisitor(QueryInfo info, Object args[]){
		return new ValidationQueryVisitor(new Neo4JQueryVisitor(info, args));
	}

}
