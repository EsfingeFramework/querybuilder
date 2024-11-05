package ef.qb.core_tests.utils;

import ef.qb.core.Repository;
import ef.qb.core_tests.methodparser.Person;
import ef.qb.core.utils.ReflectionUtils;
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
