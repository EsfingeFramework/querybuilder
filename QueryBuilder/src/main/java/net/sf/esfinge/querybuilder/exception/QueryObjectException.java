package net.sf.esfinge.querybuilder.exception;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class QueryObjectException extends RuntimeException{
	private static final long serialVersionUID = 950879232683680083L;

	private Method flawedMethod;
	
	private Type queryObjectClass;
	
	public QueryObjectException(String msg, Throwable cause, Method flawedMethod, Type queryObjectClass){
		super(msg, cause);
		
		this.flawedMethod = flawedMethod;
		this.queryObjectClass = queryObjectClass;
	}

	public Method getFlawedMethod() {
		return flawedMethod;
	}

	public Type getQueryObjectClass() {
		return queryObjectClass;
	}
}
