package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingAnnotationException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingKeySpaceNameException;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.ClassWithMissingAnnotation;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.ClassWithMissingKeyspaceValue;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.ClassWithMissingPartitionKey;
import org.junit.Test;

public class CassandraRepositoryWrongConfigurationIntegrationTest {

    @Test(expected = MissingAnnotationException.class)
    public void missingAnnotationFromClassTest() {
        QueryBuilder.create(MissingAnnotationRepository.class);
    }

    @Test(expected = MissingKeySpaceNameException.class)
    public void missingKeyspaceValueFromClassTest() {
        QueryBuilder.create(MissingKeyspaceValueRepository.class);
    }

    @Test(expected = MissingAnnotationException.class)
    public void missingPartitionKeyFromClassTest() {
        QueryBuilder.create(MissingPartitionKeyRepository.class);
    }


    public interface MissingAnnotationRepository extends Repository<ClassWithMissingAnnotation> {
    }

    public interface MissingKeyspaceValueRepository extends Repository<ClassWithMissingKeyspaceValue> {
    }

    public interface MissingPartitionKeyRepository extends Repository<ClassWithMissingPartitionKey> {
    }
}
