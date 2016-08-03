package org.esfinge.querybuilder.neo4j;

import org.esfinge.querybuilder.methodparser.QueryInfo;
import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.esfinge.querybuilder.methodparser.ValidationQueryVisitor;

public class Neo4JVisitorFactory {
	
	public static QueryVisitor createQueryVisitor(QueryInfo info, Object args[]){
		return new ValidationQueryVisitor(new Neo4JQueryVisitor(info, args));
	}

}
