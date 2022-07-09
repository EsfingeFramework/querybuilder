package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.QueryBuildingUtilities;
import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;

public class CassandraQueryExecutor implements QueryExecutor {

    @Override
    public Object executeQuery(QueryInfo queryInfo, Object[] args) {
        QueryVisitor visitor = CassandraVisitorFactory.createQueryVisitor();
        queryInfo.visit(visitor);
        QueryRepresentation qr = visitor.getQueryRepresentation();

        String query = qr.getQuery().toString();

        if (args != null) {
            query = QueryBuildingUtilities.replaceQueryArgs(query,args);
        }

        System.out.println(query);

        //System.out.println("Executing query...");

        //printQueryInfo(queryInfo);

        System.out.print("Args: ");
        if (args != null) {
            for (Object o : args)
                System.out.print(o + " ");
        }
        System.out.println();
        //System.out.println("END OF QUERY");

        return null;
    }


    private void printQueryInfo(QueryInfo info) {
        System.out.println("entityName: " + info.getEntityName());
        System.out.println("entityType: " + info.getEntityType().getSimpleName());
        System.out.println("queryType: " + info.getQueryType().name());
        System.out.println("*** QueryCondition ***");
        System.out.println("ParameterSize: " + info.getCondition().getParameterSize());

        System.out.print("ParameterNames: ");
        info.getCondition().getParameterNames().forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.print("MethodParameterNames: ");
        info.getCondition().getMethodParameterNames().forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.print("ParameterFormatters: ");
        info.getCondition().getParameterFormatters().forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.print("MethodParameterProps: ");
        info.getCondition().getMethodParameterProps().forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.println("***************************************************************");

        System.out.println("QueryStyle: " + info.getQueryStyle().name());
    }

    /*
    private String entityName;
	private Class entityType;
	private QueryType queryType;
	private QueryCondition condition = new NullCondition();
	private List<QueryOrder> order = new ArrayList<QueryOrder>();
	private QueryStyle queryStyle;
     */
}
