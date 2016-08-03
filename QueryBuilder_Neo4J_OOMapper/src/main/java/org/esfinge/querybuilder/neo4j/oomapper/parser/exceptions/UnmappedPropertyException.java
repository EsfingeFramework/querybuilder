package org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions;

@SuppressWarnings("serial")
public class UnmappedPropertyException extends RuntimeException{

	private String message;
	
	public UnmappedPropertyException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
	
}
