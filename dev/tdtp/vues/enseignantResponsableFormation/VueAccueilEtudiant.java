package tdtp.vues.enseignantResponsableFormation;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Classe représentant la vue d'accueil de l'application.
 * Affiche les différentes options de navigation sous forme de boutons
 * permettant d'accéder aux fonctionnalités principales.
 */
public class VueAccueilEtudiant extends JPanel {

	static public final String ACTION_SONDAGES = "GESTION_SONDAGES_RESPONSABLE_FORMATION";
    static public final String ACTION_NOTES = "GESTION_NOTES_RESPONSABLE_FORMATION";
    static public final String ACTION_ETUDIANTS= "GESTION_ETUDIANTS_RESPONSABLE_FORMATION";
    static public final String ACTION_PROMOTION = "GESTION_PROMOTION_RESPONSABLE_FORMATION";
    static public final String ACTION_GROUPES = "GESTION_GROUPES_RESPONSABLE_FORMATION";
    static public final String ACTION_ENSEIGNANTS = "GESTION_ENSEIGNANTS_RESPONSABLE_FORMATION";

    private CardLayout cardLayout;
    private JPanel panelCards;

    private JButton sondages, groupes, promotion, notes, etudiants, enseignants; 
    private JLabel titre;
    private JPanel panelTimetable, panelManagement, panelCommunication;
    private JPanel panelSondage, panelMessagerie, panelAgenda;
    private JPanel panelAppel, panelDevoirs, panelArbitre;

	// --- COULEURS ---
    private final Color BG_PAGE = new Color(248, 250, 252); // Fond gris très clair
    private final Color BG_CARD = Color.WHITE;
    private final Color TEXT_DARK = new Color(30, 41, 59);
    private final Color TEXT_BLUE = new Color(37, 99, 235);
    private final Color TEXT_GRAY = new Color(100, 116, 139);
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);
    
    // Couleurs Cours
    private final Color COL_MATHS = new Color(245, 158, 11);
    private final Color COL_DEV = new Color(59, 130, 246);
    private final Color COL_ANGLAIS = new Color(139, 92, 246);
    private final Color COL_EPS = new Color(16, 185, 129);

    public VueAccueilEtudiant(PanelCardLayout panelPrincipal) {

        // --- CARD LAYOUT ---
        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);
        
        setLayout(new BorderLayout(20, 0)); // 20px d'écart horizontal
        setBackground(BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Marges extérieures

        // --- PANEL 1 : ACCUEIL ---
//        JPanel panelEspace = new JPanel();
//        panelEspace.setBackground(COULEUR_PRIMAIRE);
//        panelEspace.setLayout(new BoxLayout(panelEspace, BoxLayout.Y_AXIS));

     // 1. TITRE (Haut)
//        JLabel titre = new JLabel("Ben.");
//        titre.setFont(new Font("Segoe UI", Font.BOLD, 28));
//        titre.setForeground(new Color(88, 28, 135));
//        titre.setBorder(new EmptyBorder(0, 0, 20, 0));
//        add(titre, BorderLayout.NORTH);
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PAGE);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titre = new JLabel("AppSaclay");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(88, 28, 135));
        
        JPanel menu = new JPanel();
        
        groupes = createMenuButton("Groupes", false);
        promotion = createMenuButton("Promotion", false);
        etudiants = createMenuButton("Étudiants", false);
        notes = createMenuButton("Notes", false);
        sondages = createMenuButton("Sondages", false);
        enseignants = createMenuButton("Enseignants", false);

        groupes.setActionCommand(ACTION_GROUPES); groupes.setAlignmentX(CENTER_ALIGNMENT);
        promotion.setActionCommand(ACTION_PROMOTION); promotion.setAlignmentX(CENTER_ALIGNMENT);
        etudiants.setActionCommand(ACTION_ETUDIANTS); etudiants.setAlignmentX(CENTER_ALIGNMENT);
        notes.setActionCommand(ACTION_NOTES); notes.setAlignmentX(CENTER_ALIGNMENT);
        sondages.setActionCommand(ACTION_SONDAGES ); sondages.setAlignmentX(CENTER_ALIGNMENT);
        enseignants.setActionCommand(ACTION_ENSEIGNANTS); enseignants.setAlignmentX(CENTER_ALIGNMENT);

        menu.add(groupes); menu.add(promotion); menu.add(etudiants); menu.add(notes); menu.add(sondages); menu.add(enseignants);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnRetour.setFocusPainted(false);
        btnRetour.setBackground(Color.WHITE);
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Action Retour : Revient à la vue "ESPACE_ENSEIGNANT"
        btnRetour.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelPrincipal.getLayout();
            cl.show(panelPrincipal, "ESPACE_ENSEIGNANT");
        });

        header.add(titre, BorderLayout.WEST);
        header.add(menu, BorderLayout.CENTER);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
        
//        JPanel entete = new JPanel(new GridLayout(7,1));
//        emploiTemps = new JButton("Emploi du temps"); 
//        cahierTextes = new JButton("Cahier de textes");
//        sondages = new JButton("Sondages");
//        groupes = new JButton("Groupes");
//        promotion = new JButton("Promotion");
//        vieEtudiant = new JButton("Vie étudiant");
//        notes = new JButton("Notes");
//        
//        emploiTemps.setAlignmentX(CENTER_ALIGNMENT);
//        notes.setActionCommand(ACTION_NOTES);
//        notes.setAlignmentX(CENTER_ALIGNMENT);
//        sondages.setActionCommand(ACTION_SONDAGES);
//        sondages.setAlignmentX(CENTER_ALIGNMENT);
//        notes.setActionCommand(ACTION_NOTES);
//        notes.setAlignmentX(CENTER_ALIGNMENT);
//        sondages.setActionCommand(ACTION_SONDAGES);
//        sondages.setAlignmentX(CENTER_ALIGNMENT);
//        vieEtudiant.setActionCommand(ACTION_VIE_ETUDIANT);
//        vieEtudiant.setAlignmentX(CENTER_ALIGNMENT);
//        promotion.setActionCommand(ACTION_PROMOTION);
//        promotion.setAlignmentX(CENTER_ALIGNMENT);
//        groupes.setActionCommand(ACTION_GROUPES);
//        groupes.setAlignmentX(CENTER_ALIGNMENT);
//        
//        entete.add(cahierTextes);
//        entete.add(emploiTemps);
//        entete.add(groupes);
//        entete.add(promotion);
//        entete.add(notes);
//        entete.add(vieEtudiant);
//        entete.add(sondages);
        
//        panelTimetable = new JPanel();
        // Emploi du temps and Cahier de textes UI have been removed from the student welcome view as requested.
        
     // -------------------------------------------------------------
        // 3. PANEL DROIT : GRILLE MANAGEMENT & COMMUNICATION
        // -------------------------------------------------------------
        // Container global pour la partie droite
        JPanel rightContainer = new JPanel();
        // GridLayout 1 ligne, 2 colonnes pour séparer Management et Communication
        rightContainer.setLayout(new GridLayout(1, 2, 20, 0)); 
        rightContainer.setBackground(BG_PAGE);

        // --- COLONNE MANAGEMENT (Devoirs, Appel, Arbitre) ---
        JPanel panelManagement = new JPanel();
        // GridLayout 3 lignes, 1 colonne (empilés verticalement)
        panelManagement.setLayout(new GridLayout(3, 1, 0, 20)); 
        panelManagement.setBackground(BG_PAGE);

        panelManagement.add(createCard("Appel", "1D", "Présences"));
        panelManagement.add(createCard("Sanctions", "Aucune sanction", "Arbitre"));

        // --- COLONNE COMMUNICATION (Sondage, Messagerie) ---
        JPanel panelCommunication = new JPanel();
        panelCommunication.setLayout(new GridLayout(2, 1, 0, 20)); 
        panelCommunication.setBackground(BG_PAGE);

        panelCommunication.add(createCard("Sondages", "Sortie scolaire ?", "Vote"));
        panelCommunication.add(createCard("Messagerie", "3 nouveaux messages", "Reçu"));

        // Ajout des colonnes au container droit
        rightContainer.add(panelManagement);
        rightContainer.add(panelCommunication);

        // Ajout au centre de la fenêtre
        add(rightContainer, BorderLayout.CENTER);
        
//        panelManagement = new JPanel(new GridLayout(1,3));
//        panelDevoirs = new JPanel();
//        panelAppel = new JPanel();
//        panelArbitre = new JPanel();
//        
//        panelCommunication = new JPanel(new GridLayout(1,3));
//        panelSondage = new JPanel();
//        panelMessagerie = new JPanel();
//        panelAgenda = new JPanel();


        // --- AJOUT DES DEUX ESPACES DANS LE PANEL PRINCIPAL ---
//        panelEspace.add(panelTimetable, BorderLayout.WEST);
//        panelEspace.add(panelManagement, BorderLayout.CENTER);
//        panelEspace.add(panelCommunication, BorderLayout.EAST);

        // --- AJOUT DES PANELS AU CARDLAYOUT ---
//        panelCards.add(panelEspace, "ESPACE_ENSEIGNANT");
//        panelCards.add(panelEspace);

     // --- WRAPPER POUR CENTRER VERTICALLEMENT ---
//        JPanel wrapper = new JPanel();
//        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
//        wrapper.setBackground(COULEUR_PRIMAIRE);

        // Ajout du centrage vertical
//        wrapper.add(Box.createVerticalGlue());
//        wrapper.add(panelCards);
//        wrapper.add(Box.createVerticalGlue());

        // Ajout dans la vue principale
//        add(wrapper, BorderLayout.CENTER);
//        add(panelCards, BorderLayout.CENTER);

        // --- ACTION : PASSER AU PANEL 2 ---
        setupNavigation(panelPrincipal);
    }

	public JButton getSondages() {
		return sondages;
	}

	public JButton getGroupes() {
		return groupes;
	}

	public JButton getPromotion() {
		return promotion;
	}

	public JButton getNotes() {
		return notes;
	}
	
	public JButton getEnseignants() {
		return enseignants;
	}
	
	private JPanel createCoursItem(String heure, String matiere, String salle, Color couleur) {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(BG_CARD);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        // Heure à gauche
        JLabel lblHeure = new JLabel(heure);
        lblHeure.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHeure.setForeground(COL_DEV);
        lblHeure.setPreferredSize(new Dimension(45, 0));
        lblHeure.setVerticalAlignment(SwingConstants.TOP);

        // Bloc contenu avec bordure colorée à gauche
        JPanel contenu = new JPanel(new GridLayout(2, 1));
        contenu.setBackground(new Color(241, 245, 249)); // Gris bleuté intérieur
        contenu.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 4, 0, 0, couleur), // Barre de couleur
                new EmptyBorder(8, 10, 8, 10) // Padding texte
        ));

        JLabel lblMatiere = new JLabel(matiere);
        lblMatiere.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMatiere.setForeground(TEXT_DARK);

        JLabel lblSalle = new JLabel(salle);
        lblSalle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSalle.setForeground(TEXT_GRAY);

        contenu.add(lblMatiere);
        contenu.add(lblSalle);

        panel.add(lblHeure, BorderLayout.WEST);
        panel.add(contenu, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crée une carte Dashboard (Titre + Contenu gris)
     */
    private JPanel createCard(String titre, String infoPrincipale, String sousTitre) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Titre de la carte
        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitre.setForeground(TEXT_DARK);
        lblTitre.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Zone de contenu (Gris bleuté)
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setBackground(new Color(241, 245, 249));
        contentBox.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblDate = new JLabel("14 jan - 11:00");
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblDate.setForeground(TEXT_BLUE);
        lblDate.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel(infoPrincipale);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(TEXT_DARK);
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel(sousTitre);
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSub.setForeground(TEXT_GRAY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentBox.add(lblDate);
        contentBox.add(Box.createVerticalStrut(5));
        contentBox.add(lblInfo);
        contentBox.add(Box.createVerticalStrut(5));
        contentBox.add(lblSub);

        card.add(lblTitre, BorderLayout.NORTH);
        card.add(contentBox, BorderLayout.CENTER);

        return card;
    }
    
    private JButton createMenuButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", isActive ? Font.BOLD : Font.PLAIN, 14));
        btn.setForeground(isActive ? COLOR_PRIMARY : TEXT_DARK);
        btn.setContentAreaFilled(false);
        btn.setBorder(isActive ? 
                BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_PRIMARY) : 
                new EmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
	
		
    private void setupNavigation(PanelCardLayout panelPrincipal) {
    		
            sondages.addActionListener(e -> {
                ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_SONDAGES);
            });

            groupes.addActionListener(e -> {
                ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_GROUPES);
            });

            promotion.addActionListener(e -> {
                System.out.println("DEBUG: 'Promotion' clicked — attempting to show " + ACTION_PROMOTION);
                java.awt.Component c = panelPrincipal.getVue(ACTION_PROMOTION);
                if (c == null) {
                    System.out.println("DEBUG: vue '" + ACTION_PROMOTION + "' introuvable.");
                    JOptionPane.showMessageDialog(
                        this,
                        "La vue 'Promotion' est introuvable",
                        "Erreur navigation",
                        JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    System.out.println("DEBUG: vue Promotion trouvée");
                    ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_PROMOTION);
                }
            });
            
            if (etudiants != null) etudiants.addActionListener(e -> {
                System.out.println("DEBUG: 'Étudiants' clicked — attempting to show " + ACTION_ETUDIANTS);
                java.awt.Component c = panelPrincipal.getVue(ACTION_ETUDIANTS);
                if (c == null) {
                    System.out.println("DEBUG: vue '" + ACTION_ETUDIANTS + "' introuvable.");
                    JOptionPane.showMessageDialog(this, "La vue 'Étudiants' est introuvable (debug)", "Debug", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("DEBUG: vue trouvée: " + c.getName());
                    ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ETUDIANTS);
                }
            });
            notes.addActionListener(e -> {
                ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_NOTES);
            });
            
            enseignants.addActionListener(e -> {
                ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ENSEIGNANTS);
            });
        }
}
