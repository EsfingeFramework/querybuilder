package ef.qb.core.utils;

import ef.qb.core.annotation.PolyglotJoin;
import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.QueryStyle;
import static ef.qb.core.methodparser.QueryType.RETRIEVE_LIST;
import ef.qb.core.methodparser.conditions.SimpleCondition;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Objects;

public class QueryUtils {

    public static QueryInfo createSimpleQueryInfo(Field field, String attribute) {
        var fieldType = getFieldGenericType(field);
        var info = new QueryInfo();
        info.setQueryType(RETRIEVE_LIST);
        info.setEntityType(fieldType);
        info.setEntityName(fieldType.getSimpleName());
        var condition = new SimpleCondition();
        condition.setName(attribute);
        info.setQueryStyle(QueryStyle.METHOD_SIGNATURE);
        info.addCondition(condition);
        return info;
    }

    public static Class<?> getFieldGenericType(Field field) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            return (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        }
        return field.getType();
    }

    public static PolyglotJoin validateAndGetJoinAnnotation(Field field) {
        return Objects.requireNonNull(field.getAnnotation(PolyglotJoin.class), "@PolyglotJoin is needed on a field that has a polyglot annotation relation.");
    }

}
