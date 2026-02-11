package tdtp.vues.connexion;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Classe représentant la vue d'accueil de l'application.
 * Affiche les différentes options de navigation sous forme de boutons
 * permettant d'accéder aux fonctionnalités principales.
 */
public class VueConnexionEtudiant extends JPanel {

	static public final String ACTION_ACCEDER_ESPACE_ETUDIANT = "ACCEDER_ESPACE_ETUDIANT";
//    static public final String ACTION_CONNECTE_ENSEIGNANT = "CONNECTE_ENSEIGNANT";
//    static public final String ACTION_CONNECTE_ETUDIANT = "CONNECTE_ETUDIANT";

    private CardLayout cardLayout;
    private JPanel panelCards;

    private JButton buttonAcceder;
    private JTextField nomSaisie, prenomSaisie;
    private JLabel titre, nom, prenom;

    public VueConnexionEtudiant(PanelCardLayout panelPrincipal) {
        
        final Color COULEUR_PRIMAIRE = new Color(220, 252, 231);
        final Color COULEUR_SECONDAIRE = new Color(6, 95, 70);

        // --- CARD LAYOUT ---
        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);

        // --- PANEL 1 : ACCUEIL ---
        JPanel panelConnexion = new JPanel();
        panelConnexion.setBackground(COULEUR_PRIMAIRE);
        panelConnexion.setLayout(new BoxLayout(panelConnexion, BoxLayout.Y_AXIS));

        titre = new JLabel("Espace etudiant");
        titre.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel formulaireConnexion = new JPanel(new GridLayout(2,2));

        buttonAcceder = new JButton("Démarrer");
        buttonAcceder.setAlignmentX(CENTER_ALIGNMENT);

        panelConnexion.add(titre,BorderLayout.NORTH);
        panelConnexion.add(Box.createVerticalStrut(30));
        panelConnexion.add(buttonAcceder,BorderLayout.SOUTH);
        
        panelCards.add(panelConnexion, "ESPACE_ETUDIANT");
        
        add(panelCards, BorderLayout.CENTER);
        
        buttonAcceder.addActionListener(e -> {
//          cardLayout.show(panelCards, "ESPACE_ENSEIGNANT");
      	CardLayout cl = (CardLayout) panelPrincipal.getLayout();
          
          // 2. On demande d'afficher la vue enregistrée sous le nom "ESPACE_ENSEIGNANT"
          // (Voir votre classe ApplicationCIUP où vous avez fait le panelPrincipal.add)
          cl.show(panelPrincipal, "ESPACE_ETUDIANT");
      });
    }

    public JButton getButtonAcceder() { return buttonAcceder; }
}
