import java.util.Arrays;
import java.util.HashSet;
public class ConjuntoIJ{
    public static int contador =0;
    private int id ;
    private HashSet<Estado> estadosAFD = new HashSet<Estado>();
    private int[] transiciones= new int[256];

    public ConjuntoIJ(){
        id= contador++;
        estadosAFD.clear();
        Arrays.fill(transiciones,-1);
    }
    public ConjuntoIJ(HashSet<Estado> estadosAFN){
        id= contador++;
        this.estadosAFD.addAll(estadosAFD);
        Arrays.fill(transiciones,-1);
    }
    public int[] getTransiciones(){
        return transiciones;
    }

    public HashSet<Estado> getEstadosAFD(){
        return estadosAFD;
    }
    public int getId(){
        return id;
    }
    public void setTransicion(char simbolo,int estadoAFD){
        transiciones[(int)simbolo]=estadoAFD;
    }
    public void setToken(int token){
        transiciones[255]=token;
    }
}
