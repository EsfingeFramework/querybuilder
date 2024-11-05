package ef.qb.core_tests.utils;

import java.util.Arrays;
import ef.qb.core.utils.StringUtils;
import static org.junit.Assert.*;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void simpleName() {
        var names = StringUtils.splitCamelCaseName("nome");
        assertEquals(1, names.size());
        assertEquals("nome", names.get(0));
    }

    @Test
    public void compositeName() {
        var names = StringUtils.splitCamelCaseName("nomeComposto");
        assertEquals(2, names.size());
        assertEquals("nome", names.get(0));
        assertEquals("composto", names.get(1));
    }

    @Test
    public void sigla() {
        var names = StringUtils.splitCamelCaseName("CPF");
        assertEquals(1, names.size());
        assertEquals("CPF", names.get(0));
    }

    @Test
    public void siglaNoMeio() {
        var names = StringUtils.splitCamelCaseName("numeroCPF");
        assertEquals(2, names.size());
        assertEquals("numero", names.get(0));
        assertEquals("CPF", names.get(1));
    }

    @Test
    public void numberInTheMiddle() {
        var names = StringUtils.splitCamelCaseName("nome123Composto");
        assertEquals(2, names.size());
        assertEquals("nome", names.get(0));
        assertEquals("composto", names.get(1));
    }

    @Test
    public void joinString() {
        var result = StringUtils.removeSpacesToCamelCase("teste aqui novamente");
        assertEquals("testeAquiNovamente", result);
    }

    @Test
    public void matchSimpleString() {
        String[] words = {"string", "a", "b"};
        var result = StringUtils.matchString("string", Arrays.asList(words), 0);
        assertTrue("Should match", result);
    }

    @Test
    public void matchFailed() {
        String[] words = {"str", "a", "b"};
        var result = StringUtils.matchString("string", Arrays.asList(words), 0);
        assertFalse("Shouldn't match", result);
    }

    @Test
    public void matchCompositeString() {
        String[] words = {"c", "string", "here", "b"};
        var result = StringUtils.matchString("string here", Arrays.asList(words), 1);
        assertTrue("Should match", result);
    }

    @Test
    public void notMatchCompositeString() {
        String[] words = {"c", "string", "a", "b"};
        var result = StringUtils.matchString("string here", Arrays.asList(words), 1);
        assertFalse("Shouldn't match", result);
    }

    @Test
    public void toCamelCaseTwoWords() {
        var transform = "address.city";
        var result = StringUtils.toCamelCase(transform);
        assertEquals("addressCity", result);
    }

    @Test
    public void oneWord() {
        var transform = "address";
        var result = StringUtils.toCamelCase(transform);
        assertEquals("address", result);
    }

    @Test
    public void toCamelCaseManyWords() {
        var transform = "address.city.street.name";
        var result = StringUtils.toCamelCase(transform);
        assertEquals("addressCityStreetName", result);
    }

}
