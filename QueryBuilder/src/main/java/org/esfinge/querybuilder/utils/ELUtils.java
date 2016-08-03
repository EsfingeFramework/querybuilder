package org.esfinge.querybuilder.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.FunctionMapper;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

import org.apache.el.ExpressionFactoryImpl;
import org.apache.el.ValueExpressionLiteral;
import org.apache.el.lang.EvaluationContext;
import org.apache.el.lang.FunctionMapperImpl;
import org.apache.el.lang.VariableMapperImpl;

public class ELUtils {

    public static FunctionMapper buildFunctionMapper(
            Map<String, Method> functionMethodMap) {
        FunctionMapperImpl mapper = new FunctionMapperImpl();
        for (String functionName : functionMethodMap.keySet()) {
            mapper.addFunction("", functionName,
                    functionMethodMap.get(functionName));
        }
        return mapper;
    }

    public static VariableMapper buildVariableMapper(
            Map<String, Object> attributeMap) {
        VariableMapperImpl mapper = new VariableMapperImpl();
        for (String attributeName : attributeMap.keySet()) {
            Class<?> clazz = Object.class;
            if (attributeMap.get(attributeName) != null)
                clazz = attributeMap.get(attributeName).getClass();
            ValueExpressionLiteral expression = new ValueExpressionLiteral(
                    attributeMap.get(attributeName), clazz);
            mapper.setVariable(attributeName, expression);
        }
        return mapper;
    }

    public static EvaluationContext buildEvaluationContext(
            Map<String, Method> functionMethodMap,
            Map<String, Object> attributeMap) {

        VariableMapper vMapper = buildVariableMapper(attributeMap);
        FunctionMapper fMapper = buildFunctionMapper(functionMethodMap);

        EsfingeELContext context = new EsfingeELContext(fMapper, vMapper,
        		new ArrayELResolver(), new ListELResolver(), new MapELResolver(), new BeanELResolver());

        return new EvaluationContext(context, fMapper, vMapper);
    }

    public static Object evaluateExpression(EvaluationContext elContext,
            String expression) {
        ValueExpression result = new ExpressionFactoryImpl()
                .createValueExpression(elContext, expression, Object.class);

        return result.getValue(elContext);
    }
    
    public static EvaluationContext buildContext(Object[] params) {
        Map<String, Object> varMap = new HashMap<String, Object>();
        varMap.put("param", params);
        Map<String, Method> funcMap = new HashMap<String, Method>();
        return buildEvaluationContext(funcMap, varMap);
    }
    
}