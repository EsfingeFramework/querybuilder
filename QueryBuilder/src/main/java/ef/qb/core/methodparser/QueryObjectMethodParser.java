package ef.qb.core.methodparser;

import ef.qb.core.annotation.CompareToNull;
import ef.qb.core.annotation.IgnoreWhenNull;
import ef.qb.core.annotation.PageNumber;
import ef.qb.core.annotation.QueryObject;
import ef.qb.core.annotation.ServicePriority;
import ef.qb.core.annotation.VariablePageSize;
import ef.qb.core.exception.InvalidPropertyException;
import ef.qb.core.exception.InvalidPropertyTypeException;
import ef.qb.core.exception.QueryObjectException;
import ef.qb.core.exception.WrongParamNumberException;
import ef.qb.core.methodparser.conditions.NullOption;
import ef.qb.core.methodparser.conditions.SimpleCondition;
import ef.qb.core.utils.NameUtils;
import ef.qb.core.utils.ReflectionUtils;
import static ef.qb.core.utils.ReflectionUtils.containsParameterAnnotation;
import ef.qb.core.utils.StringUtils;
import java.lang.reflect.Method;
import java.util.List;

@ServicePriority(5)
public class QueryObjectMethodParser extends BasicMethodParser {

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public QueryInfo parse(Method m) {
        var words = StringUtils.splitCamelCaseName(m.getName());
        var index = new IndexCounter();
        var qi = createQueryInfo(m, words, index);
        qi.setQueryStyle(QueryStyle.QUERY_OBJECT);

        addPagination(m, qi);

        var length = m.getParameterTypes().length;
        if (ReflectionUtils.containsParameterAnnotation(m, VariablePageSize.class)) {
            length--;
        }

        if (ReflectionUtils.containsParameterAnnotation(m, PageNumber.class)) {
            length--;
        }

        if (length != 1) {
            throw new WrongParamNumberException("The method " + m.getName() + " is using @QueryObject annotation but have more than one parameter");
        }

        var parameterIndex = ReflectionUtils.getParameterAnnotationIndex(m, QueryObject.class);
        Class paramType = m.getParameterTypes()[parameterIndex];
        for (var met : paramType.getMethods()) {
            if (ReflectionUtils.isGetter(met)) {
                try {
                    var queryPropName = NameUtils.acessorToProperty(met.getName());
                    var propWords = StringUtils.splitCamelCaseName(queryPropName);
                    var counter = new IndexCounter();
                    var propName = getPropertyName(propWords, counter, qi.getEntityType());
                    var cp = ComparisonType.getComparisonType(propWords, counter.get());
                    if (cp.equals(ComparisonType.EQUALS)) {
                        cp = ReflectionUtils.getPropertyComparisonTypeByAnnotation(queryPropName, paramType);
                    }
                    var condition = new SimpleCondition();
                    condition.setName(propName);
                    condition.setOperator(cp);
                    condition.setNullOption(getPropertyNullOption(queryPropName, paramType));
                    Class propType = ReflectionUtils.getPropertyType(qi.getEntityType(), propName);
                    if (met.getReturnType() != propType) {
                        throw new InvalidPropertyTypeException("The property " + propName + " should be " + propType.getName() + " but is " + met.getReturnType());
                    }
                    qi.addCondition(condition);
                } catch (Exception e) {
                    throw new QueryObjectException(e.getMessage(), e, met, paramType);
                }
            }
        }
        addOrderBy(qi, words, index);
        return qi;
    }

    @Override
    public boolean fitParserConvention(Method m) {
        return super.fitParserConvention(m) && containsParameterAnnotation(m, QueryObject.class);
    }

    @Override
    protected boolean isPartOfEntityName(List<String> words, IndexCounter index) {
        return index.get() < words.size() && !StringUtils.matchString("order by", words, index.get()) && !termLib.hasDomainTerm(words, index.get());
    }

    public NullOption getPropertyNullOption(String propName, Class queryClass) {
        var getterName = NameUtils.propertyToGetter(propName);
        try {
            try {
                var f = queryClass.getDeclaredField(propName);
                if (f.isAnnotationPresent(CompareToNull.class)) {
                    return NullOption.COMPARE_TO_NULL;
                }
                if (f.isAnnotationPresent(IgnoreWhenNull.class)) {
                    return NullOption.IGNORE_WHEN_NULL;
                }
            } catch (NoSuchFieldException | SecurityException e) {

            }
            var m = queryClass.getMethod(getterName);
            if (m.isAnnotationPresent(CompareToNull.class)) {
                return NullOption.COMPARE_TO_NULL;
            }
            if (m.isAnnotationPresent(IgnoreWhenNull.class)) {
                return NullOption.IGNORE_WHEN_NULL;
            }

        } catch (NoSuchMethodException | SecurityException e) {
            throw new InvalidPropertyException("Cannot access property " + propName + " or method " + getterName + " on class " + queryClass.getName(), e);
        }
        return NullOption.NONE;
    }

}
