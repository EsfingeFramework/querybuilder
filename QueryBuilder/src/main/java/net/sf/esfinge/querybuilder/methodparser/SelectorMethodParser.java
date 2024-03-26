package net.sf.esfinge.querybuilder.methodparser;

import java.lang.reflect.Method;
import java.util.List;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

public class SelectorMethodParser implements MethodParser {

    private final List<MethodParser> parserList;
    private EntityClassProvider classProvider;

    public SelectorMethodParser() {
        parserList = ServiceLocator.getServiceImplementationList(MethodParser.class);
    }

    @Override
    public QueryInfo parse(Method m) {
        for (var mp : parserList) {
            if (mp.fitParserConvention(m)) {
                return mp.parse(m);
            }
        }
        throw new InvalidQuerySequenceException("The method " + m.getModifiers() + " does not fit in any possible QueryBuilder convention for query methods");
    }

    @Override
    public void setEntityClassProvider(EntityClassProvider classProvider) {
        this.classProvider = classProvider;
        parserList.forEach(mp -> mp.setEntityClassProvider(classProvider));
    }

    @Override
    public boolean fitParserConvention(Method m) {
        return parserList.stream().anyMatch(mp -> (mp.fitParserConvention(m)));
    }

    @Override
    public void setInterface(Class<?> interf) {
        parserList.forEach(mp -> mp.setInterface(interf));
    }

    // adding this only for the recognition of domain terms by the plugin
    // this does not imply any change in the main flow
    @Override
    public void setInterface(Class<?> interf, ClassLoader loader) {
        parserList.forEach(mp -> mp.setInterface(interf, loader));
    }

    public EntityClassProvider getEntityClassProvider() {
        return classProvider;
    }

}
