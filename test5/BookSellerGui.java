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

package test5;

import jade.core.Agent;
import net.sf.clipsrules.jni.*;
import jade.core.AID;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jade.core.behaviours.OneShotBehaviour;
import java.io.File;
//import javax.swing.JFileChooser;
/**
  @author Giovanni Caire - TILAB
 */
class BookSellerGui extends JFrame {	
	private BookSellerAgent myAgent;
	
	private JTextField titleField, priceField;
	private String[] lista1=new String[10];
	private String[] lista2=new String[10];
	private String[] lista3=new String[10];
	int contador=0;
	BookSellerGui(BookSellerAgent a) {
		super(a.getLocalName());
		
		myAgent = a;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		/*p.add(new JLabel("Dame hechos: "));
		titleField = new JTextField(30);
		p.add(titleField);
		p.add(new JLabel("Dame reglas: "));
		priceField = new JTextField(30);
		p.add(priceField);*/
		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton addButton = new JButton("AddFile");
		addButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					lista1[contador]=new String();
					lista1[contador++]="/source/persons/load-persons.clp";
					lista1[contador]=new String();
			        lista1[contador++]="/source/persons/load-persons-rules.clp";
		            
					myAgent.addBehaviour(new MyOneShotBehaviour(lista1,contador));
					
					contador=0;
					lista2[contador]=new String();
					lista2[contador++]="/source/prodcust/load-prod-cust.clp";
					lista2[contador]=new String();
			        lista2[contador++]="/source/prodcust/load-prodcust-rules.clp";
		            
					myAgent.addBehaviour(new MyOneShotBehaviour(lista2,contador));
					
					contador=0;
					lista3[contador]=new String();
					lista3[contador++]="/source/market/templates.clp";
					lista3[contador]=new String();
			        lista3[contador++]="/source/market/persons.clp";
			        lista3[contador]=new String();
					lista3[contador++]="/source/market/facts.clp";
					lista3[contador]=new String();
			        lista3[contador++]="/source/market/rules.clp";
		            
					myAgent.addBehaviour(new MyOneShotBehaviour(lista3,contador));
					contador=0;
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		
		
		p = new JPanel();
		p.add(addButton);
		
		getContentPane().add(p, BorderLayout.SOUTH);
		
		// Make the agent terminate when the user closes 
		// the GUI using the button on the upper right corner	
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myAgent.doDelete();
			}
		} );
		
		setResizable(false);
	}
	
	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
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
		        System.out.println("nombre: ");
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
		        //Environment clips=new Environment();
		        
		        /*System.out.println("carpeta persons");
		        Environment clips, clips2, clips3;
		        clips=new Environment();
		        clips.eval("(clear)");
		        clips.load("C:/jade/src/examples/test/src/test2/persons/load-persons.clp");
		        clips.load("C:/jade/src/examples/test/src\test2/persons/load-persons-rules.clp");
		        clips.eval("(reset)");
		        clips.eval("(facts)");
		        clips.run();
		        System.out.println("carpeta prodcust");
		        clips2=new Environment();
		        clips2.eval("(clear)");
		        clips2.load("C:/jade/src/examples/test/src/test2/prodcust/load-prod-cust.clp");
		        clips2.load("C:/jade/src/examples/test/src/test2/prodcust/load-prodcust-rules.clp");
		        clips2.eval("(reset)");
		        clips2.eval("(facts)");
		        clips2.run();
		        System.out.println("carpeta market");
		        clips3=new Environment();
		        clips3.eval("(clear)");
		        clips3.load("C:/jade/src/examples/test/src/test2/market/templates.clp");
		        clips3.load("C:/jade/src/examples/test/src/test2/market/persons.clp");
		        clips3.load("C:/jade/src/examples/test/src/test2/market/facts.clp");
		        clips3.load("C:/jade/src/examples/test/src/test2/market/rules.clp");
		        clips3.eval("(reset)");
		        clips3.eval("(facts)");
		        clips3.run();*/
		        
		        
		        /*Environment clips=new Environment();
		        clips.eval("(clear)");
		        for(int i=0;i<contador;i++) {
		        	clips.load(lista[i]);
		        }
		        clips.eval("(reset)");
		        clips.eval("(facts)");
		        clips.run();*/
		    } 
		    /*
		    public int onEnd() {
		      myAgent.doDelete();   
		      return super.onEnd();
		    } */
     }
}
