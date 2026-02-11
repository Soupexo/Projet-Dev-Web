package tdtp.vues.enseignantResponsableAnneeSemestreFiliere;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import tdtp.modeles.Enseignant;

public class VueFormulaireEnseignant extends JDialog {
    private JTextField loginField;
    private JPasswordField mdpField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField mailField;
    private String editingLogin = null; // null => create mode, otherwise edit mode
    private JButton btnCreate;
        
    public VueFormulaireEnseignant(Window parent) {
        super(parent, "Créer un enseignant", ModalityType.APPLICATION_MODAL);
        System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Constructeur appelé");
        initUI();
        System.out.println("DEBUG FORMULAIRE ENSEIGNANT: UI initialisée");
        pack();
        setLocationRelativeTo(parent);
        System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Fenêtre prête");
    }

    /** Edit mode constructor */
    public VueFormulaireEnseignant(Window parent, java.util.Map<String, Object> enseignant) {
        super(parent, "Modifier un enseignant", ModalityType.APPLICATION_MODAL);
        System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Constructeur mode édition appelé");
        initUI();
        fillFromEnseignant(enseignant);
        this.editingLogin = String.valueOf(enseignant.get("login_u"));
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        gbc.gridy = y++;
        panel.add(new JLabel("Login:"), gbc);
        loginField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(loginField, gbc);
        gbc.gridx = 0;

        gbc.gridy = y++;
        panel.add(new JLabel("Mot de passe:"), gbc);
        mdpField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(mdpField, gbc);
        gbc.gridx = 0;

        gbc.gridy = y++;
        panel.add(new JLabel("Nom:"), gbc);
        nomField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(nomField, gbc);
        gbc.gridx = 0;

        gbc.gridy = y++;
        panel.add(new JLabel("Prénom:"), gbc);
        prenomField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(prenomField, gbc);
        gbc.gridx = 0;

        gbc.gridy = y++;
        panel.add(new JLabel("Mail:"), gbc);
        mailField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(mailField, gbc);
        gbc.gridx = 0;

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.btnCreate = new JButton("Créer");
        JButton btnCancel = new JButton("Annuler");
        btnPanel.add(btnCancel);
        btnPanel.add(this.btnCreate);

        gbc.gridy = y++;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        add(panel);

        btnCancel.addActionListener(e -> setVisible(false));
        this.btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Bouton cliqué !");
                handleCreate();
            }
        });
        System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Listeners configurés");
    }

    private void fillFromEnseignant(java.util.Map<String, Object> enseignant) {
        if (enseignant == null) return;
        System.out.println("DEBUG fillFromEnseignant: enseignant map = " + enseignant);
        
        String login = String.valueOf(enseignant.getOrDefault("login_u", ""));
        String nom = String.valueOf(enseignant.getOrDefault("nom_u", ""));
        String prenom = String.valueOf(enseignant.getOrDefault("prenom_u", ""));
        String mail = String.valueOf(enseignant.getOrDefault("mail_u", ""));
        
        System.out.println("DEBUG fillFromEnseignant: login='" + login + "', nom='" + nom + "', prenom='" + prenom + "', mail='" + mail + "'");

        loginField.setText(login);
        loginField.setEditable(false);
        nomField.setText(nom);
        prenomField.setText(prenom);
        mailField.setText(mail);

        btnCreate.setText("Enregistrer");
        
        // En mode édition, le mot de passe est optionnel
        mdpField.setToolTipText("Laisser vide pour ne pas modifier le mot de passe");
    }

   
    private void handleCreate() {
        try {
            System.out.println("DEBUG FORMULAIRE ENSEIGNANT: handleCreate() appelé");
            System.out.println("DEBUG FORMULAIRE ENSEIGNANT: editingLogin=" + editingLogin);
            
            String login = loginField.getText().trim();
            String mdp = new String(mdpField.getPassword()).trim();
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String mail = mailField.getText().trim();

            System.out.println("DEBUG FORMULAIRE ENSEIGNANT: login='" + login + "', nom='" + nom + 
                              "', prenom='" + prenom + "', mail='" + mail + "'");

            // Validation
            if (login.isEmpty() || nom.isEmpty() || prenom.isEmpty() || mail.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Les champs login, nom, prénom et mail sont obligatoires", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // En mode création, mot de passe obligatoire
            if (editingLogin == null && mdp.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Le mot de passe est obligatoire pour la création", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            final String fLogin = login;
            final String fMdp = mdp.isEmpty() ? null : mdp; // null si vide
            final String fNom = nom;
            final String fPrenom = prenom;
            final String fMail = mail;

            new Thread(() -> {
                try {
                    Enseignant ens = new Enseignant();
                    java.util.Map<String, Object> resp;
                    
                    if (editingLogin == null) {
                        // MODE CRÉATION - toujours envoyer un mdp
                        System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Mode création");
                        resp = ens.ajouterEnseignantWithResult(
                            fLogin, 
                            fMdp != null ? fMdp : "defaultPassword123", 
                            fNom, 
                            fPrenom, 
                            fMail
                        );
                    } else {
                        // MODE ÉDITION - mdp optionnel
                        System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Mode édition");
                        resp = ens.modifierEnseignant(fLogin, fNom, fPrenom, fMail, fMdp);
                    }
                    
                    // Afficher TOUTE la réponse pour debug
                    System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Réponse complète = " + resp);
                    
                    SwingUtilities.invokeLater(() -> {
                        boolean ok = false;
                        String details = "";
                        
                        if (resp != null) {
                            // Plusieurs façons de détecter le succès
                            ok = Boolean.TRUE.equals(resp.get("success")) ||
                                 resp.containsKey("updated") ||
                                 resp.containsKey("message");
                            
                            details = resp.toString();
                            
                            // Si échec, chercher le message d'erreur
                            if (!ok && resp.containsKey("error")) {
                                details = "Erreur: " + resp.get("error");
                            }
                        }
                        
                        if (ok) {
                            String message = editingLogin == null ? 
                                "Enseignant créé avec succès" : 
                                "Enseignant modifié avec succès";
                            JOptionPane.showMessageDialog(VueFormulaireEnseignant.this, 
                                message, "Succès", JOptionPane.INFORMATION_MESSAGE);
                            setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(VueFormulaireEnseignant.this, 
                                "Échec : \n" + details, "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(VueFormulaireEnseignant.this, 
                            "Exception: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}