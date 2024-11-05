package ef.qb.core.methodparser;

import java.util.Map;
import java.util.Set;

public interface QueryRepresentation {

    boolean isDynamic();

    Object getQuery();

    Object getQuery(Map<String, Object> params);

    Set<String> getFixParameters();

    Object getFixParameterValue(String param);

}
