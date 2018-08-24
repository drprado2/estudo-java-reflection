// Aqui temos todo o framework contendo as APIs para fazer reflection
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

public class RefletirPessoa {
	
	public static void main(String ...args){
		try{
			
			// Class é o principal tipo do reflection, ele representa os metadados de uma classe do java
			// Como praticamente tudo são classes, ele representa os metadados de tudo
			// ======== MANEIRAS DE OBTER O CLASS DE UMA CLASSE =====
			// 1 Class.forName("package.NomeClass") // Atravéz do nome absolute da classe
			// 2 variableInstanciaDeAlgo.getClass() // Atravéz de uma instancia
			// 3 MinhaClass.class // Usando diretamente a classe
			
			
			trabalhandoComObjetosDinamicamente();
		}catch (Exception e) {
			System.out.format("Ocorreu um erro \n %s \n %s", e.getMessage(), e.getStackTrace());
		}
	}
	
	public static void imprimirInformacoes() throws ClassNotFoundException{

		Class pessoaCl = Class.forName("estudo.reflection.Pessoa");
		
		// Lendo informações
		// ============ METODOS ===============
		
		// Obter todos os métodos
		Method[] methods = pessoaCl.getDeclaredMethods();
		
		System.out.println("Imprimindo MÉTODOS da classe Pessoa \n");
		Arrays.asList(methods).forEach(System.out::println);
		System.out.println("\nAnotações dos métodos \n");
		Arrays.asList(methods).forEach(m -> {
			// Obtendo anotações do método
			if(m.getAnnotations().length == 0)
				return;

			System.out.format("Método: %s ---", m.getName());
			Arrays.asList(m.getAnnotations()).forEach(a -> System.out.format("Anotação: %s \n", a));
		});

		// ============ CONSTRUCTORS ===============
		
		// Obtendo construtores
		System.out.println("\nImprimindo CONSTRUTORES da classe Pessoa \n");
		Arrays.asList(pessoaCl.getConstructors()).forEach(System.out::println);

		// ============ ANNOTATIONS ===============
		
		// Obtendo anotações da classe
		System.out.println("\nImprimindo ANNOTATIONS da classe Pessoa \n");
		Arrays.asList(pessoaCl.getAnnotations()).forEach(System.out::println);

		// ============ FIELDS ===============
		
		// obtendo variáveis de estado
		System.out.println("\nImprimindo FIELDS da classe Pessoa \n");
		Arrays.asList(pessoaCl.getDeclaredFields()).stream().map(p -> p.getName()).forEach(System.out::println);
	}
	
	public static void testesComInstancias(){
		
		// Aqui instanciamos em uma referência de Pessoa
		Pessoa adriano = new Pessoa("Adriano", "08657287986", "105033389", 24);
		// Aqui instanciamos em uma referência de  Object (Polimorfismo)
		Object obj = new Pessoa("Adriano", "08657287986", "105033389", 24);

		// Podemos verificar que via reflection mesmo aquele que está em uma referência de Object
		// Ainda é identificado ccomo Pessoa
		Class clA = adriano.getClass();
		Class cObj = obj.getClass();
	}
	
	public static void testeComNotations(){
		Pessoa pessoa = new Pessoa("Adriano", "1010", "2020", 25);
		
		Class cp = Pessoa.class;
		Arrays.asList(cp.getDeclaredFields()).forEach(f -> {
			// Aqui verificamos se um determinado field possui uma notation
			// Lembre-se que notations são armazenadas em alvoz específicos
			// Classe, métodos, fields... então você tem como ler notation nesses caras
			if(f.isAnnotationPresent(Persistente.class))
				System.out.format("O field: %s deve ser persistido \n", f.getName());

			// Aqui na segunda condição acessamos o valor da notation
			if((f.isAnnotationPresent(Persistente.class)) && (f.getAnnotation(Persistente.class).emMemoria().equals("true")))
				System.out.format("O field: %s deve ser persistido em memória apenas \n", f.getName());
		});
	}
	
	public static void trabalhandoComObjetosDinamicamente(){
		// Gerando uma instancia válida da classe
		Pessoa b = gerarInstanciaDefault(Pessoa.class);
		// Setando todos os fields de String com um valor default
		setarFieldsStringsCom("Funcionou reflect doido", Pessoa.class, b);
	}
	
	public static <T extends Object> void setarFieldsStringsCom(String valor, Class<T> classe, T obj){
		// Obtemos todos os fields, veja que tem a palavra "Declared", isso faz com que só
		// sejam pegas os fields da própria classe não das classes base
		// Isso tem nos métodos também
		Arrays.asList(classe.getDeclaredFields())
			.stream()
			// getType é usado para pegar a Class do field, e ai verificamos se é uma String
			.filter(f -> f.getType().equals(String.class))
			.forEach(f -> {
				try {
					// Com set, setamos o valor do field em uma instancia desse tipo,
					// Atenção se o field for private dará uma exception em runtime, na hora q o set acontecer
					f.set(obj, valor);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			});
	}
	
	// Esse método tenta gerar uma instancia da classe setando os valores default das variáveis
	// Ele é recursivo, ele pega o construtor com menos argumentos, e tenta gerar uma instancia
	// de cada argumento de forma recursiva com esse mesmo método
	public static <T extends Object> T gerarInstanciaDefault(Class<T> classe) {
		
		try{
			Constructor<T> defaultConstructor = obterContrutorDefault(classe);
			
			// gerParameterCount retorna o número de argumentos do construtor
			if(defaultConstructor.getParameterCount() == 0)
				// newInstance recebe um array com os argumentos, e gera uma nova instancia da classe
				return defaultConstructor.newInstance(new Object[0]);
			
			Object[] args = new Object[defaultConstructor.getParameterCount()];
			
			for(int i = 0; i < args.length; i++){
				// getParameters retorna um array com os argumentos que o contrutor solicita
				Parameter arg = defaultConstructor.getParameters()[i];
				Object instanciaArg = gerarInstanciaDefault(arg.getType());
				args[i] = instanciaArg;
			}
			
			return defaultConstructor.newInstance(args);
			
		}catch(Throwable e){
			System.out.println("Ocorreu um erro");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			return null;
		}
	}
	
	// Método que retorna o construtor que pede menos argumentos
	public static <T extends Object> Constructor<T> obterContrutorDefault(Class<T> classe) {
		// getConstructors retorna todos os construtores
		List<Constructor<?>> constructors = Arrays.asList(classe.getConstructors());
		constructors.sort((c1, c2) -> c1.getParameterCount() < c2.getParameterCount() ? -1 : 1);
		return (Constructor<T>) constructors.get(0);
	}
}
