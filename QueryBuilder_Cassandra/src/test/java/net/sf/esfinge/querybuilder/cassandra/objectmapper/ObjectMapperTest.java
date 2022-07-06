package net.sf.esfinge.querybuilder.cassandra.objectmapper;

import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingAnnotationException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingKeySpaceNameException;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.ClassWithMissingAnnotation;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.ClassWithMissingKeyspaceValue;
import org.junit.Test;

public class ObjectMapperTest {

    @Test(expected = MissingAnnotationException.class)
    public void listWithMissingAnnotationTest() {
        ObjectMapper<ClassWithMissingAnnotation> mapper = new ObjectMapper<>(null,ClassWithMissingAnnotation.class);
    }

    @Test(expected = MissingKeySpaceNameException.class)
    public void listWithMissingKeyspaceValueTest() {
        ObjectMapper<ClassWithMissingKeyspaceValue> mapper = new ObjectMapper<>(null,ClassWithMissingKeyspaceValue.class);
    }
}