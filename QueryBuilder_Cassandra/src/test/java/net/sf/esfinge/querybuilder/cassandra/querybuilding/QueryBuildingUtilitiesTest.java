package net.sf.esfinge.querybuilder.cassandra.querybuilding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryBuildingUtilitiesTest {


    @Test
    public void replaceQueryArgsWithOneArgTest() {
        String query = "SELECT * FROM Person WHERE id > ?";
        Object args[] = {1};

        assertEquals("SELECT * FROM Person WHERE id > 1", QueryBuildingUtilities.replaceQueryArgs(query,args));
    }

    @Test
    public void replaceQueryArgsWithTwoArgsTest() {
        String query = "SELECT * FROM Person WHERE name = ? AND lastName = ?";
        Object args[] = {1, "Max"};

        assertEquals("SELECT * FROM Person WHERE name = ? AND lastName = 'Max'", QueryBuildingUtilities.replaceQueryArgs(query,args));
    }
}