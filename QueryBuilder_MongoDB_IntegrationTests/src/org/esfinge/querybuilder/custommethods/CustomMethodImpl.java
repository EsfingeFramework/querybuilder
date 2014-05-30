package org.esfinge.querybuilder.custommethods;

public class CustomMethodImpl implements CustomMethodInterface{
	
	public static boolean methodInvoked = false;

	@Override
	public void customMethod() {
		methodInvoked = true;		
	}

}
