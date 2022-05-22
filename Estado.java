import java.util.*;
import java.io.*;

public class Estado implements Serializable{
  private static int Contador =0;
  private int id;
  private boolean aceptacion;
  private HashSet<Transicion> transiciones = new HashSet<Transicion>();
  private int token;

  public  Estado(){
    this.id = Contador++;
    this.aceptacion=false;
    this.transiciones.clear();
    this.token = -1;
  }

  public void setId(int id){
    this.id=id;
  }
  public int getId(){
    return this.id;
  }
   public void agregarTransicion(Transicion transicion){
    this.transiciones.add(transicion);
  }
  public void setTransiciones(HashSet<Transicion> transiciones){
    this.transiciones=transiciones;
  }
  public HashSet<Transicion> getTransiciones(){
    return this.transiciones;
  }
  public void  setAceptacion (boolean aceptacion){
    this.aceptacion=aceptacion;
  }
  public boolean getAceptacion(){
    return this.aceptacion;
  }

  public void setToken(int token){
      this.token=token;
  }
  public int getToken(){
      return this.token;
  }
}
