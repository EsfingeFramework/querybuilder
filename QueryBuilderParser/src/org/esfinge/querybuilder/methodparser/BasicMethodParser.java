package org.esfinge.querybuilder.methodparser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.esfinge.querybuilder.annotation.Condition;
import org.esfinge.querybuilder.annotation.DomainTerm;
import org.esfinge.querybuilder.exception.EntityClassNotFoundException;
import org.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import org.esfinge.querybuilder.methodparser.conditions.SimpleDefinedCondition;
import org.esfinge.querybuilder.methodparser.conversor.ConversorFactory;
import org.esfinge.querybuilder.methodparser.conversor.FromStringConversor;
import org.esfinge.querybuilder.utils.ReflectionUtils;
import org.esfinge.querybuilder.utils.StringUtils;

public abstract class BasicMethodParser implements MethodParser {

	protected EntityClassProvider classProvider;
	private List<DomainTerm> terms = new ArrayList<DomainTerm>();
	protected TermLibrary termLib;

	@Override
	public void setEntityClassProvider(EntityClassProvider classProvider) {
		this.classProvider = classProvider;
	}

	protected String getEntityName(List<String> words, IndexCounter index) {
		StringBuilder entityNameBuilder = new StringBuilder();
		if(!"get".contains(words.get(index.get()))){
			throw new InvalidQuerySequenceException("Query method should start with get, but "+words.get(index.get())+"was found.");
		}
		index.increment();
		while(isPartOfEntityName(words, index)){
			entityNameBuilder.append(StringUtils.firstCharWithUppercase(words.get(index.get())));
			index.increment();
		}
		return entityNameBuilder.toString();
	}

	protected boolean isPartOfEntityName(List<String> words, IndexCounter index) {
		return index.get() < words.size() && !words.get(index.get()).equals("by")
				&& !StringUtils.matchString("order by", words, index.get())
				&& !termLib.hasDomainTerm(words, index.get());
	}

	protected boolean isPartOfPropertyName(List<String> words, IndexCounter index) {
		return index.get() < words.size() && 
		       !isConector(words.get(index.get())) && 
		       !ComparisonType.isOperator(words.get(index.get())) &&
		       !isComparisonOrder(words.get(index.get()))
		       && !StringUtils.matchString("order by", words, index.get());
	}

	protected boolean hasDomainTerm(List<String> words, IndexCounter index) {
		return words.size() > index.get() && !words.get(index.get()).equals("by") 
				&& !StringUtils.matchString("order by", words, index.get())
				&& termLib.hasDomainTerm(words, index.get());
	}

	protected boolean isConector(String conector) {
		return conector.equals("and") || conector.equals("or"); 
	}

	protected boolean isComparisonOrder(String name) {
		return name.equals("asc") || name.equals("desc"); 
	}

	@Override
	public void setInterface(Class<?> interf) {
		termLib = new TermLibrary(interf);
	}

	protected QueryInfo createQueryInfo(Method m, List<String> words, IndexCounter index) {
		QueryInfo qi = new QueryInfo();
		qi.setQueryType(m);
		qi.setEntityName(getEntityName(words, index));
		Class<?> entityClass = classProvider.getEntityClass(qi.getEntityName());
		if(entityClass == null){
			throw new EntityClassNotFoundException("Entity class "+qi.getEntityName()+" not found.");
		}
		qi.setEntityType(entityClass);
		readDomainTerms(qi, words, index);
		return qi;
	}

	protected void readDomainTerms(QueryInfo qi, List<String> words, IndexCounter index) {
		while(hasDomainTerm(words, index)){
			DomainTerm term = termLib.getDomainTerm(words, index);
			addDomainTermConditions(qi, index, term);
		}
	}

	private void addDomainTermConditions(QueryInfo qi, IndexCounter index,
			DomainTerm term) {
				for(Condition c : term.conditions()){
					Class propType = ReflectionUtils.getPropertyType(qi.getEntityType(), c.property());
					Object value = c.value();
					if(propType != String.class){
						FromStringConversor conv = ConversorFactory.get(propType);
						value = conv.convert(c.value());
					}
					SimpleDefinedCondition sdc = new SimpleDefinedCondition(c.property(), c.comparison(), value);
					qi.addCondition(sdc, c.conector());
				}
				index.add(term.term().split(" ").length);
			}

	@Override
	public boolean fitParserConvention(Method m) {
		List<String> words = StringUtils.splitCamelCaseName(m.getName());
		if(!words.get(0).equals("get")){
			return false;
		}
		IndexCounter index = new IndexCounter();
		String name = getEntityName(words, index);
		Class c = classProvider.getEntityClass(name);
		return c != null;
	}

	protected String getPropertyName(List<String> words, IndexCounter index, Class entityClass) {
		String paramName = words.get(index.get());
		index.increment();
		while(isPartOfPropertyName(words, index)){
			if(ReflectionUtils.existProperty(entityClass, paramName)){
				paramName += "." + words.get(index.get());
			}else{
				paramName += StringUtils.firstCharWithUppercase(words.get(index.get()));
			}				
			index.increment();
		}
		return paramName;
	}

	protected void addOrderBy(QueryInfo qi, List<String> words, IndexCounter index) {
		if(StringUtils.matchString("order by", words, index.get())){
			index.add(2);
			while(words.size() > index.get()){
				if(index.get(words).equals("and"))
					index.increment();
				String orderProp = getPropertyName(words, index, qi.getEntityType());
				OrderingDirection dir = OrderingDirection.ASC;
				if(words.size() > index.get() && isComparisonOrder(index.get(words))){
					if(index.get(words).equals("desc")){
						dir = OrderingDirection.DESC;
					}
					index.increment();
				}
				qi.addOrdering(orderProp, dir);
			}
		}
	}

}