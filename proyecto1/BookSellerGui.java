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

//import jade.core.*; //esta linea agregue
//import jade.core.AID;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
  @author Giovanni Caire - TILAB
 */
class BookSellerGui extends JFrame {
	private AlibabaAgent alibabaAgent;	
	private AmazonAgent amazonAgent;
	private BarnesNobleAgent barnesNobleAgent;
	
	private JTextField nombreTextField, tipoTextField, precioTextField, elementosDisponiblesTextField;
	//alibaba
	BookSellerGui(AlibabaAgent alibabaAgentNuevo) {
		super(alibabaAgentNuevo.getLocalName());
		
		alibabaAgent = alibabaAgentNuevo;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(4, 2));

		p.add(new JLabel("Nombre: "));
		nombreTextField = new JTextField(15);
		p.add(nombreTextField);

		p.add(new JLabel("Tipo: "));
		tipoTextField = new JTextField(15);
		p.add(tipoTextField);

		p.add(new JLabel("Precio: "));
		precioTextField = new JTextField(15);
		p.add(precioTextField);

		p.add(new JLabel("Elementos disponibles"));
		elementosDisponiblesTextField = new JTextField(15);
		p.add(elementosDisponiblesTextField);

		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String nombre = nombreTextField.getText().trim();
					String tipo = tipoTextField.getText().trim();
					String precio = precioTextField.getText().trim();
					String elementosDisponibles = elementosDisponiblesTextField.getText().trim();
					//myAgent.updateCatalogue(title, Integer.parseInt(price));
					alibabaAgent.updateCatalogue(nombre, tipo, precio, elementosDisponibles);
					nombreTextField.setText("");
					tipoTextField.setText("");
					precioTextField.setText("");
					elementosDisponiblesTextField.setText("");
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		p = new JPanel();
		p.add(addButton);


		JButton cargarButton = new JButton("Cargar datos");
		cargarButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					alibabaAgent.cargar();
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		p.add(cargarButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		// Make the agent terminate when the user closes 
		// the GUI using the button on the upper right corner	
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				alibabaAgent.doDelete();
			}
		} );
		
		setResizable(false);
	}
	//amazon
	BookSellerGui(AmazonAgent amazonAgentNuevo) {
		super(amazonAgentNuevo.getLocalName());
		
		amazonAgent = amazonAgentNuevo;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(4, 2));

		p.add(new JLabel("Nombre: "));
		nombreTextField = new JTextField(15);
		p.add(nombreTextField);

		p.add(new JLabel("Tipo: "));
		tipoTextField = new JTextField(15);
		p.add(tipoTextField);

		p.add(new JLabel("Precio: "));
		precioTextField = new JTextField(15);
		p.add(precioTextField);

		p.add(new JLabel("Elementos disponibles"));
		elementosDisponiblesTextField = new JTextField(15);
		p.add(elementosDisponiblesTextField);

		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String nombre = nombreTextField.getText().trim();
					String tipo = tipoTextField.getText().trim();
					String precio = precioTextField.getText().trim();
					String elementosDisponibles = elementosDisponiblesTextField.getText().trim();
					//myAgent.updateCatalogue(title, Integer.parseInt(price));
					amazonAgent.updateCatalogue(nombre, tipo, precio, elementosDisponibles);
					nombreTextField.setText("");
					tipoTextField.setText("");
					precioTextField.setText("");
					elementosDisponiblesTextField.setText("");
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		p = new JPanel();
		p.add(addButton);


		JButton cargarButton = new JButton("Cargar datos");
		cargarButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					amazonAgent.cargar();
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		p.add(cargarButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		// Make the agent terminate when the user closes 
		// the GUI using the button on the upper right corner	
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				amazonAgent.doDelete();
			}
		} );
		
		setResizable(false);
	}
	//barnes noble
	BookSellerGui(BarnesNobleAgent barnesNobleAgentNuevo) {
		super(barnesNobleAgentNuevo.getLocalName());
		
		barnesNobleAgent = barnesNobleAgentNuevo;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(4, 2));

		p.add(new JLabel("Nombre: "));
		nombreTextField = new JTextField(15);
		p.add(nombreTextField);

		p.add(new JLabel("Tipo: "));
		tipoTextField = new JTextField(15);
		p.add(tipoTextField);

		p.add(new JLabel("Precio: "));
		precioTextField = new JTextField(15);
		p.add(precioTextField);

		p.add(new JLabel("Elementos disponibles"));
		elementosDisponiblesTextField = new JTextField(15);
		p.add(elementosDisponiblesTextField);

		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String nombre = nombreTextField.getText().trim();
					String tipo = tipoTextField.getText().trim();
					String precio = precioTextField.getText().trim();
					String elementosDisponibles = elementosDisponiblesTextField.getText().trim();
					//myAgent.updateCatalogue(title, Integer.parseInt(price));
					barnesNobleAgent.updateCatalogue(nombre, tipo, precio, elementosDisponibles);
					nombreTextField.setText("");
					tipoTextField.setText("");
					precioTextField.setText("");
					elementosDisponiblesTextField.setText("");
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		p = new JPanel();
		p.add(addButton);


		JButton cargarButton = new JButton("Cargar datos");
		cargarButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					barnesNobleAgent.cargar();
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
		} );
		p.add(cargarButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		// Make the agent terminate when the user closes 
		// the GUI using the button on the upper right corner	
		addWindowListener(new	WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				barnesNobleAgent.doDelete();
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
}
