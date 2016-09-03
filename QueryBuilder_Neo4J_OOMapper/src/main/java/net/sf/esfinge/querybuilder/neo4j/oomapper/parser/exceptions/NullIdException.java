package net.sf.esfinge.querybuilder.neo4j.oomapper.parser.exceptions;

@SuppressWarnings("serial")
public class NullIdException extends RuntimeException {

	private String message;
	
	public NullIdException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
	
}
