package org.esfinge.querybuilder.jdbc;

import org.esfinge.querybuilder.methodparser.EntityClassProvider;
import org.esfinge.querybuilder.finder.FinderManager;
import org.esfinge.querybuilder.finder.XmlEntityFinder;

public class JDBCEntityClassProvider implements EntityClassProvider {

	@Override
	public Class<?> getEntityClass(String name) {

		try {
			try {
				FinderManager finderManager = new FinderManager(
						new XmlEntityFinder());
				String resourceToFind = "";
				resourceToFind = finderManager.find(name);
				Class<?> classResult = Class.forName(resourceToFind)
						.newInstance().getClass();
				return classResult;

			} catch (InstantiationException e) {
				return null;
			} catch (IllegalAccessException e) {
				return null;
			}

		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
