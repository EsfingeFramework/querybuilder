package esfinge.querybuilder.mongodb;

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
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;

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

    public void addCondition(ArrayList<ArrayList<Criteria>> criteria, Class<?> clazz, ParameterFormater formatter) {
        addCondition(criteria, formatter.formatParameter(fixedValue), clazz);
    }

    public void addCondition(ArrayList<ArrayList<Criteria>> criteria, Object value, Class<?> clazz) {

        if (!(value == null && nullOption == NullOption.IGNORE_WHEN_NULL)) {

            Criteria newCriteria;
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
                    newCriteria = compositeProperty(clazz, value);
                } else {

                    switch (compType) {
                        case GREATER_OR_EQUALS:
                            newCriteria = getQuery(clazz).criteria(propertyName).greaterThanOrEq(value);
                            break;
                        case LESSER_OR_EQUALS:
                            newCriteria = getQuery(clazz).criteria(propertyName).lessThanOrEq(value);
                            break;
                        case GREATER:
                            newCriteria = getQuery(clazz).criteria(propertyName).greaterThan(value);
                            break;
                        case LESSER:
                            newCriteria = getQuery(clazz).criteria(propertyName).lessThan(value);
                            break;
                        case NOT_EQUALS:
                            newCriteria = getQuery(clazz).criteria(propertyName).notEqual(value);
                            break;
                        case EQUALS:
                            newCriteria = getQuery(clazz).criteria(propertyName).equal(value);
                            break;
                        default:
                            newCriteria = getQuery(clazz).criteria(propertyName).contains((String) value);
                    }
                }

            } else {

                if (composite) {
                    newCriteria = getQuery(clazz).criteria(propertyName.substring(0, propertyName.indexOf("."))).equal(null);
                } else {
                    newCriteria = getQuery(clazz).criteria(propertyName).equal(null);
                }

            }
            criteria.get(criteria.size() - 1).add(newCriteria);

            switch (nextConector) {
                case "and": {
                    break;
                }
                case "or":
                    criteria.add(new ArrayList<>());
                    break;
                case "": {
                    break;
                }
                default:
                    System.err.println("Unsupported connector!");
            }
        }
    }

    private Criteria compositeProperty(Class<?> clazz, Object value) {

        var properties = propertyName.split("\\.");
        var q = getQuery(MongoDBEntityClassProvider.getEntityClass(properties[properties.length - 2]));
        switch (compType) {
            case GREATER_OR_EQUALS:
                q.field(properties[properties.length - 1]).greaterThanOrEq(value);
                break;
            case LESSER_OR_EQUALS:
                q.field(properties[properties.length - 1]).lessThanOrEq(value);
                break;
            case GREATER:
                q.field(properties[properties.length - 1]).greaterThan(value);
                break;
            case LESSER:
                q.field(properties[properties.length - 1]).lessThan(value);
                break;
            case NOT_EQUALS:
                q.field(properties[properties.length - 1]).notEqual(value);
                break;
            case EQUALS:
                q.field(properties[properties.length - 1]).equal(value);
                break;
            default:
                q.field(properties[properties.length - 1]).contains((String) value);
        }
        var results = q.asList();

        for (var i = properties.length - 3; i >= 0; i--) {
            var query = getQuery(MongoDBEntityClassProvider.getEntityClass(properties[i]));
            query.field(properties[i + 1]).in(results);
            results = query.asList();
        }

        return getQuery(clazz).criteria(properties[0]).in(results);
    }

    private Query<?> getQuery(Class<?> clazz) {
        var dsp = ServiceLocator.getServiceImplementation(DatastoreProvider.class);
        var ds = dsp.getDatastore();
        return ds.createQuery(clazz);
    }

    @Override
    public String toString() {
        var s = propertyName;
        s += " (" + paramName + ") ";
        s += compType + " ";
        return s;
    }
}
