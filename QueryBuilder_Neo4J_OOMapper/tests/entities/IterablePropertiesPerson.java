package entities;

import java.util.LinkedList;

import org.esfinge.querybuilder.neo4j.oomapper.annotations.Id;
import org.esfinge.querybuilder.neo4j.oomapper.annotations.NodeEntity;

@NodeEntity
public class IterablePropertiesPerson {

	@Id
	private String nome;
	
	private LinkedList<Integer> numeros;
	private float[] floats;
	private Integer[] integers;

	public LinkedList<Integer> getNumeros() {
		return numeros;
	}

	public void setNumeros(LinkedList<Integer> numeros) {
		this.numeros = numeros;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public float[] getFloats() {
		return floats;
	}

	public void setFloats(float[] floats) {
		this.floats = floats;
	}

	public Integer[] getIntegers() {
		return integers;
	}

	public void setIntegers(Integer[] integers) {
		this.integers = integers;
	}
	
}
