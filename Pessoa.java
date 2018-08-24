@ClassAnnotation(nome="Adriano")
public class Pessoa {
	// Vamos supor que queremos persistir apenas os atributos que tem a nottation persistente
	@Persistente(emMemoria="true")
	public String nome;
	@Persistente(emMemoria="false")
	public String cpf;
	@Persistente(emMemoria="true")
	public String rg;
	public int idade;

	public Pessoa(String nome, String cpf, String rg) {
		this.nome = nome;
		this.cpf = cpf;
	}

	public Pessoa(String nome, String cpf, String rg, int idade) {
		this.nome = nome;
		this.cpf = cpf;
		this.rg = rg;
		this.idade = idade;
	}

	public Pessoa(String nome, String cpf) {
		this.nome = nome;
		this.cpf = cpf;
	}
	

	@MetodoAnnotation(valorDefaultMetodo="teste")
	public String getNome() {
		return nome;
	}
	public String getCpf() {
		return cpf;
	}
	public String getRg() {
		return rg;
	}
	public int getIdade() {
		return idade;
	}
}
