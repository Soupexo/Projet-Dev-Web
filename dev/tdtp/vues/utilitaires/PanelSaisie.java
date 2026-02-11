package tdtp.vues.utilitaires;

import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Classe représentant un panneau de saisie composé d'un label et d'un champ de texte.
 * Permet à l'utilisateur d'entrer une valeur dans un champ de texte associé à une description.
 */
public class PanelSaisie extends JPanel {
	JLabel l;
	JTextField t;

    /**
     * Constructeur du panneau de saisie.
     * 
     * @param s           Texte affiché dans le label pour décrire le champ de saisie.
     * @param longueurMax Nombre maximal de caractères pouvant être saisis.
     */
	public PanelSaisie(String s, int longueurMax) {
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setBorder(new EmptyBorder(20, 0, 20, 0));

		l = new JLabel(s);
		t = new JTextField(longueurMax);

		add(l);
		add(t);
	}

    /**
     * Retourne le texte saisi dans le champ de saisie.
     * 
     * @return Texte entré par l'utilisateur.
     */
	public String getText() {
		return t.getText();
	}
}
