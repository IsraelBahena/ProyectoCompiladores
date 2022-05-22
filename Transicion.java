import java.io.Serializable;

public class Transicion implements Serializable{
  private static int contador;
  private  int id ;
  private char simboloInferior;
  private char simboloSuperior;
  private Estado estadoResultado;


    public  Transicion(char simbolo,Estado estado){
      this.id = contador++;
      this.simboloInferior=simbolo;
      this.simboloSuperior=simbolo;
      this.estadoResultado= estado;
    }
    public  Transicion(char simboloInferior,char simboloSuperior,Estado estado){
      this.id = contador++;
      this.simboloInferior=simboloInferior;
      this.simboloSuperior=simboloSuperior;
      this.estadoResultado= estado;
    }
    public  Transicion(){
      this.id = contador++;
      this.estadoResultado= null;
    }
    public void setTransicion(char simbolo,Estado estado){
      this.simboloInferior= simbolo;
      this.estadoResultado= estado;
    }
    public void setTransicion(char simboloInferior, char simboloSuperior,Estado estado){
      this.simboloInferior=simboloInferior;
      this.simboloSuperior=simboloSuperior;
      this.estadoResultado=estado;
    }
    public void setEstadoResultado(Estado estado){
      this.estadoResultado=estado;
    }

    public char getSimbInferior(){
      return this.simboloInferior;
    }

    public char getSimbSuperior(){
      return this.simboloSuperior;
    }

    public Estado getEstadoResultado(char s) {
		if(this.simboloInferior <=s && s<= this.simboloSuperior) {
			return estadoResultado;
		}
		return null;
	}

}
