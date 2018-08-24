import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Persistente {
	// Dessa maneira deixamos como default os valores das anotações, e assim não somos obrigados
	// a informar no momento de usar, caso contrário teriámos erro de compilação se não informassemos
	public String emMemoria() default "false";
	public String emBanco() default "true";
}
