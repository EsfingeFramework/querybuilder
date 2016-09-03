package entities;
import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.Id;
import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.Indexed;
import net.sf.esfinge.querybuilder.neo4j.oomapper.annotations.NodeEntity;

@NodeEntity
public class SimplePerson {
	
	@Id
	private String nome;
	private String sobrenome;
	@Indexed
	private int idade;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	@Override
	public String toString(){
		return nome + " " + sobrenome + " " + idade;
	}
}
