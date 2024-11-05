package ef.qb.core.methodparser;

import java.lang.reflect.Method;
import java.util.List;
import ef.qb.core.annotation.CompareToNull;
import ef.qb.core.annotation.ComparisonOperator;
import ef.qb.core.annotation.IgnoreWhenNull;
import ef.qb.core.annotation.PageNumber;
import ef.qb.core.annotation.ServicePriority;
import ef.qb.core.annotation.VariablePageSize;
import ef.qb.core.exception.InvalidPropertyException;
import ef.qb.core.exception.InvalidPropertyTypeException;
import ef.qb.core.exception.WrongParamNumberException;
import ef.qb.core.methodparser.conditions.CompositeCondition;
import ef.qb.core.methodparser.conditions.NullCondition;
import ef.qb.core.methodparser.conditions.NullOption;
import ef.qb.core.methodparser.conditions.QueryCondition;
import ef.qb.core.methodparser.conditions.SimpleCondition;
import ef.qb.core.utils.ReflectionUtils;
import ef.qb.core.utils.StringUtils;

@ServicePriority(1)
public class DSLMethodParser extends BasicMethodParser {

    @Override
    public QueryInfo parse(Method m) {
        var words = StringUtils.splitCamelCaseName(m.getName());
        var index = new IndexCounter();
        var qi = createQueryInfo(m, words, index);
        qi.setQueryStyle(QueryStyle.METHOD_SIGNATURE);

        var qc = setParameters(m, qi, words, index, 0);
        qi.addCondition(qc);
        addOrderBy(qi, words, index);
        addPagination(m, qi);
        validateParameters(m, qi);

        return qi;
    }

    private void validateParameters(Method m, QueryInfo qi) {
        Class<?> entityClass = qi.getEntityType();

        var length = m.getParameterTypes().length;
        if (ReflectionUtils.containsParameterAnnotation(m, VariablePageSize.class)) {
            length--;
        }

        if (ReflectionUtils.containsParameterAnnotation(m, PageNumber.class)) {
            length--;
        }

        if (length != qi.getCondition().getParameterSize()) {
            throw new WrongParamNumberException("The method " + m.getName() + " should have " + qi.getCondition().getParameterSize() + " parameters");
        }

        var paramNames = qi.getCondition().getMethodParameterProps();
        for (var i = 0; i < length; i++) {
            var propertyType = ReflectionUtils.getPropertyType(entityClass, paramNames.get(i));
            if (propertyType != m.getParameterTypes()[i]) {
                throw new InvalidPropertyTypeException("The parameter " + i + " should be " + propertyType.getName() + " but is " + m.getParameterTypes()[i].getName());
            }
        }
    }

    private QueryCondition setParameters(Method m, QueryInfo qi, List<String> words, IndexCounter index, int methodParamIndex) {
        var entityClass = qi.getEntityType();

        if (index.get() >= words.size() || StringUtils.matchString("order by", words, index.get())) {
            return new NullCondition();
        }

        index.increment();
        var param = new SimpleCondition();

        if (index.get() < words.size()) {
            var paramName = getPropertyName(words, index, entityClass);
            @SuppressWarnings("UnusedAssignment")
            String first = null;
            String second = null;
            if (paramName.contains(".")) {
                var props = paramName.split("\\.");
                first = props[0];
                second = props[1];
            } else {
                first = paramName;
            }

            Class<?> returnType = null;
            try {
                returnType = entityClass.getMethod("get" + StringUtils.firstCharWithUppercase(first)).getReturnType();
            } catch (NoSuchMethodException | SecurityException e) {
                throw new InvalidPropertyException(first + " not found in " + entityClass.getName());
            }
            if (second != null) {
                try {
                    returnType.getMethod("get" + StringUtils.firstCharWithUppercase(second));
                } catch (NoSuchMethodException | SecurityException e) {
                    throw new InvalidPropertyException(second + " not found in type of " + first + ", " + returnType.getName());
                }
            }

            param.setName(paramName);
            extractComparisonType(m, words, methodParamIndex, index, param);
        }
        /* RContribution: */
        setNullOption(m, methodParamIndex, param);
        if (index.get() < words.size() && isConector(words.get(index.get()))) {
            return new CompositeCondition(param, words.get(index.get()), setParameters(m, qi, words, index, methodParamIndex + 1));
        } else {
            return param;
        }
    }

    protected void setNullOption(Method m, int methodParamIndex, SimpleCondition param) {
        if (ReflectionUtils.containsParameterAnnotation(m, methodParamIndex, IgnoreWhenNull.class)) {
            param.setNullOption(NullOption.IGNORE_WHEN_NULL);
        } else if (ReflectionUtils.containsParameterAnnotation(m, methodParamIndex, CompareToNull.class)) {
            param.setNullOption(NullOption.COMPARE_TO_NULL);
        }
    }

    private void extractComparisonType(Method m, List<String> words, int methodParamIndex, IndexCounter index, SimpleCondition param) {
        var cp = ComparisonType.getComparisonType(words, index.get());
        index.add(cp.wordNumber());
        param.setOperator(cp);
        if (m.getParameterTypes().length <= methodParamIndex) {
            throw new WrongParamNumberException("Less parameters than needed");
        }
        if (cp == ComparisonType.EQUALS && m.getParameterTypes().length > 0) {
            for (var an : m.getParameterAnnotations()[methodParamIndex]) {
                if (ReflectionUtils.isComparisonOperator(an.annotationType())) {
                    param.setOperator(an.annotationType().getAnnotation(ComparisonOperator.class).value());
                }
            }
        }
    }

}
