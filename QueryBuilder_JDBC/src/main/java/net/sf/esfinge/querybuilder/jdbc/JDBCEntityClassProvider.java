package net.sf.esfinge.querybuilder.jdbc;

import net.sf.esfinge.querybuilder.finder.FinderManager;
import net.sf.esfinge.querybuilder.finder.XmlEntityFinder;
import net.sf.esfinge.querybuilder.methodparser.EntityClassProvider;

public class JDBCEntityClassProvider implements EntityClassProvider {

    @Override
    public Class<?> getEntityClass(String name) {

        try {
            try {
                var finderManager = new FinderManager(
                        new XmlEntityFinder());
                @SuppressWarnings("UnusedAssignment")
                var resourceToFind = "";
                resourceToFind = finderManager.find(name);
                var classResult = Class.forName(resourceToFind)
                        .newInstance().getClass();
                return classResult;

            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }

        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
