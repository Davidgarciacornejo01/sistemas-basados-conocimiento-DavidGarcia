package examples.proyecto1;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
public class SupplierAgentSaco extends Agent{
    AlibabaDB alibabaDB;
    AmazonDB amazonDB;
    BarnesNobleDB barnesNobleDB;
    protected void setup() {
        alibabaDB=new AlibabaDB();
        amazonDB=new AmazonDB();
        barnesNobleDB=new BarnesNobleDB();
        addBehaviour(new ReceptorComportaminento());
    }
    private class ReceptorComportaminento extends SimpleBehaviour {
        private boolean fin = false;
    
        public void action() {
            //System.out.println(" Preparandose para recibir");
            
            //alibabaDB.getProdutos();
            //Obtiene el primer mensaje de la cola de mensajes
            ACLMessage mensaje = receive();
            
            if (mensaje!= null) {
                System.out.println(getLocalName() + ": acaba de recibir el siguiente mensaje: ");
                String nombreServidor="";
                String servidor=mensaje.toString();
                int respuestaAlibaba=servidor.indexOf("alibaba");
                if(respuestaAlibaba==-1){
                    System.out.println("palabra no encontrada");
                }
                else{
                    System.out.println("palabra encontrada");
                    nombreServidor="alibaba";
                }
                int respuestaAmazon=servidor.indexOf("amazon");
                if(respuestaAmazon==-1){
                    System.out.println("palabra no encontrada");
                }
                else{
                    System.out.println("palabra encontrada");
                    nombreServidor="amazon";
                }
                int respuestaBarnes=servidor.indexOf("barnes");
                if(respuestaBarnes==-1){
                    System.out.println("palabra no encontrada");
                }
                else{
                    System.out.println("palabra encontrada");
                    nombreServidor="barnes";
                }
                System.out.println("nombre del servidor: "+nombreServidor);
                
                //System.out.println(mensaje.toString());
                String copiaMensaje=mensaje.toString();
                
                char c=(char)34;
                String comillas=String.valueOf(c);
                copiaMensaje=copiaMensaje.replace("\n","");
                copiaMensaje=copiaMensaje.replace(comillas,";");
                //System.out.println(copiaMensaje);
                String[] array=copiaMensaje.split(";");
                //System.out.println("despues: "+respuestaAlibaba);
                /*
                for(String linea : array){
                    System.out.println(linea);
                }*/
                String cadena=array[1];
                //cadena=cadena.substring(1);
                System.out.println("\n\n\n\n\n"+cadena);
                String[] palabras=cadena.split(",");
                
                if(nombreServidor.equals("alibaba"))
                {
                    System.out.println("entro para enviar el mensaje al servidor");
                    alibabaDB.updateProductos(5, palabras[0], palabras[2]);
                }
                if(nombreServidor.equals("amazon")){
                    System.out.println("entro para enviar el mensaje al servidor");
                    //amazonDB.updateProductos(5, palabras[0], palabras[2]);
                    amazonDB.updateProductos(5, palabras[0], palabras[2]);
                }
                if(nombreServidor.equals("barnes")){
                    System.out.println("entro para enviar el mensaje al servidor");
                    barnesNobleDB.updateProductos(5, palabras[0], palabras[2]);
                    
                }
                System.out.println("\n\n\n\n\n\n");

                //fin = true;
            }
        }

        public boolean done() {
            return fin;
        }
    }

}