package esfinge.querybuilder.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PolyglotOneToOne {

    String mappedBy() default "NONE";

    String joinColumn() default "NONE";

    String referencedColumnName();

}
