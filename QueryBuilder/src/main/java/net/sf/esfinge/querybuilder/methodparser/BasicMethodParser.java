package net.sf.esfinge.querybuilder.methodparser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import net.sf.esfinge.querybuilder.annotation.DomainTerm;
import net.sf.esfinge.querybuilder.annotation.InvariablePageSize;
import net.sf.esfinge.querybuilder.annotation.PageNumber;
import net.sf.esfinge.querybuilder.annotation.VariablePageSize;
import net.sf.esfinge.querybuilder.exception.EntityClassNotFoundException;
import net.sf.esfinge.querybuilder.exception.InvalidPaginationAnnotationSchemeException;
import net.sf.esfinge.querybuilder.exception.InvalidPropertyTypeException;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.conditions.SimpleDefinedCondition;
import net.sf.esfinge.querybuilder.methodparser.conversor.ConversorFactory;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;
import net.sf.esfinge.querybuilder.utils.StringUtils;

public abstract class BasicMethodParser implements MethodParser {

    protected EntityClassProvider classProvider;
    //private final List<DomainTerm> terms = new ArrayList<>();
    protected TermLibrary termLib;

    @Override
    public void setEntityClassProvider(EntityClassProvider classProvider) {
        this.classProvider = classProvider;
    }

    protected String getEntityName(List<String> words, IndexCounter index) {
        var entityNameBuilder = new StringBuilder();
        if (!"get".equals(words.get(index.get()))) {
            throw new InvalidQuerySequenceException("Query method should start with get, but " + words.get(index.get()) + " was found.");
        }
        index.increment();
        while (isPartOfEntityName(words, index)) {
            entityNameBuilder.append(StringUtils.firstCharWithUppercase(words.get(index.get())));
            index.increment();
        }
        return entityNameBuilder.toString();
    }

    protected boolean isPartOfEntityName(List<String> words, IndexCounter index) {
        return index.get() < words.size() && !words.get(index.get()).equals("by") && !StringUtils.matchString("order by", words, index.get()) && !termLib.hasDomainTerm(words, index.get());
    }

    protected boolean isPartOfPropertyName(List<String> words, IndexCounter index) {
        return index.get() < words.size() && !isConector(words.get(index.get())) && !ComparisonType.isOperator(words.get(index.get())) && !isComparisonOrder(words.get(index.get())) && !StringUtils.matchString("order by", words, index.get());
    }

    protected boolean hasDomainTerm(List<String> words, IndexCounter index) {
        return words.size() > index.get() && !words.get(index.get()).equals("by") && !StringUtils.matchString("order by", words, index.get()) && termLib.hasDomainTerm(words, index.get());
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

    @Override
    public void setInterface(Class<?> interf, ClassLoader loader) {
        termLib = new TermLibrary(interf, loader);
    }

    protected QueryInfo createQueryInfo(Method m, List<String> words, IndexCounter index) {
        var qi = new QueryInfo();
        qi.setQueryType(m);
        qi.setEntityName(getEntityName(words, index));
        var entityClass = classProvider.getEntityClass(qi.getEntityName());
        if (entityClass == null) {
            throw new EntityClassNotFoundException("Entity class " + qi.getEntityName() + " not found.");
        }
        qi.setEntityType(entityClass);
        readDomainTerms(qi, words, index);
        return qi;
    }

    protected void readDomainTerms(QueryInfo qi, List<String> words, IndexCounter index) {
        while (hasDomainTerm(words, index)) {
            var term = termLib.getDomainTerm(words, index);
            addDomainTermConditions(qi, index, term);
        }
    }

    private void addDomainTermConditions(QueryInfo qi, IndexCounter index, DomainTerm term) {
        for (var c : term.conditions()) {
            Class propType = ReflectionUtils.getPropertyType(qi.getEntityType(), c.property());
            Object value = c.value();
            if (propType != String.class) {
                var conv = ConversorFactory.get(propType);
                value = conv.convert(c.value());
            }
            var sdc = new SimpleDefinedCondition(c.property(), c.comparison(), value);
            qi.addCondition(sdc, c.conector());
        }
        index.add(term.term().split(" ").length);
    }

    @Override
    public boolean fitParserConvention(Method m) {
        var words = StringUtils.splitCamelCaseName(m.getName());
        if (!words.get(0).equals("get")) {
            return false;
        }
        var index = new IndexCounter();
        var name = getEntityName(words, index);
        Class c = classProvider.getEntityClass(name);
        return c != null;
    }

    protected String getPropertyName(List<String> words, IndexCounter index, Class entityClass) {
        var paramName = words.get(index.get());
        index.increment();
        while (isPartOfPropertyName(words, index)) {
            if (ReflectionUtils.existProperty(entityClass, paramName)) {
                paramName += "." + words.get(index.get());
            } else {
                paramName += StringUtils.firstCharWithUppercase(words.get(index.get()));
            }
            index.increment();
        }
        return paramName;
    }

    protected void addOrderBy(QueryInfo qi, List<String> words, IndexCounter index) {
        if (StringUtils.matchString("order by", words, index.get())) {
            index.add(2);
            while (words.size() > index.get()) {
                if (index.get(words).equals("and")) {
                    index.increment();
                }

                var orderProp = getPropertyName(words, index, qi.getEntityType());

                // validates typo on orderBy properties (eclipse plugin)
                ReflectionUtils.getPropertyType(qi.getEntityType(), orderProp);

                var dir = OrderingDirection.ASC;
                if (words.size() > index.get() && isComparisonOrder(index.get(words))) {
                    if (index.get(words).equals("desc")) {
                        dir = OrderingDirection.DESC;
                    }
                    index.increment();
                }
                qi.addOrdering(orderProp, dir);
            }
        }
    }

    private Annotation cloneAnnotationBySerialization(Annotation annotation) {
        try {
            return TermLibrary.cloneAnnotationBySerialization(annotation);
        } catch (IOException | ClassNotFoundException exception) { //if something goes wrong
            return annotation; //return the own annotation, because it's not possible to clone it through serialization
        }
    }

    protected void addPagination(Method m, QueryInfo info) {
        var hasPageSizeAnnotation = false;
        var hasPageNumberAnnotation = false;
        var parameterTypes = m.getParameterTypes();
        //try to get the @InvariablePageSize annotation from the method
        for (var annotation : m.getAnnotations()) {
            var clone = cloneAnnotationBySerialization(annotation);
            if (clone.annotationType() == InvariablePageSize.class) {
                var size = (InvariablePageSize) clone;
                info.setPageSize(size.value());
                hasPageSizeAnnotation = true;
            }
        }
        var size = VariablePageSize.class;
        var number = PageNumber.class;
        var annotations = m.getParameterAnnotations();
        for (var index = 0; index < annotations.length; index++) {
            for (var annotation : annotations[index]) {
                if (number.getName().equals(annotation.annotationType().getName())) {
                    if (hasPageNumberAnnotation) {
                        throw new InvalidPaginationAnnotationSchemeException("The method " + m.getName() + " should have only one @PageNumber annotation");
                    } else if (parameterTypes[index] != int.class && parameterTypes[index] != Integer.class) {
                        throw new InvalidPropertyTypeException("The parameter with @PageNumber should be an integer number but is " + parameterTypes[index].getName());
                    } else {
                        info.setPageNumberParameterIndex(index);
                        hasPageNumberAnnotation = true;
                    }
                } else if (size.getName().equals(annotation.annotationType().getName())) {
                    if (hasPageSizeAnnotation) {
                        throw new InvalidPaginationAnnotationSchemeException("The method " + m.getName() + " should have only one page size annotation");
                    } else if (parameterTypes[index] != int.class && parameterTypes[index] != Integer.class) {
                        throw new InvalidPropertyTypeException("The parameter with @VariablePageSize should be an integer number but is " + parameterTypes[index].getName());
                    } else {
                        info.setPageSizeParameterIndex(index);
                        hasPageSizeAnnotation = true;
                    }
                }
            }
        }

        if (hasPageSizeAnnotation && !hasPageNumberAnnotation) {
            throw new InvalidPaginationAnnotationSchemeException("The method " + m.getName() + " is using an page size annotation but no parameter with @PageNumber was found");
        } else if (hasPageNumberAnnotation && !hasPageSizeAnnotation) {
            throw new InvalidPaginationAnnotationSchemeException("The method " + m.getName() + " is using the @PageNumber annotation but no variable or invariable page size annotation was found");
        }
    }

}
