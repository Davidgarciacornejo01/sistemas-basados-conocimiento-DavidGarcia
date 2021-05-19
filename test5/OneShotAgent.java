package test5;
import net.sf.clipsrules.jni.*;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.io.File;

public class OneShotAgent extends Agent {
	private String[] lista1=new String[10];
	private String[] lista2=new String[10];
	private String[] lista3=new String[10];
	int contador=0;
  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");
    lista1[contador]=new String();
	lista1[contador++]="/source/persons/load-persons.clp";
	lista1[contador]=new String();
    lista1[contador++]="/source/persons/load-persons-rules.clp";
    
	addBehaviour(new MyOneShotBehaviour(lista1,contador));
	
	contador=0;
	lista2[contador]=new String();
	lista2[contador++]="/source/prodcust/load-prod-cust.clp";
	lista2[contador]=new String();
    lista2[contador++]="/source/prodcust/load-prodcust-rules.clp";
    
	addBehaviour(new MyOneShotBehaviour(lista2,contador));
	
	contador=0;
	lista3[contador]=new String();
	lista3[contador++]="/source/market/templates.clp";
	lista3[contador]=new String();
    lista3[contador++]="/source/market/persons.clp";
    lista3[contador]=new String();
	lista3[contador++]="/source/market/facts.clp";
	lista3[contador]=new String();
    lista3[contador++]="/source/market/rules.clp";
    
	addBehaviour(new MyOneShotBehaviour(lista3,contador));
	contador=0;
    //addBehaviour(new MyOneShotBehaviour());
  } 
  
  private class MyOneShotBehaviour extends OneShotBehaviour {
	  private String[] arrayRutas;
	  private int contadorOneShot;
	  public MyOneShotBehaviour(String[] array,int cont) {
		  this.arrayRutas=array;
		  this.contadorOneShot=cont;
	  }
    public void action() {
        System.out.println("Agent's action method executed");
        File file=new File("");
        System.out.println(file.getAbsolutePath());
        //aqui use un poco de codigo ascci para poder lograr que me reconozca el caracter tipo cadena casteandolo a string
        int n1=92;
        int n2=47;
        String ruta=file.getAbsoluteFile().toString();
        String rutaModificada = ruta.replace((char)n1,(char)n2);
        System.out.println(rutaModificada);
        Environment clips=new Environment();
        clips.eval("(clear)");
        for(int i=0;i<contadorOneShot;i++) {
        	clips.load(rutaModificada + arrayRutas[i]);
        }
        clips.eval("(reset)");
        clips.eval("(facts)");
        clips.run();
    } 
    /*
    public int onEnd() {
      myAgent.doDelete();   
      return super.onEnd();
    }*/ 
  }    // END of inner class ...Behaviour
}
