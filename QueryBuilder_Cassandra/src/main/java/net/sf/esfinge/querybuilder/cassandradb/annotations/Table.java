package net.sf.esfinge.querybuilder.cassandradb.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    String name() default "";
}
