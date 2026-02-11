package tdtp.vues.accueil;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import tdtp.vues.utilitaires.PanelCardLayout;

public class VueAccueil extends JPanel {
    private JButton buttonDemarrage;
    private JButton buttonConnexionEnseignant;
    private JButton buttonConnexionEtudiant;
    private JButton buttonConnexionResponsableFormation; 
    private JPanel panelConnexion;
    
    public VueAccueil(PanelCardLayout panelPrincipal) {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // Header avec titre
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(248, 250, 252));
        JLabel titre = new JLabel("Gestion de groupes TD/TP");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(30, 41, 59));
        header.add(titre);
        add(header, BorderLayout.NORTH);
        
        // Panel principal au centre
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(248, 250, 252));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Université
        JLabel uni = new JLabel("Université Paris-Saclay");
        uni.setFont(new Font("Segoe UI", Font.BOLD, 20));
        uni.setForeground(new Color(30, 41, 59));
        uni.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel iut = new JLabel("IUT D'ORSAY");
        iut.setFont(new Font("Segoe UI", Font.BOLD, 18));
        iut.setForeground(new Color(100, 116, 139));
        iut.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(uni);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(iut);
        centerPanel.add(Box.createVerticalStrut(50));
        
        // Section Espace enseignant
        JLabel titreEnseignant = new JLabel("Espace enseignant");
        titreEnseignant.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titreEnseignant.setForeground(new Color(30, 41, 59));
        titreEnseignant.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titreEnseignant);
        centerPanel.add(Box.createVerticalStrut(10));
        
        buttonConnexionEnseignant = new JButton("Connexion enseignant");
        styleButton(buttonConnexionEnseignant);
        buttonConnexionEnseignant.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(buttonConnexionEnseignant);
        centerPanel.add(Box.createVerticalStrut(30));
        
        // Séparateur
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(300, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(sep);
        centerPanel.add(Box.createVerticalStrut(30));
        
        // Section Espace étudiant
        JLabel titreEtudiant = new JLabel("Espace étudiant");
        titreEtudiant.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titreEtudiant.setForeground(new Color(30, 41, 59));
        titreEtudiant.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titreEtudiant);
        centerPanel.add(Box.createVerticalStrut(10));
        
        buttonConnexionEtudiant = new JButton("Connexion étudiant");
        styleButton(buttonConnexionEtudiant);
        buttonConnexionEtudiant.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(buttonConnexionEtudiant);
        centerPanel.add(Box.createVerticalStrut(30));
        
        // AJOUTER ICI : Section Espace responsable formation
        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(300, 1));
        sep2.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(sep2);
        centerPanel.add(Box.createVerticalStrut(30));
        
        JLabel titreResponsable = new JLabel("Espace responsable formation");
        titreResponsable.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titreResponsable.setForeground(new Color(30, 41, 59));
        titreResponsable.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titreResponsable);
        centerPanel.add(Box.createVerticalStrut(10));
        
        // NOUVEAU BOUTON
        buttonConnexionResponsableFormation = new JButton("Connexion responsable formation");
        styleButton(buttonConnexionResponsableFormation);
        buttonConnexionResponsableFormation.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(buttonConnexionResponsableFormation);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Actions des boutons
        buttonConnexionEnseignant.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelPrincipal.getLayout();
            cl.show(panelPrincipal, "CONNECTE_ENSEIGNANT");
        });
        
        buttonConnexionEtudiant.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelPrincipal.getLayout();
            cl.show(panelPrincipal, "CONNECTE_ETUDIANT");
        });
        
        // ACTION DU NOUVEAU BOUTON
        buttonConnexionResponsableFormation.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelPrincipal.getLayout();
            cl.show(panelPrincipal, "CONNECTE_RESPONSABLE_FORMATION");
        });
    }
    
    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(new Color(37, 99, 235));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(250, 40));
        btn.setMaximumSize(new Dimension(250, 40));
    }
    
    public JButton getButtonConnexionEnseignant() {
        return buttonConnexionEnseignant;
    }
    
    public JButton getButtonConnexionEtudiant() {
        return buttonConnexionEtudiant;
    }
    
    public JButton getButtonConnexionResponsableFormation() { // NOUVEAU
        return buttonConnexionResponsableFormation;
    }
    
    // ... autres méthodes si nécessaire
}