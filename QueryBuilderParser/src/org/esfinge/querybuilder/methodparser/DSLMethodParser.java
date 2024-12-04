package org.esfinge.querybuilder.methodparser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.esfinge.querybuilder.annotation.CompareToNull;
import org.esfinge.querybuilder.annotation.ComparisonOperator;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;
import org.esfinge.querybuilder.annotation.ServicePriority;
import org.esfinge.querybuilder.exception.InvalidPropertyException;
import org.esfinge.querybuilder.exception.InvalidPropertyTypeException;
import org.esfinge.querybuilder.exception.WrongParamNumberException;
import org.esfinge.querybuilder.methodparser.conditions.CompositeCondition;
import org.esfinge.querybuilder.methodparser.conditions.NullCondition;
import org.esfinge.querybuilder.methodparser.conditions.NullOption;
import org.esfinge.querybuilder.methodparser.conditions.QueryCondition;
import org.esfinge.querybuilder.methodparser.conditions.SimpleCondition;
import org.esfinge.querybuilder.utils.ReflectionUtils;
import org.esfinge.querybuilder.utils.ServiceLocator;
import org.esfinge.querybuilder.utils.StringUtils;

@ServicePriority(1)
public class DSLMethodParser extends BasicMethodParser {
	
	@Override
	public QueryInfo parse(Method m) {
		List<String> words = StringUtils.splitCamelCaseName(m.getName());
		IndexCounter index = new IndexCounter();
		
		QueryInfo qi = createQueryInfo(m, words, index);
		qi.setQueryStyle(QueryStyle.METHOD_SIGNATURE);
		
		QueryCondition qc = setParameters(m, qi, words, index, 0);
		qi.addCondition(qc);
		addOrderBy(qi, words, index);
		validateParameters(m, qi);
		
		return qi;
	}


	private void validateParameters(Method m, QueryInfo qi) {
		Class<?> entityClass = qi.getEntityType();
		if(m.getParameterTypes().length != qi.getCondition().getParameterSize()){
			throw new WrongParamNumberException("The method "+m.getName()+" should have "+qi.getCondition().getParameterSize()+" parameters");
		}
		
		List<String> paramNames = qi.getCondition().getMethodParameterProps();
		for(int i = 0; i<m.getParameterTypes().length; i++){
			Class<?> propertyType = ReflectionUtils.getPropertyType(entityClass, paramNames.get(i));
			if(propertyType != m.getParameterTypes()[i]){
				throw new InvalidPropertyTypeException("The parameter "+i+" should be "+propertyType.getName()+" but is "+ m.getParameterTypes()[i].getName());
			}
		}
	}

	private QueryCondition setParameters(Method m, QueryInfo qi, List<String> words,
			IndexCounter index, int methodParamIndex) {
		Class entityClass = qi.getEntityType();
		
		if(index.get() >= words.size() || StringUtils.matchString("order by", words, index.get())){
			return new NullCondition();
		}

		index.increment();
		SimpleCondition param = new SimpleCondition();
		
		if(index.get() < words.size()){
			String paramName = getPropertyName(words, index, entityClass);
			String first = null;
			String second = null;
			if(paramName.contains(".")){
				String[] props = paramName.split("\\.");	
				first = props[0];
				second = props[1];
			}else{
				first = paramName;
			}
			
			Class<?> returnType = null;
			try{
				returnType = entityClass.getMethod("get"+StringUtils.firstCharWithUppercase(first)).getReturnType();
			}catch (Exception e){
				throw new InvalidPropertyException(first + " not found in "+entityClass.getName());
			}
			if(second != null){
				try{
					returnType.getMethod("get"+StringUtils.firstCharWithUppercase(second));
				}catch (Exception e){
					throw new InvalidPropertyException(second + " not found in type of "+first+", "+returnType.getName());
				}
			}

			param.setName(paramName);
			extractComparisonType(m, words, methodParamIndex, index, param);
		}
		/* RContribution: */
		setNullOption(m, methodParamIndex, param);
		if(index.get() < words.size() && isConector(words.get(index.get()))){
			return new CompositeCondition(param, words.get(index.get()), setParameters(m, qi, words, index, methodParamIndex+1));
		}else{
			return param;
		}
	}

	protected void setNullOption(Method m, int methodParamIndex,
			SimpleCondition param) {
		if(ReflectionUtils.containsParameterAnnotation(m, methodParamIndex, IgnoreWhenNull.class)){
			param.setNullOption(NullOption.IGNORE_WHEN_NULL);
		}else if(ReflectionUtils.containsParameterAnnotation(m, methodParamIndex, CompareToNull.class)){
			param.setNullOption(NullOption.COMPARE_TO_NULL);
		}
	}

	private void extractComparisonType(Method m, List<String> words,
			int methodParamIndex, IndexCounter index, SimpleCondition param) {
		ComparisonType cp = ComparisonType.getComparisonType(words, index.get());
		index.add(cp.wordNumber());
		param.setOperator(cp);
		if(m.getParameterTypes().length <= methodParamIndex){
			throw new WrongParamNumberException("Less parameters than needed");
		}
		if(cp == ComparisonType.EQUALS && m.getParameterTypes().length > 0){
			for(Annotation an : m.getParameterAnnotations()[methodParamIndex]){
				if(ReflectionUtils.isComparisonOperator(an.annotationType())){
					param.setOperator(an.annotationType().getAnnotation(ComparisonOperator.class).value());
				}
			}
		}
	}
	
}
