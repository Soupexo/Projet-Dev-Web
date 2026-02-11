package tdtp.vues.connexion;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import tdtp.vues.utilitaires.PanelCardLayout;

public class VueConnexionResponsableFormation extends JPanel {
    private JTextField fieldLogin;
    private JPasswordField fieldPassword;
    private JButton buttonAcceder;
    
    public VueConnexionResponsableFormation(PanelCardLayout panelPrincipal) {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // Panel central
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(248, 250, 252));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Titre
        JLabel titre = new JLabel("Connexion Enseignant Responsable Formation");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(30, 41, 59));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(titre, gbc);
        
        // Bouton Accéder
        buttonAcceder = new JButton("Accéder");
        buttonAcceder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buttonAcceder.setBackground(new Color(37, 99, 235));
        buttonAcceder.setForeground(Color.WHITE);
        buttonAcceder.setFocusPainted(false);
        buttonAcceder.setPreferredSize(new Dimension(300, 45));
        buttonAcceder.setActionCommand("ACCEDER_ESPACE_RESPONSABLE_FORMATION");
        
        gbc.insets = new Insets(20, 0, 0, 0);
        centerPanel.add(buttonAcceder, gbc);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    public JButton getButtonAcceder() {
        return buttonAcceder;
    }
    
    public String getLogin() {
        return fieldLogin.getText();
    }
    
    public String getPassword() {
        return new String(fieldPassword.getPassword());
    }
}