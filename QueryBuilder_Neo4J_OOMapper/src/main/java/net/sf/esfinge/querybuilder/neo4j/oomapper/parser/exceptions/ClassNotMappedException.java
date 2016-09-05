package net.sf.esfinge.querybuilder.neo4j.oomapper.parser.exceptions;

@SuppressWarnings("serial")
public class ClassNotMappedException extends RuntimeException{

	private String sourceClassName;
	
	public ClassNotMappedException(Class<?> sourceClass){
		this.sourceClassName = sourceClass.getName();
	}
	
	@Override
	public String getMessage(){
		return sourceClassName;
	}
}
