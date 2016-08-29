package org.esfinge.querybuilder.finder;

public class FinderManager {
	private IFindable finder;

	public FinderManager(IFindable finder) {
		setFinder(finder);
	}

	public void setFinder(IFindable finder) {
		this.finder = finder;
	}

	public String find(String KeySearch) {
		String result = finder.search(KeySearch);
		return result;
	}

}