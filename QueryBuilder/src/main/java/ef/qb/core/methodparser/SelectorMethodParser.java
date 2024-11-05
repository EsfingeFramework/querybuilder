package ef.qb.core.methodparser;

import ef.qb.core.exception.InvalidQuerySequenceException;
import ef.qb.core.utils.ServiceLocator;
import java.lang.reflect.Method;
import java.util.List;

public class SelectorMethodParser implements MethodParser {

    private final List<MethodParser> parserList;

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

}
