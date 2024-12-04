package ef.qb.neo4j;

import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.PagingAndSortingQuery;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.request.strategy.QueryStatements;

public class Neo4JQueryParameters {

    private final int DEPTH_ENTITY = 0;
    private final int DEPTH_LIST = 1;

    private String label;
    private Filters filters;
    private SortOrder sortOrder;

    public Neo4JQueryParameters(String label, Filters filters, SortOrder sortOrder) {
        this.label = label;
        this.filters = filters;
        this.sortOrder = sortOrder;
    }

    public String getLabel() {
        return label;
    }

    public Filters getFilters() {
        return filters;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public String resolveQuery(Class<?> type, Neo4jSession session) {
        var sb = new StringBuilder();
        var depth = DEPTH_LIST;
        QueryStatements<?> queryStatements = session.queryStatementsFor(type, depth);

        // Assuming filters are handled differently in version 4
        PagingAndSortingQuery query;
        if (filters.isEmpty()) {
            query = queryStatements.findByType(label, depth);
            query.setSortOrder(sortOrder);
            sb.append(query.getStatement());
        } else {
            // Ensure filters are compatible with version 4
            query = queryStatements.findByType(label, filters, depth);
            query.setSortOrder(sortOrder);
            var statement = query.getStatement();
            for (var filter : filters) {
                for (var key : filter.parameters().keySet()) {
                    var object = filter.parameters().get(key);
                    if (object instanceof String) {
                        statement = statement.replace(String.format("{ `%s` }", key), String.format("'%s'", object));
                    } else {
                        statement = statement.replace(String.format("{ `%s` }", key), object.toString());
                    }
                }
            }
            sb.append(statement);
        }

        return sb.toString();
    }

}
