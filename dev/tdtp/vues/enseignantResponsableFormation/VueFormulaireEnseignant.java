package tdtp.vues.enseignantResponsableFormation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

            System.out.println("DEBUG FORMULAIRE ENSEIGNANT: login='" + login + "', nom='" + nom + "', prenom='" + prenom + "', mail='" + mail + "'");

            // Validation différente selon le mode
            if (editingLogin == null) {
                // Mode création : tous les champs sont obligatoires
                if (login.isEmpty() || mdp.isEmpty() || nom.isEmpty() || prenom.isEmpty() || mail.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this, 
                        "Tous les champs obligatoires doivent être remplis (login, mot de passe, nom, prénom, mail)", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            } else {
                // Mode édition : le mot de passe est optionnel
                if (login.isEmpty() || nom.isEmpty() || prenom.isEmpty() || mail.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this, 
                        "Les champs login, nom, prénom et mail sont obligatoires", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            final String fLogin = login;
            final String fMdp = mdp.isEmpty() ? null : mdp;
            final String fNom = nom;
            final String fPrenom = prenom;
            final String fMail = mail;

            System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Lancement du thread");
            new Thread(() -> {
                try {
                    System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Thread démarré");
                    Enseignant ens = new Enseignant();
                    
                    if (editingLogin == null) {
                        // Mode création
                        System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Mode création");
                        java.util.Map<String, Object> resp = ens.ajouterEnseignantWithResult(
                            fLogin, 
                            fMdp != null ? fMdp : "password", 
                            fNom, 
                            fPrenom, 
                            fMail
                        );
                        
                        boolean ok = resp != null && Boolean.TRUE.equals(resp.get("success"));
                        if (ok) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(
                                    this, 
                                    "Enseignant créé avec succès", 
                                    "Succès", 
                                    JOptionPane.INFORMATION_MESSAGE
                                );
                                setVisible(false);
                            });
                        } else {
                            final String details = resp != null ? 
                                String.valueOf(resp.getOrDefault("error", resp.toString())) : 
                                "Réponse vide";
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(
                                    this, 
                                    "Échec création enseignant : \n" + details, 
                                    "Erreur", 
                                    JOptionPane.ERROR_MESSAGE
                                );
                            });
                        }
                    } else {
                        // Mode édition
                        System.out.println("DEBUG FORMULAIRE ENSEIGNANT: Mode édition");
                        java.util.Map<String, Object> resp = ens.modifierEnseignant(
                            fLogin, 
                            fNom, 
                            fPrenom, 
                            fMail
                        );
                        
                        boolean ok = resp != null && Boolean.TRUE.equals(resp.get("success"));
                        if (ok) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(
                                    this, 
                                    "Enseignant modifié avec succès", 
                                    "Succès", 
                                    JOptionPane.INFORMATION_MESSAGE
                                );
                                setVisible(false);
                            });
                        } else {
                            final String details = resp != null ? 
                                String.valueOf(resp.getOrDefault("error", resp.toString())) : 
                                "Réponse vide";
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(
                                    this, 
                                    "Échec modification enseignant : \n" + details, 
                                    "Erreur", 
                                    JOptionPane.ERROR_MESSAGE
                                );
                            });
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("DEBUG FORMULAIRE ENSEIGNANT: Exception dans le thread: " + ex.getMessage());
                    ex.printStackTrace();
                    
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(
                            this, 
                            "Erreur: " + ex.getMessage(), 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE
                        );
                    });
                }
            }).start();
        } catch (Exception ex) {
            System.err.println("DEBUG FORMULAIRE ENSEIGNANT: Exception dans handleCreate: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this, 
                "Erreur: " + ex.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}