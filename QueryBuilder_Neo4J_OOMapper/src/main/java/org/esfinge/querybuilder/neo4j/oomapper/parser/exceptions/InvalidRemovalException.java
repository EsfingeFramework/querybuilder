package org.esfinge.querybuilder.neo4j.oomapper.parser.exceptions;

@SuppressWarnings("serial")
public class InvalidRemovalException extends RuntimeException {

	private String message;
	
	public InvalidRemovalException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
	
}
