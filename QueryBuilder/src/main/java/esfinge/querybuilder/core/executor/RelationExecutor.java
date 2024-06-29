package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.methodparser.QueryInfo;
import java.lang.reflect.Field;

public interface RelationExecutor {

    boolean supports(Field field);

    Object correlate(Field field, QueryInfo priInfo, Object priResult);
}
