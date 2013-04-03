package edu.brown.cs32.bughouse.global;

import java.awt.*;

import javax.swing.JFrame;

public class BughouseGUI extends JFrame {
	
	
	public BughouseGUI(){
		super("Bughouse Chess");
		Container content = this.getContentPane();
		this.setLayout(new CardLayout());
		this.setPreferredSize(new Dimension(800,600));
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

}
