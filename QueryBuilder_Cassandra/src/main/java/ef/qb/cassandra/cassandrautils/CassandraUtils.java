package ef.qb.cassandra.cassandrautils;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import ef.qb.cassandra.exceptions.MissingAnnotationException;
import ef.qb.cassandra.exceptions.MissingKeySpaceNameException;

public class CassandraUtils {

    public static void checkValidClassConfiguration(Class<?> aClass) {
        if (!aClass.isAnnotationPresent(Table.class)) {
            throw new MissingAnnotationException("@Table annotation missing from class " + aClass.getSimpleName());
        }

        var classFields = aClass.getDeclaredFields();
        var partitionAnnotationFound = false;
        for (var f : classFields) {
            if (f.isAnnotationPresent(PartitionKey.class)) {
                partitionAnnotationFound = true;
                break;
            }
        }

        if (!partitionAnnotationFound) {
            throw new MissingAnnotationException("The @PartitionKey annotation needs to be assigned to a field " + aClass.getSimpleName());
        }

        if (aClass.getDeclaredAnnotation(Table.class).keyspace().equals("")) {
            throw new MissingKeySpaceNameException("Missing keyspace value from class " + aClass.getSimpleName());
        }
    }
}
