package net.sf.esfinge.querybuilder.methodparser;

import java.lang.reflect.Method;
import java.util.List;
import net.sf.esfinge.querybuilder.annotation.CompareToNull;
import net.sf.esfinge.querybuilder.annotation.IgnoreWhenNull;
import net.sf.esfinge.querybuilder.annotation.PageNumber;
import net.sf.esfinge.querybuilder.annotation.QueryObject;
import net.sf.esfinge.querybuilder.annotation.ServicePriority;
import net.sf.esfinge.querybuilder.annotation.VariablePageSize;
import net.sf.esfinge.querybuilder.exception.InvalidPropertyException;
import net.sf.esfinge.querybuilder.exception.InvalidPropertyTypeException;
import net.sf.esfinge.querybuilder.exception.QueryObjectException;
import net.sf.esfinge.querybuilder.exception.WrongParamNumberException;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;
import net.sf.esfinge.querybuilder.methodparser.conditions.SimpleCondition;
import net.sf.esfinge.querybuilder.utils.NameUtils;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;
import static net.sf.esfinge.querybuilder.utils.ReflectionUtils.containsParameterAnnotation;
import net.sf.esfinge.querybuilder.utils.StringUtils;

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
