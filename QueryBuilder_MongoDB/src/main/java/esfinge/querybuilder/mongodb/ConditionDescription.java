package esfinge.querybuilder.mongodb;

import dev.morphia.annotations.Reference;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import esfinge.querybuilder.core.methodparser.ComparisonType;
import static esfinge.querybuilder.core.methodparser.ComparisonType.EQUALS;
import static esfinge.querybuilder.core.methodparser.ComparisonType.GREATER;
import static esfinge.querybuilder.core.methodparser.ComparisonType.GREATER_OR_EQUALS;
import static esfinge.querybuilder.core.methodparser.ComparisonType.LESSER;
import static esfinge.querybuilder.core.methodparser.ComparisonType.LESSER_OR_EQUALS;
import static esfinge.querybuilder.core.methodparser.ComparisonType.NOT_EQUALS;
import esfinge.querybuilder.core.methodparser.conditions.NullOption;
import esfinge.querybuilder.core.methodparser.formater.ParameterFormater;
import esfinge.querybuilder.core.utils.ServiceLocator;
import java.util.ArrayList;
import java.util.List;

public class ConditionDescription {

    private final String propertyName;
    private String paramName;
    private final ComparisonType compType;
    private Object fixedValue;
    private NullOption nullOption = NullOption.NONE;
    private String nextConector = "";

    public ConditionDescription(String propertyName, ComparisonType compType) {
        super();
        this.propertyName = propertyName;
        this.paramName = propertyName.substring(propertyName.lastIndexOf(".") + 1);
        this.compType = compType;
    }

    public NullOption getNullOption() {
        return nullOption;
    }

    public void setNullOption(NullOption nullOption) {
        this.nullOption = nullOption;
    }

    public String getNextConector() {
        return nextConector;
    }

    public void setNextConector(String nextConector) {
        this.nextConector = nextConector;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getParamName() {
        return paramName;
    }

    public ComparisonType getCompType() {
        return compType;
    }

    public String getCondition() {
        throw new UnsupportedOperationException();
    }

    public Object getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(Object fixedValue) {
        paramName += compType.name();
        this.fixedValue = fixedValue;
    }

    public void addCondition(ArrayList<List<Filter>> criteria, Class<?> clazz, ParameterFormater formatter) {
        addCondition(criteria, formatter.formatParameter(fixedValue), clazz);
    }

    public void addCondition(ArrayList<List<Filter>> criteria, Object value, Class<?> clazz) {

        if (!(value == null && nullOption == NullOption.IGNORE_WHEN_NULL)) {

            Filter newFilter;
            var composite = false;

            if (propertyName.contains(".")) {
                for (var field : clazz.getDeclaredFields()) {
                    if (field.getName().equalsIgnoreCase(propertyName.substring(0, propertyName.indexOf(".")))) {
                        if (field.isAnnotationPresent(Reference.class)) {
                            composite = true;
                        }
                    }
                }
            }

            if (value != null) {
                if (composite) {
                    newFilter = compositeProperty(value);
                } else {
                    switch (compType) {
                        case GREATER_OR_EQUALS:
                            newFilter = Filters.gte(propertyName, value);
                            break;
                        case LESSER_OR_EQUALS:
                            newFilter = Filters.lte(propertyName, value);
                            break;
                        case GREATER:
                            newFilter = Filters.gt(propertyName, value);
                            break;
                        case LESSER:
                            newFilter = Filters.lt(propertyName, value);
                            break;
                        case NOT_EQUALS:
                            newFilter = Filters.ne(propertyName, value);
                            break;
                        case EQUALS:
                            newFilter = Filters.eq(propertyName, value);
                            break;
                        default:
                            newFilter = Filters.regex(propertyName, value.toString());
                    }
                }
            } else {
                newFilter = Filters.eq(propertyName, null);
            }

            criteria.get(criteria.size() - 1).add(newFilter);

            switch (nextConector) {
                case "and":
                    break;
                case "or":
                    criteria.add(new ArrayList<>());
                    break;
                case "":
                    break;
                default:
                    System.err.println("Unsupported connector!");
            }
        }
    }

    private Filter compositeProperty(Object value) {

        var properties = propertyName.split("\\.");
        Query<?> q = getQuery(MongoDBEntityClassProvider.getEntityClass(properties[properties.length - 2]));
        Filter filter;

        switch (compType) {
            case GREATER_OR_EQUALS:
                filter = Filters.gte(properties[properties.length - 1], value);
                break;
            case LESSER_OR_EQUALS:
                filter = Filters.lte(properties[properties.length - 1], value);
                break;
            case GREATER:
                filter = Filters.gt(properties[properties.length - 1], value);
                break;
            case LESSER:
                filter = Filters.lt(properties[properties.length - 1], value);
                break;
            case NOT_EQUALS:
                filter = Filters.ne(properties[properties.length - 1], value);
                break;
            case EQUALS:
                filter = Filters.eq(properties[properties.length - 1], value);
                break;
            default:
                filter = Filters.regex(properties[properties.length - 1], value.toString());
        }

        var results = q.filter(filter).iterator().toList();

        for (var i = properties.length - 3; i >= 0; i--) {
            Query<?> query = getQuery(MongoDBEntityClassProvider.getEntityClass(properties[i]));
            query.filter(Filters.in(properties[i + 1], results));
            results = query.iterator().toList();
        }

        return Filters.in(properties[0], results);
    }

    private Query<?> getQuery(Class<?> clazz) {
        var dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
        var ds = dsp.getDatastore();
        return ds.find(clazz);
    }

    @Override
    public String toString() {
        var s = propertyName;
        s += " (" + paramName + ") ";
        s += compType + " ";
        return s;
    }
}
