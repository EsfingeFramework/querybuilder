package net.sf.esfinge.querybuilder.core_tests;

import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;

public class DummyEntityClassProvider implements EntityClassProvider {

    @Override
    public Class<?> getEntityClass(String name) {
        return null;
    }

}
