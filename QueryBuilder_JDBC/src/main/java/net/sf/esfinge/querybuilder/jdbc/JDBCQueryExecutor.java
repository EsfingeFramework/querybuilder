package net.sf.esfinge.querybuilder.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryStyle;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.methodparser.formater.FormaterFactory;
import net.sf.esfinge.querybuilder.utils.DataBaseChannel;
import net.sf.esfinge.querybuilder.utils.EntityParser;
import net.sf.esfinge.querybuilder.utils.Line;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class JDBCQueryExecutor implements QueryExecutor {

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public Object executeQuery(QueryInfo info, Object[] args) {
        var dcp = ServiceLocator.getServiceImplementation(DatabaseConnectionProvider.class);
        var c = dcp.getConnection();
        var visitor = new JDBCQueryVisitor();
        if (info.getQueryStyle() == QueryStyle.METHOD_SIGNATURE) {
            if (args != null) {
                for (var i = 0; i < args.length; i++) {
                    if (args[i] != null) {
                        var param = info.getNamedParemeters().get(i);

                        if (args[i] != null) {
                            var cp = ComparisonType.getComparisonType(param);
                            var factory = ServiceLocator.getServiceImplementation(FormaterFactory.class);
                            args[i] = factory.getFormater(cp).formatParameter(args[i]);
                        }
                    }
                }
            }

            visitor.setValuesOfQuery(args);
        } else {
            var map = ReflectionUtils.toParameterMap(args[0]);
            var parameters = info.getNamedParemeters();
            var params = new Object[map.size()];

            for (var i = 0; i < parameters.size(); i++) {
                var param = parameters.get(i);
                var value = map.get(param);

                if (value != null) {
                    var cp = ComparisonType.getComparisonType(param);
                    var factory = ServiceLocator.getServiceImplementation(FormaterFactory.class);
                    params[i] = factory.getFormater(cp).formatParameter(value);
                }
            }

            visitor.setValuesOfQuery(params);
        }

        info.visit(visitor);

        var query = visitor.getJDBCQuery();
        var channel = new DataBaseChannel(c);
        ArrayList<Line> mapResult = null;
        try {
            mapResult = channel.executeQuery(query);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        var parser = new EntityParser(info.getEntityType());
        ArrayList<Object> Result = null;

        try {
            Result = parser.parseEntity(mapResult);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception err) {
            err.printStackTrace();
        }

        if (Result != null) {
            if (info.getQueryType() == QueryType.RETRIEVE_SINGLE) {
                return Result.get(0);
            }
        }

        return Result;
    }

}
