package edu.brown.cs32.bughouse.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;

public class ConnectToServerMenu extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BackEnd backend_;
	private BughouseGUI parent_;
	private JTextField name_;

	public ConnectToServerMenu(BughouseGUI parent,BackEnd backend){
		super();
		parent_ = parent;
		this.setLayout(new GridBagLayout());
		this.backend_ = backend;
		this.setHeading();
		this.setTextField();
		this.setLoginButton();
	}
	
	private void setHeading() {
		JLabel header = new JLabel("Welcome to Bughouse!");
		header.setFont(new Font("Serif", Font.PLAIN, 36));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.insets = new Insets(2, 2, 5, 2);
		this.add(header,c);
	}
	
	private void setTextField(){
		JLabel name = new JLabel("Enter name here ");
		name_ = new JTextField(20);
		GridBagConstraints label1 =  new GridBagConstraints();
		GridBagConstraints field1 = new GridBagConstraints();
		label1.gridx = 0;
		label1.gridy = 1;
		field1.gridx = 1;
		field1.gridy = 1;
		field1.insets = new Insets(2, 1,10, 2);
		this.add(name,label1);
		this.add(name_,field1);
		
	}
	
	private void setLoginButton(){
		JButton connect = new JButton("Connect to server");
		connect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String name = name_.getText();
				if (name.length()==0){
					JOptionPane.showMessageDialog(null, "Type in a name", "Name needed", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					 backend_.joinServer(name);
				} catch (IOException
						| RequestTimedOutException e1) {
					JOptionPane.showMessageDialog(null, "The connection to the " +
							"server timed out.Please try connecting again",
							"Connection timed out",	JOptionPane.ERROR_MESSAGE);
				}
				parent_.joinServer();
			}
		});
		connect.setToolTipText("Click here to connect to the server to join or create a game");
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		this.add(connect,c);
	}
}
