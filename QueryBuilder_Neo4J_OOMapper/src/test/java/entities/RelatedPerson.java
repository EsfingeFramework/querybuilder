package entities;

import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.Id;
import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.NodeEntity;
import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.RelatedTo;

@NodeEntity
public class RelatedPerson {
	
	@Id
	private String nome;

	@RelatedTo(targetClass = SimplePerson.class)
	private SimplePerson amigo;

	public SimplePerson getAmigo() {
		return amigo;
	}

	public void setAmigo(SimplePerson amigo) {
		this.amigo = amigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
