package ef.qb.core.executor;

import ef.qb.core.methodparser.QueryInfo;
import java.lang.reflect.Field;

public interface RelationProcessor {

    boolean supports(Field field);

    void configure(Field field);

    Object correlate(Field field, QueryInfo priInfo, Object priResult);
}
