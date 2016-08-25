package org.esfinge.querybuilder.mongodb;

import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.ValidationQueryVisitor;

public class MongoDBVisitorFactory {
	
	public static QueryVisitor createQueryVisitor(QueryInfo info, Object args[]){
		return new ValidationQueryVisitor(new MongoDBQueryVisitor(info, args));
	}

}
