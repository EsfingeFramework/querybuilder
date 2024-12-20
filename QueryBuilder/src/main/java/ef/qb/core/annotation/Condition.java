package ef.qb.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ef.qb.core.methodparser.ComparisonType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Condition {

    String property();

    String value();

    ComparisonType comparison() default ComparisonType.EQUALS;

    String conector() default "and";
}
