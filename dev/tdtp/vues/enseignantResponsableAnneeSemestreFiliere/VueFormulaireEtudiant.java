package tdtp.vues.enseignantResponsableAnneeSemestreFiliere;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tdtp.modeles.Etudiant;

public class VueFormulaireEtudiant extends JDialog {
    private JTextField loginField;
    private JPasswordField mdpField;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField mailField;
    private JCheckBox apprentiCheck;
    private JComboBox<String> typeBacCombo;
    private JComboBox<String> genreCombo;
    private String editingLogin = null; // null => create mode, otherwise edit mode
    private JButton btnCreate;
        
    public VueFormulaireEtudiant(Window parent) {
        super(parent, "Créer un étudiant", ModalityType.APPLICATION_MODAL);
        System.out.println("DEBUG FORMULAIRE: Constructeur appelé");
        initUI();
        System.out.println("DEBUG FORMULAIRE: UI initialisée");
        pack();
        setLocationRelativeTo(parent);
        System.out.println("DEBUG FORMULAIRE: Fenêtre prête");
    }

    /** Edit mode constructor. Expects a Map with student fields (keys like "login_e", "nom_e", "prenom_e", "mail_e", "est_apprenti"). */
    public VueFormulaireEtudiant(Window parent, java.util.Map<String, Object> student) {
        super(parent, "Modifier un étudiant", ModalityType.APPLICATION_MODAL);
        System.out.println("DEBUG FORMULAIRE: Constructeur appelé");
        initUI();
        fillFromStudent(student);
        this.editingLogin = String.valueOf(student.get("login_u"));
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

        gbc.gridy = y++;
        panel.add(new JLabel("Est apprenti:"), gbc);
        apprentiCheck = new JCheckBox();
        gbc.gridx = 1;
        panel.add(apprentiCheck, gbc);
        gbc.gridx = 0;

        gbc.gridy = y++;
        panel.add(new JLabel("Type Bac:"), gbc);
        typeBacCombo = new JComboBox<>(new String[]{"Général", "Technologique", "Professionnel"});
        gbc.gridx = 1;
        panel.add(typeBacCombo, gbc);
        gbc.gridx = 0;

        gbc.gridy = y++;
        panel.add(new JLabel("Genre:"), gbc);
        genreCombo = new JComboBox<>(new String[]{"M", "F", "Autre"});
        gbc.gridx = 1;
        panel.add(genreCombo, gbc);
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
                System.out.println("DEBUG FORMULAIRE: Bouton cliqué !");
                handleCreate();
            }
        });
        System.out.println("DEBUG FORMULAIRE: Listeners configurés");
    }
    private void fillFromStudent(java.util.Map<String, Object> student) {
        if (student == null) return;
        System.out.println("DEBUG fillFromStudent: student map = " + student);
        String login = String.valueOf(student.getOrDefault("login_u", ""));
        String nom = String.valueOf(student.getOrDefault("nom_u", ""));
        String prenom = String.valueOf(student.getOrDefault("prenom_u", ""));
        String mail = String.valueOf(student.getOrDefault("mail_u", ""));
        System.out.println("DEBUG fillFromStudent: login='" + login + "', nom='" + nom + "', prenom='" + prenom + "', mail='" + mail + "'");
        boolean estApprenti = false;
        Object ap = student.get("est_apprenti_e");
        if (ap instanceof Boolean) estApprenti = (Boolean) ap;
        else if (ap instanceof String) estApprenti = Boolean.parseBoolean((String) ap);

        loginField.setText(login);
        loginField.setEditable(false);
        nomField.setText(nom);
        prenomField.setText(prenom);
        mailField.setText(mail);
        apprentiCheck.setSelected(estApprenti);

        Object tb = student.get("type_bac_e");
        if (tb != null) typeBacCombo.setSelectedItem(String.valueOf(tb));
        Object g = student.get("genre_e");
        if (g != null) genreCombo.setSelectedItem(String.valueOf(g));

        btnCreate.setText("Enregistrer");
    }
    private void handleCreate() {
        try {
            System.out.println("DEBUG FORMULAIRE: handleCreate() appelé");
            System.out.println("DEBUG FORMULAIRE: editingLogin=" + editingLogin);
            
            String login = loginField.getText().trim();
            String mdp = new String(mdpField.getPassword()).trim();
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String mail = mailField.getText().trim();
            boolean estApprenti = apprentiCheck.isSelected();
            String typeBac = (String) typeBacCombo.getSelectedItem();
            String genre = (String) genreCombo.getSelectedItem();

            System.out.println("DEBUG FORMULAIRE: login='" + login + "', nom='" + nom + "', prenom='" + prenom + "', mail='" + mail + "'");

            if (login.isEmpty() || nom.isEmpty() || prenom.isEmpty() || mail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs obligatoires doivent être remplis", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            final String fLogin = login;
            final String fMdp = mdp.isEmpty() ? null : mdp;
            final String fNom = nom;
            final String fPrenom = prenom;
            final String fMail = mail;
            final boolean fEstApprenti = estApprenti;
            final String fTypeBac = typeBac;
            final String fGenre = genre;

            System.out.println("DEBUG FORMULAIRE: Lancement du thread de création");
            new Thread(() -> {
                try {
                    System.out.println("DEBUG FORMULAIRE: Thread démarré");
                    Etudiant e = new Etudiant();
                    if (editingLogin == null) {
                        System.out.println("DEBUG FORMULAIRE: Mode création");
                        java.util.Map<String, Object> resp = e.ajouterEtudiantWithResult(fLogin, fMdp != null ? fMdp : "", fNom, fPrenom, fMail, fEstApprenti, fTypeBac, fGenre);
                        boolean ok = resp != null && Boolean.TRUE.equals(resp.get("success"));
                        if (ok) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Étudiant créé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                                setVisible(false);
                            });
                        } else {
                            final String details = resp != null ? String.valueOf(resp.getOrDefault("raw", resp.getOrDefault("error", resp.toString()))) : "Réponse vide";
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Échec création étudiant : \n" + details, "Erreur", JOptionPane.ERROR_MESSAGE);
                            });
                        }
                    } else {
                        // Mode édition
                        System.out.println("DEBUG FORMULAIRE: Mode édition");
                        boolean ok = e.modifierEtudiant(fLogin, fMdp, fNom, fPrenom, fMail, fEstApprenti, fTypeBac, fGenre);
                        if (ok) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Étudiant modifié avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                                setVisible(false);
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Échec modification étudiant", "Erreur", JOptionPane.ERROR_MESSAGE);
                            });
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("DEBUG FORMULAIRE: Exception dans le thread: " + ex.getMessage());
                    ex.printStackTrace();
                    
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        } catch (Exception ex) {
            System.err.println("DEBUG FORMULAIRE: Exception dans handleCreate: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}