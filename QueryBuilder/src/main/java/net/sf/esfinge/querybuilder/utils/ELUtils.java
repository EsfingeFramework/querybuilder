package net.sf.esfinge.querybuilder.utils;

import jakarta.el.ArrayELResolver;
import jakarta.el.BeanELResolver;
import jakarta.el.FunctionMapper;
import jakarta.el.ListELResolver;
import jakarta.el.MapELResolver;
import jakarta.el.VariableMapper;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.el.ExpressionFactoryImpl;
import org.apache.el.ValueExpressionLiteral;
import org.apache.el.lang.EvaluationContext;
import org.apache.el.lang.FunctionMapperImpl;
import org.apache.el.lang.VariableMapperImpl;

public class ELUtils {

    public static FunctionMapper buildFunctionMapper(
            Map<String, Method> functionMethodMap) {
        var mapper = new FunctionMapperImpl();
        functionMethodMap.keySet().forEach(functionName
                -> mapper.mapFunction("", functionName, functionMethodMap.get(functionName)));
        return mapper;
    }

    public static VariableMapper buildVariableMapper(
            Map<String, Object> attributeMap) {
        var mapper = new VariableMapperImpl();
        attributeMap.keySet().forEach(attributeName -> {
            Class<?> clazz = Object.class;
            if (attributeMap.get(attributeName) != null) {
                clazz = attributeMap.get(attributeName).getClass();
            }
            var expression = new ValueExpressionLiteral(
                    attributeMap.get(attributeName), clazz);
            mapper.setVariable(attributeName, expression);
        });
        return mapper;
    }

    public static EvaluationContext buildEvaluationContext(
            Map<String, Method> functionMethodMap,
            Map<String, Object> attributeMap) {

        var vMapper = buildVariableMapper(attributeMap);
        var fMapper = buildFunctionMapper(functionMethodMap);
        var context = new EsfingeELContext(fMapper, vMapper,
                new ArrayELResolver(), new ListELResolver(), new MapELResolver(), new BeanELResolver());

        return new EvaluationContext(context, fMapper, vMapper);
    }

    public static Object evaluateExpression(EvaluationContext elContext,
            String expression) {
        var result = new ExpressionFactoryImpl()
                .createValueExpression(elContext, expression, Object.class);

        return result.getValue(elContext);
    }

    public static EvaluationContext buildContext(Object[] params) {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("param", params);
        Map<String, Method> funcMap = new HashMap<>();
        return buildEvaluationContext(funcMap, varMap);
    }

}
