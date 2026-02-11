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
public class VueConnexionEnseignant extends JPanel {

    static public final String ACTION_ACCEDER_ESPACE_ENSEIGNANT = "ACCEDER_ESPACE_ENSEIGNANT";

    private CardLayout cardLayout;
    private JPanel panelCards;

    private JButton buttonAcceder;
    private JTextField nomSaisie, prenomSaisie;
    private JLabel titre, nom, prenom;

    public VueConnexionEnseignant(PanelCardLayout panelPrincipal) {

        final Color COULEUR_PRIMAIRE = new Color(220, 252, 231);
        final Color COULEUR_SECONDAIRE = new Color(6, 95, 70);

        // --- CARD LAYOUT ---
        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);

        // --- PANEL 1 : ACCUEIL ---
        JPanel panelConnexion = new JPanel();
        panelConnexion.setBackground(COULEUR_PRIMAIRE);
        panelConnexion.setLayout(new BoxLayout(panelConnexion, BoxLayout.Y_AXIS));

        titre = new JLabel("Espace enseignant");
        titre.setAlignmentX(CENTER_ALIGNMENT);
        

        buttonAcceder = new JButton("Démarrer");
        buttonAcceder.setActionCommand(ACTION_ACCEDER_ESPACE_ENSEIGNANT);
        buttonAcceder.setAlignmentX(CENTER_ALIGNMENT);

        panelConnexion.add(titre);
        panelConnexion.add(Box.createVerticalStrut(30));
        panelConnexion.add(buttonAcceder);
        
        // --- AJOUT DES PANELS AU CARDLAYOUT ---
        panelCards.add(panelConnexion, "ESPACE_ENSEIGNANT");

        add(panelCards, BorderLayout.CENTER);

        // --- ACTION : PASSER AU PANEL 2 ---
        buttonAcceder.addActionListener(e -> {
        	CardLayout cl = (CardLayout) panelPrincipal.getLayout();
            cl.show(panelPrincipal, "ESPACE_ENSEIGNANT");
        });
    }

    public void afficherVue(String nomVue) {
        cardLayout.show(panelCards, nomVue);
    }
    
    public JButton getButtonAcceder() { return buttonAcceder; }
}
