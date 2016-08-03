package org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions;

@SuppressWarnings("serial")
public class DuplicateIdException extends RuntimeException {

	private String message;
	
	public DuplicateIdException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
	
}
