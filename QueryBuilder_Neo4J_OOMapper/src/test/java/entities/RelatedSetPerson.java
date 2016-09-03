package entities;

import java.util.Set;

import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.Id;
import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.NodeEntity;
import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.RelatedTo;

@NodeEntity
public class RelatedSetPerson {

	@Id
	private String nome;
	
	@RelatedTo(targetClass = SimplePerson.class)
	private Set<SimplePerson> amigos;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<SimplePerson> getAmigos() {
		return amigos;
	}

	public void setAmigos(Set<SimplePerson> amigos) {
		this.amigos = amigos;
	}
	
	
}
