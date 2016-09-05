package net.sf.esfinge.querybuilder.utils;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.el.lang.EvaluationContext;
import org.junit.Test;

import net.sf.esfinge.querybuilder.utils.ELUtils;


public class ELComponentTest {
	
	public enum Level {
	    JUNIOR, PLAIN, SENIOR
	}
    
    public class Bean{
		private String prop1;
		private int prop2;
		private Level level;
		private boolean teste = true;
		public Bean(String prop1, int prop2) {
			super();
			this.prop1 = prop1;
			this.prop2 = prop2;
			level = Level.SENIOR;
		}
		public String getProp1() {
			return prop1;
		}
		public void setProp1(String prop1) {
			this.prop1 = prop1;
		}
		public int getProp2() {
			return prop2;
		}
		public void setProp2(int prop2) {
			this.prop2 = prop2;
		}
		public boolean getPropBoolean() {
		    return true;
		}
		public Level getLevel() {
		    return level;
		}
		public boolean temTeste() {
		    return teste;
		}
		public boolean getTemTeste() {
		    return temTeste();
		}
		public void setLevel(Level level) {
		    this.level = level;
		}
	}
	
	public static String addInicio(String str){
		return "inicio"+str;
	}
	
	public static int soma5(int i){
		return i+5;
	}
	
	@Test
	public void testSimpleEL(){
		Map<String, Object> varMap = new HashMap<String, Object>();
        varMap.put("value", "OK");
        Map<String, Method> funcMap = new HashMap<String, Method>();
        EvaluationContext ctx = ELUtils.buildEvaluationContext(funcMap, varMap);
        
        assertEquals("OK", ELUtils.evaluateExpression(ctx, "#{value}"));
	}
	
	@Test
	public void testBeanEL(){
		Map<String, Object> varMap = new HashMap<String, Object>();
        varMap.put("bean", new Bean("OK",13));
        Map<String, Method> funcMap = new HashMap<String, Method>();
        EvaluationContext ctx = ELUtils.buildEvaluationContext(funcMap, varMap);
        
        assertEquals("OK", ELUtils.evaluateExpression(ctx, "#{bean.prop1}"));
        assertEquals(13, ELUtils.evaluateExpression(ctx, "#{bean.prop2}"));
        assertEquals("OK", ELUtils.evaluateExpression(ctx, "#{bean['prop1']}"));
        assertEquals(true, ELUtils.evaluateExpression(ctx, "#{bean.propBoolean}"));
        assertEquals(13, ELUtils.evaluateExpression(ctx, "#{bean['prop2']}"));
        assertEquals(true, ELUtils.evaluateExpression(ctx, "#{bean.level eq 'SENIOR'}"));
        assertEquals(true, ELUtils.evaluateExpression(ctx, "#{bean.temTeste}"));
	}
	
	@Test
	public void testMapEL(){
		Map<String, Object> varMap = new HashMap<String, Object>();
		
		Map<String,Object> propMap = new HashMap<String, Object>();
		propMap.put("value1", "OK");
		propMap.put("value2", 13);
		
        varMap.put("map", propMap);
        Map<String, Method> funcMap = new HashMap<String, Method>();
        EvaluationContext ctx = ELUtils.buildEvaluationContext(funcMap, varMap);
        
        assertEquals("OK", ELUtils.evaluateExpression(ctx, "#{map.value1}"));
        assertEquals(13, ELUtils.evaluateExpression(ctx, "#{map.value2}"));
        assertEquals("OK", ELUtils.evaluateExpression(ctx, "#{map['value1']}"));
        assertEquals(13, ELUtils.evaluateExpression(ctx, "#{map['value2']}"));
	}
	
	@Test
	public void testArrayEL(){
		Map<String, Object> varMap = new HashMap<String, Object>();
		
		Object[] array = {"OK",13};
		
        varMap.put("array", array);
        Map<String, Method> funcMap = new HashMap<String, Method>();
        EvaluationContext ctx = ELUtils.buildEvaluationContext(funcMap, varMap);
        
        assertEquals("OK", ELUtils.evaluateExpression(ctx, "#{array[0]}"));
        assertEquals(13, ELUtils.evaluateExpression(ctx, "#{array[1]}"));
	}
	
	@Test
	public void testListEL(){
		Map<String, Object> varMap = new HashMap<String, Object>();
		
		List<Object> list = new ArrayList<Object>();
		list.add("OK");
		list.add(13);
		
        varMap.put("list", list);
        Map<String, Method> funcMap = new HashMap<String, Method>();
        EvaluationContext ctx = ELUtils.buildEvaluationContext(funcMap, varMap);
        
        assertEquals("OK", ELUtils.evaluateExpression(ctx, "#{list[0]}"));
        assertEquals(13, ELUtils.evaluateExpression(ctx, "#{list[1]}"));
	}
	
	@Test
	public void testMethodsEL() throws SecurityException, NoSuchMethodException{
		Map<String, Object> varMap = new HashMap<String, Object>();
		varMap.put("bean", new Bean("OK",13));
        Map<String, Method> funcMap = new HashMap<String, Method>();
        funcMap.put("addInicio",this.getClass().getMethod("addInicio", String.class));
        funcMap.put("soma5",this.getClass().getMethod("soma5", int.class));
        EvaluationContext ctx = ELUtils.buildEvaluationContext(funcMap, varMap);
        
        assertEquals("inicioOK", ELUtils.evaluateExpression(ctx, "#{addInicio(bean.prop1)}"));
        assertEquals(18, ELUtils.evaluateExpression(ctx, "#{soma5(bean.prop2)}"));
	}

}
