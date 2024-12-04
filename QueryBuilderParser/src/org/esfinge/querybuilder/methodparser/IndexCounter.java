package org.esfinge.querybuilder.methodparser;

import java.util.List;

public class IndexCounter {
	
	private int counter = 0;
	
	public int get(){
		return counter;
	}
	
	public int add(int num){
		counter+=num;
		return counter;
	}
	
	public int increment(){
		counter++;
		return counter;
	}
	
	public <E> E get(List<E> list){
		return list.get(counter);
	}

}
