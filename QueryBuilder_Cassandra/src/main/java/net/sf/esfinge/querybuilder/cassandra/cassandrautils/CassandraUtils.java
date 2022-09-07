package net.sf.esfinge.querybuilder.cassandra.cassandrautils;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingAnnotationException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingKeySpaceNameException;

import java.lang.reflect.Field;

public class CassandraUtils {

    public static void checkValidClassConfiguration(Class<?> aClass) {
        if (!aClass.isAnnotationPresent(Table.class))
            throw new MissingAnnotationException("@Table annotation missing from class " + aClass.getSimpleName());

        Field[] classFields = aClass.getDeclaredFields();
        boolean partitionAnnotationFound = false;

        for (Field f : classFields) {
            if (f.isAnnotationPresent(PartitionKey.class)) {
                partitionAnnotationFound = true;
                break;
            }
        }

        if (!partitionAnnotationFound)
            throw new MissingAnnotationException("The @PartitionKey annotation needs to be assigned to a field " + aClass.getSimpleName());

        if (aClass.getDeclaredAnnotation(Table.class).keyspace().equals(""))
            throw new MissingKeySpaceNameException("Missing keyspace value from class " + aClass.getSimpleName());
    }
}
