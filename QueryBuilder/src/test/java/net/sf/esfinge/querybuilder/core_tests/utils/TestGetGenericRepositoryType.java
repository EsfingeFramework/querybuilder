package net.sf.esfinge.querybuilder.core_tests.utils;

import net.sf.esfinge.querybuilder.Repository;
import net.sf.esfinge.querybuilder.core_tests.methodparser.Person;
import net.sf.esfinge.querybuilder.utils.ReflectionUtils;
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
