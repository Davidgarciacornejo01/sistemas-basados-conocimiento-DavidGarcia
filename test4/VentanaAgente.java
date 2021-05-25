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

package test4;

//import jade.core.*; //esta linea agregue
import jade.core.AID;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.sf.clipsrules.jni.*;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.*;

/**
  @author Giovanni Caire - TILAB
 */
class VentanaAgente extends JFrame {	
	Environment clips;
	private AgenteConocimiento myAgent;
	
	private JTextField titleField, priceField;
	
	VentanaAgente(AgenteConocimiento a) {
		super(a.getLocalName());
		clips=new Environment();
		myAgent = a;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		p.add(new JLabel("Dame hechos: "));
		titleField = new JTextField(30);
		p.add(titleField);
		p.add(new JLabel("Dame reglas: "));
		priceField = new JTextField(30);
		p.add(priceField);
		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String hechosT = titleField.getText().trim();
					String reglasT = priceField.getText().trim();
					//myAgent.addBehaviour(new MyOneShotBehaviour(hechosT,reglasT));
					myAgent.addBehaviour(new Tell(hechosT,reglasT));
					myAgent.addBehaviour(new Ask());
					//OneShotAgent oneShotAgent=new OneShotAgent(myAgent,hechos,reglas);
					
					//Principal principal=new Principal();
					//principal.setDatooRegla(hechos,reglas);
					
					//myAgent.updateCatalogue(title, Integer.parseInt(price));
					titleField.setText("");
					priceField.setText("");
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(VentanaAgente.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
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
	
	private class Tell extends Behaviour{
		private String hecho;
		private String regla;
		boolean tellBandera=false;
		public Tell(String hechos,String reglas){
			hecho=hechos;
			regla=reglas;
		}
		public void action(){
			clips=new Environment();
            clips.eval("(clear)");
            clips.eval("(reset)");
		    clips.eval(hecho);
			clips.build(regla);
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

	private class Ask extends Behaviour{
		boolean askBandera = false;
		public void action(){
			System.out.println("se ejecuto ask");
			System.out.println("estas son las reglas:");
			clips.eval("(rules)");
			System.out.println("estos son los resultados de reglas");
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



/*
	private class MyOneShotBehaviour extends OneShotBehaviour {
		private String hecho;
		private String regla;
		public MyOneShotBehaviour(String hechos,String reglas) {
			hecho=hechos;
			regla=reglas;
		}
		public void action() {
			System.out.println("Agent's action method executed");
		        System.out.println("hecho: "+hecho);
		        System.out.println("regla: "+regla);
		        clips=new Environment();
                clips.eval("(clear)");
                clips.eval("(reset)");
		        clips.eval(hecho);
			    clips.build(regla);
                clips.eval("(rules)");
                clips.eval("(facts)");
                clips.run();
			} 
		    
		    public int onEnd() {
		      myAgent.doDelete();   
		      return super.onEnd();
		    }
		} */
	
}
