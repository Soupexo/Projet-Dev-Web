package tdtp.vues.enseignantResponsableFormation;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Vue de connexion pour l'espace Enseignant Responsable de Formation.
 * Permet d'accéder aux fonctionnalités de gestion de l'application.
 */
public class VueConnexionEnseignantResponsableFormation extends JPanel {
    
    // --- COULEURS ---
    private final Color BG_PAGE = new Color(248, 250, 252);
    private final Color BG_CARD = Color.WHITE;
    private final Color TEXT_DARK = new Color(30, 41, 59);
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);
    private final Color COLOR_SUCCESS = new Color(16, 185, 129);
    private final Color COLOR_DANGER = new Color(239, 68, 68);
    
    private PanelCardLayout panelPrincipal;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JButton btnConnexion;
    private JLabel lblError;
    
    public VueConnexionEnseignantResponsableFormation(PanelCardLayout panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);
        setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // --- PANEL PRINCIPAL ---
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_PAGE);
        
        // --- CARTE DE CONNEXION ---
        JPanel cardPanel = new JPanel(new BorderLayout(20, 20));
        cardPanel.setBackground(BG_CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(226, 232, 240)),
            new EmptyBorder(30, 30, 30, 30)
        ));
        cardPanel.setPreferredSize(new Dimension(400, 300));
        
        // --- EN-TÊTE ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Espace Enseignant");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitle = new JLabel("Responsable de Formation");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 116, 139));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.CENTER);
        
        // --- FORMULAIRE ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Login
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Login:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.7;
        txtLogin = new JTextField();
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtLogin, gbc);
        
        // Mot de passe
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Mot de passe:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.7;
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtPassword, gbc);
        
        // Message d'erreur
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        lblError = new JLabel("");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(COLOR_DANGER);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        lblError.setVisible(false);
        formPanel.add(lblError, gbc);
        
        // Bouton de connexion
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 5, 5, 5);
        
        btnConnexion = new JButton("Se connecter");
        btnConnexion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConnexion.setBackground(COLOR_PRIMARY);
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setFocusPainted(false);
        btnConnexion.setBorderPainted(false);
        btnConnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConnexion.setPreferredSize(new Dimension(0, 45));
        
        formPanel.add(btnConnexion, gbc);
        
        // Assemblage
        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        
        mainPanel.add(cardPanel);
        add(mainPanel, BorderLayout.CENTER);
        
        // --- ACTIONS ---
        setupActions();
    }
    
    private void setupActions() {
        // Action sur le bouton de connexion
        btnConnexion.addActionListener(e -> {
            String login = txtLogin.getText().trim();
            String password = new String(txtPassword.getPassword());
            
            if (login.isEmpty() || password.isEmpty()) {
                showError("Veuillez remplir tous les champs");
                return;
            }
            
            // Simulation de connexion (à remplacer avec vraie authentification)
            if (authenticate(login, password)) {
                hideError();
                // Navigation vers l'espace enseignant
                CardLayout cl = (CardLayout) panelPrincipal.getLayout();
                cl.show(panelPrincipal, "ESPACE_ENSEIGNANT");
            } else {
                showError("Login ou mot de passe incorrect");
            }
        });
        
        // Action sur la touche Entrée
        txtPassword.addActionListener(e -> btnConnexion.doClick());
        
        // Effacer le message d'erreur lors de la saisie
        txtLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                hideError();
            }
        });
        
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                hideError();
            }
        });
    }
    
    private boolean authenticate(String login, String password) {
        // Simulation d'authentification
        // À remplacer avec appel API ou base de données
        return login.equals("enseignant") && password.equals("enseignant");
    }
    
    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
        btnConnexion.setBackground(COLOR_DANGER);
    }
    
    private void hideError() {
        lblError.setVisible(false);
        btnConnexion.setBackground(COLOR_PRIMARY);
    }
    
    public JTextField getTxtLogin() {
        return txtLogin;
    }
    
    public JPasswordField getTxtPassword() {
        return txtPassword;
    }
    
    public JButton getBtnConnexion() {
        return btnConnexion;
    }
}
