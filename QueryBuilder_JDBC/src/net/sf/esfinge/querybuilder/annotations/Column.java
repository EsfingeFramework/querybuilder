package net.sf.esfinge.querybuilder.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

	String name() default "";

	String type() default "";

	boolean autoIncrement() default false;

}
