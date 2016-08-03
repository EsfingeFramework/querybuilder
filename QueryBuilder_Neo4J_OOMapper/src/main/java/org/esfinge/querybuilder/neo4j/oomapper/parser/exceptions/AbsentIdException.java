package org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions;

@SuppressWarnings("serial")
public class AbsentIdException extends RuntimeException {

	private String message;
	
	public AbsentIdException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
	
}
