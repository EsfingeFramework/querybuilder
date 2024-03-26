package net.sf.esfinge.querybuilder;

import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;

public class DummyEntityClassProvider implements EntityClassProvider {

    @Override
    public Class<?> getEntityClass(String name) {
        return null;
    }

}
