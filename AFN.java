import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import java.lang.*;

public class AFN{

    public static int contadorIdAFN;
    private int id;
    private HashSet<Estado> nodos = new HashSet<Estado>();
    private Estado estadoInicial;
    private HashSet<Character> alfabeto = new HashSet<Character>();
    private HashSet<Estado> estadosDeAceptacion = new HashSet<Estado>();

    public void AFN (){
        this.id=contadorIdAFN++;
        this.estadoInicial= null;
        this.alfabeto.clear();
        this.nodos.clear();
        this.estadosDeAceptacion.clear();
    }

    public void NuevoAFNBasico(char simbolo){
        this.id= contadorIdAFN++;
        Estado e1= new Estado();
        Estado e2 = new Estado();
        Transicion transicion= new Transicion();
        transicion.setTransicion(simbolo,e2);
        e1.agregarTransicion(transicion);
        this.alfabeto.add(simbolo);
        this.estadoInicial=e1;
        e2.setAceptacion(true);
        this.nodos.add(e1);
        this.nodos.add(e2);
        this.estadosDeAceptacion.add(e2);
    }

    public void NuevoAFNBasico(char simboloInferior,char simboloSuperior){
        this.id= contadorIdAFN++;
        Estado e1= new Estado();
        Estado e2 = new Estado();
        Transicion transicion= new Transicion();
        transicion.setTransicion(simboloInferior,simboloSuperior,e2);
        e1.agregarTransicion(transicion);
        AgregarSimbolosAlfabeto(simboloInferior,simboloSuperior);
        this.estadoInicial=e1;
        e2.setAceptacion(true);
        nodos.add(e1);
        nodos.add(e2);
        estadosDeAceptacion.add(e2);
    }

    public void AgregarSimbolosAlfabeto(char simboloInferior,char simboloSuperior){
        String intervalo=String.valueOf(simboloInferior+simboloSuperior);
        for(int i=intervalo.codePointAt(0);i<=intervalo.codePointAt(1);i++){
            alfabeto.add((char)i);
        }
    }

    public Estado getEstadoInicial(){
        return this.estadoInicial;
    }

    public AFN unirThompsonAFN(AFN afn2){
        Estado e1 = new Estado();
		Estado e2 = new Estado();

		Transicion t1 = new Transicion(SimbolosEspeciales.EPSILON, this.getEstadoInicial());
		Transicion t2 = new Transicion(SimbolosEspeciales.EPSILON, afn2.getEstadoInicial());
		e1.agregarTransicion(t1);
		e1.agregarTransicion(t2);

		for(Estado e: this.estadosDeAceptacion) {
			e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));
			e.setAceptacion(false);
		}
		for(Estado e: afn2.estadosDeAceptacion) {
			e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));
			e.setAceptacion(false);
		}
		this.estadosDeAceptacion.clear();
		afn2.estadosDeAceptacion.clear();
		this.estadoInicial = e1;
		e2.setAceptacion(true);
		this.estadosDeAceptacion.add(e2);
		this.nodos.addAll(afn2.nodos);
		this.nodos.add(e1);
		this.alfabeto.addAll(afn2.alfabeto);
		return this;
    }

    public AFN concatenarThompsonAFN(AFN afn2){
        for(Transicion t: afn2.estadoInicial.getTransiciones()) {
			for(Estado e: this.estadosDeAceptacion) {
				e.agregarTransicion(t);
				e.setAceptacion(false);
			}
		}
		afn2.nodos.remove(afn2.estadoInicial);
		this.estadosDeAceptacion = afn2.estadosDeAceptacion;
		this.nodos.addAll(afn2.nodos);
		this.alfabeto.addAll(afn2.alfabeto);

		return this;
    }

    public AFN cerraduraPositiva(){
        Estado e1 = new Estado();
		Estado e2 = new Estado();

		e1.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, this.estadoInicial));
		for(Estado e: this.estadosDeAceptacion) {
			e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));
			e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, this.estadoInicial));
			e.setAceptacion(false);
		}
		e2.setAceptacion(true);

		this.estadoInicial = e1;
		this.estadosDeAceptacion.clear();
		this.estadosDeAceptacion.add(e2);
		this.nodos.add(e1);
		this.nodos.add(e2);

		return this;
    }

    public AFN cerraduraKleene(){
        Estado e1 = new Estado();
		Estado e2 = new Estado();

		e1.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, this.estadoInicial));
		for(Estado e: estadosDeAceptacion) {
			e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));
			e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, this.estadoInicial));
			e.setAceptacion(false);
		}
		e2.setAceptacion(true);
		e1.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));

		estadoInicial = e1;
		nodos.add(e1);
		nodos.add(e2);
		estadosDeAceptacion.clear();
		estadosDeAceptacion.add(e2);

		return this;

    }

    public AFN cerraduraOpcional(){
        Estado e1 = new Estado();
		Estado e2 = new Estado();

		e1.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, estadoInicial));
		for(Estado e: estadosDeAceptacion) {
			e.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));
			e.setAceptacion(false);
		}
		e2.setAceptacion(true);
		e1.agregarTransicion(new Transicion(SimbolosEspeciales.EPSILON, e2));

		this.estadoInicial = e1;
		this.nodos.add(e1);
		this.nodos.add(e2);
		this.estadosDeAceptacion.clear();
		this.estadosDeAceptacion.add(e2);

		return this;

    }

    public HashSet<Estado> cerraduraEpsilon(Estado estado){
        HashSet<Estado> estados = new HashSet<Estado>();
        Stack<Estado> stackDeEstados = new Stack<Estado>();
		Estado aux;
		stackDeEstados.push(estado);

		while(!stackDeEstados.empty()) {
			aux = stackDeEstados.pop();
            estados.add(aux);
			for(Transicion t: aux.getTransiciones()) {
				if(!estados.contains(t.getEstadoResultado(SimbolosEspeciales.EPSILON)) && t.getEstadoResultado(SimbolosEspeciales.EPSILON)!=null) {
					stackDeEstados.push(t.getEstadoResultado(SimbolosEspeciales.EPSILON));
				}
			}
		}
        return estados;
    }

    public HashSet<Estado> cerraduraEpsilon(HashSet<Estado> conjuntoestados){
        HashSet<Estado> estados = new HashSet<Estado>();
        Stack<Estado> stackDeEstados = new Stack<Estado>();
		Estado aux;
        for( Estado e :conjuntoestados){
            stackDeEstados.push(e);
        }

		while(!stackDeEstados.empty()) {
			aux = stackDeEstados.pop();
            estados.add(aux);
			for(Transicion t: aux.getTransiciones()) {
				if(!estados.contains(t.getEstadoResultado(SimbolosEspeciales.EPSILON)) && t.getEstadoResultado(SimbolosEspeciales.EPSILON)!=null) {
					stackDeEstados.push(t.getEstadoResultado(SimbolosEspeciales.EPSILON));
				}
			}
		}
        return estados;
    }

    public HashSet<Estado> moverA(Estado estado, char simbolo){
		HashSet<Estado> salidaEstados = new HashSet<Estado>();
		Estado estadoAux;

		for(Transicion t: estado.getTransiciones()) {
			estadoAux = t.getEstadoResultado(simbolo);
			if(estadoAux !=null) {
				salidaEstados.add(estadoAux);
			}
		}

		return salidaEstados;
	}

    public HashSet<Estado> moverA(HashSet<Estado> estados, char simbolo){
		HashSet<Estado> salidaEstados = new HashSet<Estado>();
		Estado aux;

		for(Estado e: estados) {
			for(Transicion t: e.getTransiciones()) {
				aux = t.getEstadoResultado(simbolo);
				if(aux !=null) {
					salidaEstados.add(aux);
				}
			}
		}

		return salidaEstados;
	}

    public HashSet<Estado> IrA(HashSet<Estado> estados, char simbolo ){
        HashSet<Estado> estadosresultado = new HashSet<Estado>();
        estadosresultado.clear();
        estadosresultado.addAll(cerraduraEpsilon(moverA(estados,simbolo)));

        return estadosresultado;
    }

    public AFD ConvertirAFNtoAFD (){
        AFD afdresultante = new AFD();
        int CardinalidadAlfabeto, numeroEstadosAFD;
        int i,j,r;
        char[] Alfabeto;
        HashSet<ConjuntoIJ> conjuntosIJ= new HashSet<ConjuntoIJ>();
        Stack<ConjuntoIJ> stackDeI_n = new Stack<ConjuntoIJ>();
        // primero cerradura epsilon del estado estadoInicial
        ConjuntoIJ conjuntoAnalizando = new ConjuntoIJ(cerraduraEpsilon(this.estadoInicial));
        HashSet<Estado> EstadosAux = new HashSet<Estado>();
        boolean existe =false;
        stackDeI_n.push(conjuntoAnalizando);
        while(!stackDeI_n.empty()){
            conjuntoAnalizando = stackDeI_n.pop();
            conjuntosIJ.add(conjuntoAnalizando);
            for(char simbolo: this.alfabeto){
                EstadosAux=IrA(conjuntoAnalizando.getEstadosAFD(),simbolo);
                if(EstadosAux.isEmpty())
                ///////////////////////////    return;
                existe=false;
                for(ConjuntoIJ C :conjuntosIJ ){
                    if (EstadosAux.equals(C.getEstadosAFD())) { //si no existe el conjunto IJ entonces se agrega
                        existe=true;
                        conjuntoAnalizando.setTransicion(simbolo,C.getId());
                        for(Estado estado: EstadosAux){
                            if(estado.getAceptacion())
                                conjuntoAnalizando.setToken(estado.getToken());
                        }
                        break;
                    }
                }
                if(!existe){
                    ConjuntoIJ ConjuntoIJNuevo = new ConjuntoIJ(EstadosAux);
                    conjuntoAnalizando.setTransicion(simbolo,ConjuntoIJNuevo.getId());
                    for(Estado estado: EstadosAux){
                        if(estado.getAceptacion())
                            conjuntoAnalizando.setToken(estado.getToken());
                    }
                }
            }

        }

        return afdresultante;
    }




}
