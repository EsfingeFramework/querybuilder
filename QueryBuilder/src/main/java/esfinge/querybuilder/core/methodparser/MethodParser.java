package esfinge.querybuilder.core.methodparser;

import java.lang.reflect.Method;

public interface MethodParser {

    QueryInfo parse(Method m);

    boolean fitParserConvention(Method m);

    void setInterface(Class<?> interf);

    // adding this only for the recognition of domain terms by the plugin
    // this does not imply any change in the main flow
    void setInterface(Class<?> interf, ClassLoader loader);

}
