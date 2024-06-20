package net.sf.esfinge.querybuilder.finder;

public class FinderManager {

    private IFindable finder;

    public FinderManager(IFindable finder) {
        setFinder(finder);
    }

    public void setFinder(IFindable finder) {
        this.finder = finder;
    }

    public String find(String KeySearch) {
        var result = finder.search(KeySearch);
        return result;
    }

}
