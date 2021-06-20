/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
 *****************************************************************/

package examples.proyecto1;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;

import jade.core.*;
import net.sf.clipsrules.jni.*;
public class AmazonAgent extends Agent {
	// The catalogue of books for sale (maps the title of a book to its price)
	private Hashtable catalogue;
	// The GUI by means of which the user can add books in the catalogue
	private BookSellerGui myGui;
    Environment clips;
	// Put agent initializations here
	protected void setup() {
		// Create the catalogue
		catalogue = new Hashtable();
		
		// Create and show the GUI 
		myGui = new BookSellerGui(this);
		myGui.showGui();

		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		sd.setName("JADE-book-trading");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Add the behaviour serving queries from buyer agents
		addBehaviour(new OfferRequestsServer());

		// Add the behaviour serving purchase orders from buyer agents
		addBehaviour(new PurchaseOrdersServer());
		addBehaviour(new ReferenciaReporte());
		addBehaviour(new CargaDatos());
	}
	
	public void cargar(){
		addBehaviour(new CargaDatos());
	}
	// Put agent clean-up operations here
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Close the GUI
		myGui.dispose();
		// Printout a dismissal message
		System.out.println("Seller-agent "+getAID().getName()+" terminating.");
	}

	/**
     This is invoked by the GUI when the user adds a new book for sale
	 */
	public void updateCatalogue(String nombre, String tipo, String precio, String elementosDisponibles) {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				catalogue.put(nombre, new Integer(precio));
				System.out.println("insertado en catalogo");
				//catalogue.put(title, new Integer(price));
				//System.out.println(title+" inserted into catalogue. Price = "+price);
				AmazonDB amazonDb=new AmazonDB();
				amazonDb.setProductos(nombre, tipo, precio, elementosDisponibles);
				System.out.println("insertado en la base de datos");
				if(tipo.equals("computadora")){
					addBehaviour(new TellComputadora(tipo, precio));
				    addBehaviour(new AskComputadora());
				}
				if(tipo.equals("celular")){
					addBehaviour(new TellCelular(tipo, precio));
				    addBehaviour(new AskCelular());
				}
				if(tipo.equals("saco")){
					addBehaviour(new TellSaco(tipo, precio));
				    addBehaviour(new AskSaco());
				}
				if(tipo.equals("zapatos")){
					addBehaviour(new TellZapatos(tipo, precio));
				    addBehaviour(new AskZapatos());
				}
				if(tipo.equals("zapatos")){
					addBehaviour(new TellPantalla(tipo, precio));
				    addBehaviour(new AskPantalla());
				}
				
			}
		} );
	}

	/**
	   Inner class OfferRequestsServer.
	   This is the behaviour used by Book-seller agents to serve incoming requests 
	   for offer from buyer agents.
	   If the requested book is in the local catalogue the seller agent replies 
	   with a PROPOSE message specifying the price. Otherwise a REFUSE message is
	   sent back.
	 */
	private class OfferRequestsServer extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// CFP Message received. Process it
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();

				Integer price = (Integer) catalogue.get(title);
				if (price != null) {
					// The requested book is available for sale. Reply with the price
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price.intValue()));
				}
				else {
					// The requested book is NOT available for sale.
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	}  // End of inner class OfferRequestsServer

	/**
	   Inner class PurchaseOrdersServer.
	   This is the behaviour used by Book-seller agents to serve incoming 
	   offer acceptances (i.e. purchase orders) from buyer agents.
	   The seller agent removes the purchased book from its catalogue 
	   and replies with an INFORM message to notify the buyer that the
	   purchase has been sucesfully completed.
	 */
	private class PurchaseOrdersServer extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// ACCEPT_PROPOSAL Message received. Process it
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();

				Integer price = (Integer) catalogue.remove(title);
				if (price != null) {
					reply.setPerformative(ACLMessage.INFORM);
					System.out.println(title+" sold to agent "+msg.getSender().getName());
				}
				else {
					// The requested book has been sold to another buyer in the meanwhile .
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	}  // End of inner class OfferRequestsServer

	class ReferenciaReporte extends SimpleBehaviour{

		@Override
		public void action() {
			// TODO Auto-generated method stub
			try {
				addBehaviour(new Reporte());
				//System.out.println("se ejecuto");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}

	}

	class Reporte extends OneShotBehaviour{
		AmazonDB amazonDB;
		public Reporte(){
			amazonDB=new AmazonDB();
		}
		@Override
		public void action() {
			// TODO Auto-generated method stub
			//System.out.println("mensaje que se repite");
			
			
			String cadena = amazonDB.getProdutos();
			//char dosPuntos=(char)58;
			
			cadena=cadena.substring(1);
			String[] arrayDosPuntos = cadena.split(":");
			for(int i=0;i<arrayDosPuntos.length;i++){
				String[] array=arrayDosPuntos[i].split(",");
				int elementosDisponibles= Integer.parseInt(array[4]);
				//si es una computadora y es cero
				if(elementosDisponibles==0 && array[2].equals("computadora")){
					addBehaviour(new OneShotBehaviour(){
						public void action() {
							// TODO Auto-generated method stub
							//System.out.println(getLocalName() +": Preparandose para enviar un mensaje a receptor");
							AID id = new AID();
							id.setLocalName("computadora");
				 
						    // Creación del objeto ACLMessage
							ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
				 
						    //Rellenar los campos necesarios del mensaje
							mensaje.setSender(getAID());
							mensaje.setLanguage("Spanish");
							mensaje.addReceiver(id);
							mensaje.setContent(array[0]+","+array[1]+","+array[2]+","+array[3]+","+array[4]);
				 
						    //Envia el mensaje a los destinatarios
							send(mensaje);
				 
							System.out.println(getLocalName() +": ... se a mandado un mensaje para pedir mas producto");
							System.out.println(mensaje.toString());
							
						}
						
					});
				}
				//si es un celular y es cero
				if(elementosDisponibles==0 && array[2].equals("celular")){
					addBehaviour(new OneShotBehaviour(){
						public void action() {
							// TODO Auto-generated method stub
							//System.out.println(getLocalName() +": Preparandose para enviar un mensaje a receptor");
							AID id = new AID();
							id.setLocalName("celular");
				 
						    // Creación del objeto ACLMessage
							ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
				 
						    //Rellenar los campos necesarios del mensaje
							mensaje.setSender(getAID());
							mensaje.setLanguage("Spanish");
							mensaje.addReceiver(id);
							mensaje.setContent(array[0]+","+array[1]+","+array[2]+","+array[3]+","+array[4]);
				 
						    //Envia el mensaje a los destinatarios
							send(mensaje);
				 
							System.out.println(getLocalName() +": ... se a mandado un mensaje para pedir mas producto");
							System.out.println(mensaje.toString());
							
						}
						
					});
				}
				//si es un saco y es cero
				if(elementosDisponibles==0 && array[2].equals("saco")){
					addBehaviour(new OneShotBehaviour(){
						public void action() {
							// TODO Auto-generated method stub
							//System.out.println(getLocalName() +": Preparandose para enviar un mensaje a receptor");
							AID id = new AID();
							id.setLocalName("saco");
				 
						    // Creación del objeto ACLMessage
							ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
				 
						    //Rellenar los campos necesarios del mensaje
							mensaje.setSender(getAID());
							mensaje.setLanguage("Spanish");
							mensaje.addReceiver(id);
							mensaje.setContent(array[0]+","+array[1]+","+array[2]+","+array[3]+","+array[4]);
				 
						    //Envia el mensaje a los destinatarios
							send(mensaje);
				 
							System.out.println(getLocalName() +": ... se a mandado un mensaje para pedir mas producto");
							System.out.println(mensaje.toString());
							
						}
						
					});
				}
				//si son zapatos y es cero
				if(elementosDisponibles==0 && array[2].equals("zapatos")){
					addBehaviour(new OneShotBehaviour(){
						public void action() {
							// TODO Auto-generated method stub
							//System.out.println(getLocalName() +": Preparandose para enviar un mensaje a receptor");
							AID id = new AID();
							id.setLocalName("zapatos");
				 
						    // Creación del objeto ACLMessage
							ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
				 
						    //Rellenar los campos necesarios del mensaje
							mensaje.setSender(getAID());
							mensaje.setLanguage("Spanish");
							mensaje.addReceiver(id);
							mensaje.setContent(array[0]+","+array[1]+","+array[2]+","+array[3]+","+array[4]);
				 
						    //Envia el mensaje a los destinatarios
							send(mensaje);
				 
							System.out.println(getLocalName() +": ... se a mandado un mensaje para pedir mas producto");
							System.out.println(mensaje.toString());
							
						}
						
					});
				}
				//si es una pantalla y es cero
				if(elementosDisponibles==0 && array[2].equals("pantalla")){
					addBehaviour(new OneShotBehaviour(){

						public void action() {
							// TODO Auto-generated method stub
							//System.out.println(getLocalName() +": Preparandose para enviar un mensaje a receptor");
							AID id = new AID();
							id.setLocalName("pantalla");
				 
						    // Creación del objeto ACLMessage
							ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
				 
						    //Rellenar los campos necesarios del mensaje
							mensaje.setSender(getAID());
							mensaje.setLanguage("Spanish");
							mensaje.addReceiver(id);
							mensaje.setContent(array[0]+","+array[1]+","+array[2]+","+array[3]+","+array[4]);
				 
						    //Envia el mensaje a los destinatarios
							send(mensaje);
				 
							System.out.println(getLocalName() +": ... se a mandado un mensaje para pedir mas producto");
							//System.out.println(mensaje.toString());
							
						}
						
					});
				}
			}
		}

	}
	class CargaDatos extends OneShotBehaviour{

		@Override
		public void action() {
			// TODO Auto-generated method stub
			AlibabaDB alibabaDB=new AlibabaDB();
			String[][] matriz = alibabaDB.dameProductos();
			int contador=alibabaDB.dameContador();
			for(int i=0;i<contador;i++){
				catalogue.put(matriz[i][1], Integer.parseInt(matriz[i][3]));
			}
			
			System.out.println("se cargaron todos los datos");
			
		}

	}
	//desde aqui inica lo de clips

	
	private class TellComputadora extends Behaviour{
		private String tipo;
		private String precio;
		boolean tellBandera=false;
		public TellComputadora(String tipoNuevo,String precioNuevo){
			tipo=tipoNuevo;
			precio=precioNuevo;
		}
		public void action(){
			clips=new Environment();
			char c=(char)34;
            String comillas=String.valueOf(c);
            String regla="(defrule compro-1-producto (product (category computadora)) => (printout t "+comillas+"se compro una computadora tienes descuento de 20% y una silla gamer"+comillas+" crlf))";
            //String regla2="(defrule compro-1-celular (product (price ?precio))(test(> ?precio 4000)) => (printout t "+comillas+"tiene descuento de 10% y a 18 meses sin intereses"+comillas+"))";
            System.out.println("esta es la regla: "+regla);
            clips.eval("(clear)");
            clips.eval("(reset)");
		    clips.build("(deftemplate product (slot category) (slot price))");
			clips.build("(deffacts products (product (category "+tipo+") (price "+precio+")))");
			clips.eval("(reset)");
			clips.build(regla);

			
			//clips.build("(defrule computadora (product (category "+comillas+"computadora"+comillas+") (price ?p)) => (printout t "+comillas+"Tiene un descuento de un 25% en la compra de la computadora"+comillas+" crlf))");
			//clips.build(regla);
            clips.eval("(facts)");
			tellBandera=true;
		}
		public boolean done(){
			if(tellBandera){
				return true;
			}
			else{
				return false;
			}
		}
	}

	private class AskComputadora extends Behaviour{
		boolean askBandera = false;
		public void action(){
			System.out.println("se ejecuto ask");
			//System.out.println("estas son las reglas:");
			clips.eval("(rules)");
			//System.out.println("estos son los resultados de reglas");
			clips.run();
			askBandera = true;
		}
		public boolean done(){
			if(askBandera)
			{
				return true;
			}
			else{
				return false;
			}
		}
		/*
		public int onEnd(){
			myAgent.doDelete();   
		    //return super.onEnd();
			return 0;
		}*/
	}


	private class TellCelular extends Behaviour{
		private String tipo;
		private String precio;
		boolean tellBandera=false;
		public TellCelular(String tipoNuevo,String precioNuevo){
			tipo=tipoNuevo;
			precio=precioNuevo;
		}
		public void action(){
			clips=new Environment();
			char c=(char)34;
            String comillas=String.valueOf(c);
            //String regla="(defrule compro-1-producto (product (category computadora)) => (printout t "+comillas+"se compro una computadora"+comillas+" crlf))";
            String regla2="(defrule compro-1-celular (product (price ?precio))(test(> ?precio 4000)) => (printout t "+comillas+"tiene descuento de 10% y a 18 meses sin intereses"+comillas+"))";
            System.out.println("esta es la regla: "+regla2);
            clips.eval("(clear)");
            clips.eval("(reset)");
		    clips.build("(deftemplate product (slot category) (slot price))");
			clips.build("(deffacts products (product (category "+tipo+") (price "+precio+")))");
			clips.eval("(reset)");
			clips.build(regla2);
			
			
			//clips.build("(defrule computadora (product (category "+comillas+"computadora"+comillas+") (price ?p)) => (printout t "+comillas+"Tiene un descuento de un 25% en la compra de la computadora"+comillas+" crlf))");
			//clips.build(regla);
            clips.eval("(facts)");
			tellBandera=true;
		}
		public boolean done(){
			if(tellBandera){
				return true;
			}
			else{
				return false;
			}
		}
	}

	private class AskCelular extends Behaviour{
		boolean askBandera = false;
		public void action(){
			System.out.println("se ejecuto ask");
			//System.out.println("estas son las reglas:");
			clips.eval("(rules)");
			//System.out.println("estos son los resultados de reglas");
			clips.run();
			askBandera = true;
		}
		public boolean done(){
			if(askBandera)
			{
				return true;
			}
			else{
				return false;
			}
		}
		/*
		public int onEnd(){
			myAgent.doDelete();   
		    //return super.onEnd();
			return 0;
		}*/
	}

	private class TellSaco extends Behaviour{
		private String tipo;
		private String precio;
		boolean tellBandera=false;
		public TellSaco(String tipoNuevo,String precioNuevo){
			tipo=tipoNuevo;
			precio=precioNuevo;
		}
		public void action(){
			clips=new Environment();
			char c=(char)34;
            String comillas=String.valueOf(c);
            //String regla="(defrule compro-1-producto (product (category celular)) => (printout t "+comillas+"se compro una computadora"+comillas+" crlf))";
            //String regla2="(defrule compro-1-celular (product (price ?precio))(test(> ?precio 4000)) => (printout t "+comillas+"tiene descuento de 10% y a 18 meses sin intereses"+comillas+"))";
            String regla3="(defrule compro-1-saco (product (category saco) (price ?precio))(test(> ?precio 500)) => (printout t "+comillas+"se compro un saco y tiene descuento de 10%"+comillas+"))";
            System.out.println("esta es la regla: "+regla3);
            clips.eval("(clear)");
            clips.eval("(reset)");
		    clips.build("(deftemplate product (slot category) (slot price))");
			clips.build("(deffacts products (product (category "+tipo+") (price "+precio+")))");
			clips.eval("(reset)");
			clips.build(regla3);
			
			
			//clips.build("(defrule computadora (product (category "+comillas+"computadora"+comillas+") (price ?p)) => (printout t "+comillas+"Tiene un descuento de un 25% en la compra de la computadora"+comillas+" crlf))");
			//clips.build(regla);
            clips.eval("(facts)");
			tellBandera=true;
		}
		public boolean done(){
			if(tellBandera){
				return true;
			}
			else{
				return false;
			}
		}
	}

	private class AskSaco extends Behaviour{
		boolean askBandera = false;
		public void action(){
			System.out.println("se ejecuto ask");
			//System.out.println("estas son las reglas:");
			clips.eval("(rules)");
			//System.out.println("estos son los resultados de reglas");
			clips.run();
			askBandera = true;
		}
		public boolean done(){
			if(askBandera)
			{
				return true;
			}
			else{
				return false;
			}
		}
		/*
		public int onEnd(){
			myAgent.doDelete();   
		    //return super.onEnd();
			return 0;
		}*/
	}

	private class TellZapatos extends Behaviour{
		private String tipo;
		private String precio;
		boolean tellBandera=false;
		public TellZapatos(String tipoNuevo,String precioNuevo){
			tipo=tipoNuevo;
			precio=precioNuevo;
		}
		public void action(){
			clips=new Environment();
			char c=(char)34;
            String comillas=String.valueOf(c);
            String regla="(defrule compro-1-producto (product (category zapatos)) => (printout t "+comillas+"se compro unos zaptos y tiene descuento del 20% mas el 15% de descuenti"+comillas+" crlf))";
            //String regla2="(defrule compro-1-celular (product (price ?precio))(test(> ?precio 4000)) => (printout t "+comillas+"tiene descuento de 10% y a 18 meses sin intereses"+comillas+"))";
            System.out.println("esta es la regla: "+regla);
            clips.eval("(clear)");
            clips.eval("(reset)");
		    clips.build("(deftemplate product (slot category) (slot price))");
			clips.build("(deffacts products (product (category "+tipo+") (price "+precio+")))");
			clips.eval("(reset)");
			clips.build(regla);

			
			//clips.build("(defrule computadora (product (category "+comillas+"computadora"+comillas+") (price ?p)) => (printout t "+comillas+"Tiene un descuento de un 25% en la compra de la computadora"+comillas+" crlf))");
			//clips.build(regla);
            clips.eval("(facts)");
			tellBandera=true;
		}
		public boolean done(){
			if(tellBandera){
				return true;
			}
			else{
				return false;
			}
		}
	}

	private class AskZapatos extends Behaviour{
		boolean askBandera = false;
		public void action(){
			System.out.println("se ejecuto ask");
			//System.out.println("estas son las reglas:");
			clips.eval("(rules)");
			//System.out.println("estos son los resultados de reglas");
			clips.run();
			askBandera = true;
		}
		public boolean done(){
			if(askBandera)
			{
				return true;
			}
			else{
				return false;
			}
		}
		/*
		public int onEnd(){
			myAgent.doDelete();   
		    //return super.onEnd();
			return 0;
		}*/
	}

	private class TellPantalla extends Behaviour{
		private String tipo;
		private String precio;
		boolean tellBandera=false;
		public TellPantalla(String tipoNuevo,String precioNuevo){
			tipo=tipoNuevo;
			precio=precioNuevo;
		}
		public void action(){
			clips=new Environment();
			char c=(char)34;
            String comillas=String.valueOf(c);
            String regla="(defrule compro-1-producto (product (price ?precio))(test(>= ?precio 6000)) => (printout t "+comillas+"se compro una pantalla tiene descuento de 30%"+comillas+" crlf))";
            //String regla2="(defrule compro-1-celular (product (price ?precio))(test(> ?precio 4000)) => (printout t "+comillas+"tiene descuento de 10% y a 18 meses sin intereses"+comillas+"))";
            System.out.println("esta es la regla: "+regla);
            clips.eval("(clear)");
            clips.eval("(reset)");
		    clips.build("(deftemplate product (slot category) (slot price))");
			clips.build("(deffacts products (product (category "+tipo+") (price "+precio+")))");
			clips.eval("(reset)");
			clips.build(regla);

			
			//clips.build("(defrule computadora (product (category "+comillas+"computadora"+comillas+") (price ?p)) => (printout t "+comillas+"Tiene un descuento de un 25% en la compra de la computadora"+comillas+" crlf))");
			//clips.build(regla);
            clips.eval("(facts)");
			tellBandera=true;
		}
		public boolean done(){
			if(tellBandera){
				return true;
			}
			else{
				return false;
			}
		}
	}

	private class AskPantalla extends Behaviour{
		boolean askBandera = false;
		public void action(){
			System.out.println("se ejecuto ask");
			//System.out.println("estas son las reglas:");
			clips.eval("(rules)");
			//System.out.println("estos son los resultados de reglas");
			clips.run();
			askBandera = true;
		}
		public boolean done(){
			if(askBandera)
			{
				return true;
			}
			else{
				return false;
			}
		}
		/*
		public int onEnd(){
			myAgent.doDelete();   
		    //return super.onEnd();
			return 0;
		}*/
	}
}
