package tdtp.vues.utilitaires;

import javax.swing.*;

import tdtp.modeles.*;

import java.awt.*;

public class Fenetre extends JFrame {
	
	public Fenetre(String titre) {
		super(titre);
		setSize(1300,800);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
