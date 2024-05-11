package esfinge.querybuilder.core_tests.methodparser.conversor;

import esfinge.querybuilder.core.methodparser.conversor.ConversorFactory;
import static org.junit.Assert.*;
import org.junit.Test;

public class ConversorTests {

    @Test
    public void toIntTest() {
        var value = ConversorFactory.get(int.class).convert("23");
        assertEquals(value, 23);
    }

    @Test
    public void toShortTest() {
        var value = ConversorFactory.get(short.class).convert("23");
        assertEquals(value, (short) 23);
    }

    @Test
    public void toByteTest() {
        var value = ConversorFactory.get(byte.class).convert("23");
        assertEquals(value, (byte) 23);
    }

    @Test
    public void toLongTest() {
        var value = ConversorFactory.get(long.class).convert("23");
        assertEquals(value, Long.valueOf(23));
    }

    @Test
    public void toBooleanTest() {
        var value = ConversorFactory.get(boolean.class).convert("true");
        assertEquals(value, Boolean.TRUE);
    }

    @Test
    public void toFloatTest() {
        var value = ConversorFactory.get(float.class).convert("23");
        assertEquals(value, Float.valueOf(23));
    }

    @Test
    public void toDoubleTest() {
        var value = ConversorFactory.get(double.class).convert("23");
        assertEquals(value, Double.valueOf(23));
    }

}
