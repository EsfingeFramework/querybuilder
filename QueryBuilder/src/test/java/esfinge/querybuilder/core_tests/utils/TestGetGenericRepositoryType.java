package esfinge.querybuilder.core_tests.utils;

import esfinge.querybuilder.core.Repository;
import esfinge.querybuilder.core_tests.methodparser.Person;
import esfinge.querybuilder.core.utils.ReflectionUtils;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestGetGenericRepositoryType {

    public interface PersonRepository extends Repository<Person> {
    }

    @Test
    public void verifyInterfaceType() {
        Class type = ReflectionUtils.getFirstGenericTypeFromInterfaceImplemented(PersonRepository.class, Repository.class);
        assertEquals("The type should be person", type, Person.class);
    }

}
