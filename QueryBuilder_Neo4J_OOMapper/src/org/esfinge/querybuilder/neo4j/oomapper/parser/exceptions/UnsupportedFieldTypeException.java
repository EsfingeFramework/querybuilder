package org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions;

@SuppressWarnings("serial")
public class UnsupportedFieldTypeException extends RuntimeException {

	
	private String message;
	
	public UnsupportedFieldTypeException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
}
