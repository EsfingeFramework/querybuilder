package org.esfinge.querybuilder.mongodb;

import org.esfinge.querybuilder.methodparser.QueryInfo;
import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.esfinge.querybuilder.methodparser.ValidationQueryVisitor;

public class MongoDBVisitorFactory {
	
	public static QueryVisitor createQueryVisitor(QueryInfo info, Object args[]){
		return new ValidationQueryVisitor(new MongoDBQueryVisitor(info, args));
	}

}
