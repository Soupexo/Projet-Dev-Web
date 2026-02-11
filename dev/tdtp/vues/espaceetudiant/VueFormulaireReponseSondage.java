package tdtp.vues.espaceetudiant;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import tdtp.modeles.SondageApi;

/**
 * Formulaire de réponse à un sondage (Version JDialog)
 */
public class VueFormulaireReponseSondage extends JDialog {
    
    private JTextField idSondageField;
    private JTextField idEtudiantField;
    private JTextField questionField;
    private JTextArea reponseArea;
    private JTextField dateField;
    private JButton btnEnvoyer;

    public VueFormulaireReponseSondage(Window parent, int idSondage, String question, int idEtudiant) {
        super(parent, "Répondre au sondage", ModalityType.APPLICATION_MODAL);
        
        initUI();
        
        // Pré-remplissage des données
        idSondageField.setText(String.valueOf(idSondage));
        questionField.setText(question);
        idEtudiantField.setText(String.valueOf(idEtudiant));
        dateField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // ID Sondage (Lecture seule)
        gbc.gridy = y++; gbc.gridx = 0;
        panel.add(new JLabel("ID Sondage:"), gbc);
        idSondageField = new JTextField(20);
        idSondageField.setEditable(false);
        idSondageField.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        panel.add(idSondageField, gbc);

        // ID Etudiant (Lecture seule)
        gbc.gridy = y++; gbc.gridx = 0;
        panel.add(new JLabel("Mon ID:"), gbc);
        idEtudiantField = new JTextField(20);
        idEtudiantField.setEditable(false);
        idEtudiantField.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        panel.add(idEtudiantField, gbc);

        // Question (Lecture seule)
        gbc.gridy = y++; gbc.gridx = 0;
        panel.add(new JLabel("Question:"), gbc);
        questionField = new JTextField(20);
        questionField.setEditable(false);
        gbc.gridx = 1;
        panel.add(questionField, gbc);

        // Réponse (Saisie)
        gbc.gridy = y++; gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JLabel("Ma Réponse:"), gbc);
        reponseArea = new JTextArea(5, 20);
        reponseArea.setLineWrap(true);
        reponseArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        panel.add(new JScrollPane(reponseArea), gbc);

        // Date (Auto)
        gbc.gridy = y++; gbc.gridx = 0;
        panel.add(new JLabel("Date:"), gbc);
        dateField = new JTextField(20);
        dateField.setEditable(false);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancel = new JButton("Annuler");
        btnEnvoyer = new JButton("Envoyer la réponse");
        btnEnvoyer.setBackground(new Color(37, 99, 235));
        btnEnvoyer.setForeground(Color.WHITE);

        btnPanel.add(btnCancel);
        btnPanel.add(btnEnvoyer);

        gbc.gridy = y++; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        add(panel);

        // Actions
        btnCancel.addActionListener(e -> setVisible(false));
        btnEnvoyer.addActionListener(e -> handleSend());
    }

    private void handleSend() {
        String reponseText = reponseArea.getText().trim();

        if (reponseText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir une réponse.", "Champ vide", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnEnvoyer.setEnabled(false);
        
        new Thread(() -> {
            try {
                // On récupère les IDs convertis
                int idS = Integer.parseInt(idSondageField.getText());
                int idE = Integer.parseInt(idEtudiantField.getText());
                String question = questionField.getText();
                String dateStr = dateField.getText();

                // Appel à votre API
                Map<String, Object> resp = SondageApi.ajouterReponse(idS, idE, question, reponseText, dateStr);
                
                boolean ok = resp != null && Boolean.TRUE.equals(resp.get("success"));

                SwingUtilities.invokeLater(() -> {
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Votre réponse a été envoyée !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        setVisible(false);
                    } else {
                        String error = resp != null ? String.valueOf(resp.get("error")) : "Erreur inconnue";
                        JOptionPane.showMessageDialog(this, "Échec : " + error, "Erreur", JOptionPane.ERROR_MESSAGE);
                        btnEnvoyer.setEnabled(true);
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Erreur système : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    btnEnvoyer.setEnabled(true);
                });
            }
        }).start();
    }
}