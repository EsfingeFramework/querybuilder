package org.esfinge.querybuilder.methodparser;

import static org.esfinge.querybuilder.utils.ReflectionUtils.containsParameterAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.esfinge.querybuilder.annotation.CompareToNull;
import org.esfinge.querybuilder.annotation.IgnoreWhenNull;
import org.esfinge.querybuilder.annotation.QueryObject;
import org.esfinge.querybuilder.annotation.ServicePriority;
import org.esfinge.querybuilder.exception.InvalidPropertyException;
import org.esfinge.querybuilder.exception.InvalidPropertyTypeException;
import org.esfinge.querybuilder.exception.QueryObjectException;
import org.esfinge.querybuilder.methodparser.conditions.NullOption;
import org.esfinge.querybuilder.methodparser.conditions.SimpleCondition;
import org.esfinge.querybuilder.utils.NameUtils;
import org.esfinge.querybuilder.utils.ReflectionUtils;
import org.esfinge.querybuilder.utils.StringUtils;

@ServicePriority(5)
public class QueryObjectMethodParser extends BasicMethodParser{

	@Override
	public QueryInfo parse(Method m) {
		List<String> words = StringUtils.splitCamelCaseName(m.getName());
		IndexCounter index = new IndexCounter();
		QueryInfo qi = createQueryInfo(m, words, index);
		qi.setQueryStyle(QueryStyle.QUERY_OBJECT);
		
		Class paramType = m.getParameterTypes()[0];
		for(Method met : paramType.getMethods()){
			if(ReflectionUtils.isGetter(met)){
				try{
					String queryPropName = NameUtils.acessorToProperty(met.getName());
					List<String> propWords = StringUtils.splitCamelCaseName(queryPropName);
					IndexCounter counter = new IndexCounter();
					String propName = getPropertyName(propWords, counter, qi.getEntityType());
					ComparisonType cp = ComparisonType.getComparisonType(propWords, counter.get());
					if(cp.equals(ComparisonType.EQUALS)){
						cp = ReflectionUtils.getPropertyComparisonTypeByAnnotation(queryPropName, paramType);
					}
					SimpleCondition condition = new SimpleCondition();
					condition.setName(propName);
					condition.setOperator(cp);
					condition.setNullOption(getPropertyNullOption(queryPropName, paramType));
					Class propType = ReflectionUtils.getPropertyType(qi.getEntityType(), propName);
					if(met.getReturnType() != propType){
						throw new InvalidPropertyTypeException("The property "+propName+" should be "+propType.getName()+" but is "+ met.getReturnType());
					}
					qi.addCondition(condition);
				}catch(Exception e){
					throw new QueryObjectException(e.getMessage(), e, met, paramType);
				}
			}
		}
		addOrderBy(qi, words, index);
		return qi;
	}

	@Override
	public boolean fitParserConvention(Method m) {
		return super.fitParserConvention(m) && containsParameterAnnotation(m, 0, QueryObject.class);
	}
	
	protected boolean isPartOfEntityName(List<String> words, IndexCounter index) {
		return index.get() < words.size()
				&& !StringUtils.matchString("order by", words, index.get())
				&& !termLib.hasDomainTerm(words, index.get());
	}
	
	public NullOption getPropertyNullOption(String propName, Class queryClass){
		String getterName = NameUtils.propertyToGetter(propName);
		try {
			try{
				Field f = queryClass.getDeclaredField(propName);
				if(f.isAnnotationPresent(CompareToNull.class))
					return NullOption.COMPARE_TO_NULL;
				if(f.isAnnotationPresent(IgnoreWhenNull.class))
					return NullOption.IGNORE_WHEN_NULL;
			}catch(Exception e){
				
			}
			Method m = queryClass.getMethod(getterName);
			if(m.isAnnotationPresent(CompareToNull.class))
				return NullOption.COMPARE_TO_NULL;
			if(m.isAnnotationPresent(IgnoreWhenNull.class))
				return NullOption.IGNORE_WHEN_NULL;
			
		} catch (Exception e) {
			throw new InvalidPropertyException("Cannot access property "+propName+" or method "+ getterName +" on class "+queryClass.getName(),e); 
		}
		return NullOption.NONE;
	}


}
