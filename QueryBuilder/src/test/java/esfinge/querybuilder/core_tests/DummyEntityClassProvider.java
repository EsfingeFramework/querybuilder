package esfinge.querybuilder.core_tests;

import esfinge.querybuilder.core.methodparser.EntityClassProvider;

public class DummyEntityClassProvider implements EntityClassProvider {

    @Override
    public Class<?> getEntityClass(String name) {
        return null;
    }

}
