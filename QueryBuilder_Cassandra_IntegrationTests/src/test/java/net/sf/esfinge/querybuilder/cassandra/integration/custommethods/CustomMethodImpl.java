package net.sf.esfinge.querybuilder.cassandra.integration.custommethods;

public class CustomMethodImpl implements CustomMethodInterface{
	
	public static boolean methodInvoked = false;

	@Override
	public void customMethod() {
		methodInvoked = true;		
	}

}
