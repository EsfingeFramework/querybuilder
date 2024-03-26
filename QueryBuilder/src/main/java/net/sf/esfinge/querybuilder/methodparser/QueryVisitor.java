package net.sf.esfinge.querybuilder.methodparser;

import java.util.Map;
import java.util.Set;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

public interface QueryVisitor {

    void visitEntity(String entity);

    void visitConector(String conector);

    void visitCondition(String propertyName, ComparisonType operator);

    void visitCondition(String propertyName, ComparisonType operator, NullOption nullOption);

    void visitCondition(String propertyName, ComparisonType operator, Object fixedValue);

    void visitOrderBy(String property, OrderingDirection order);

    void visitEnd();

    @Deprecated
    boolean isDynamic();

    @Deprecated
    String getQuery();

    @Deprecated
    String getQuery(Map<String, Object> params);

    @Deprecated
    Set<String> getFixParameters();

    @Deprecated
    Object getFixParameterValue(String param);

    QueryRepresentation getQueryRepresentation();

}
