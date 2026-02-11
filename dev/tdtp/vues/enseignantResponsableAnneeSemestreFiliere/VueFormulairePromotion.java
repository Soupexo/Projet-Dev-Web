package tdtp.vues.enseignantResponsableAnneeSemestreFiliere;

import javax.swing.*;
import java.awt.*;

import tdtp.modeles.Promotion;

public class VueFormulairePromotion extends JDialog {
    private JTextField nbEtudiantMaxField;
    private JTextField nbEtudiantCovoiturageMaxField;
    private JTextField nbGroupeMaxField;
    private JTextField nbGroupeTotalField;
    private JTextField nbEtudiantTotalField;
    private JTextField anneeField;
    private Integer editingId = null;

    public VueFormulairePromotion(Window parent) {
        super(parent, "Créer une promotion", ModalityType.APPLICATION_MODAL);
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    public VueFormulairePromotion(Window parent, java.util.Map<String, Object> prom) {
        super(parent, "Éditer une promotion", ModalityType.APPLICATION_MODAL);
        initUI();
        if (prom != null) {
            this.editingId = prom.get("num_p") != null ? ((Number) prom.get("num_p")).intValue() : null;
            Object nb = prom.get("nombre_etudiant_max_p");
            nbEtudiantMaxField.setText(nb != null ? String.valueOf(nb) : "");
            Object nbCovo = prom.get("nombre_etudiant_max_covoiturage_p");
            nbEtudiantCovoiturageMaxField.setText(nbCovo != null ? String.valueOf(nbCovo) : "");
            Object nbGMax = prom.get("nombre_de_groupe_max_p");
            nbGroupeMaxField.setText(nbGMax != null ? String.valueOf(nbGMax) : "");
            Object nbGTotal = prom.get("nombre_de_groupe_total_p");
            nbGroupeTotalField.setText(nbGTotal != null ? String.valueOf(nbGTotal) : "");
            Object nbETotal = prom.get("nombre_etudiant_total_p");
            nbEtudiantTotalField.setText(nbETotal != null ? String.valueOf(nbETotal) : "");
            Object an = prom.get("num_a");
            anneeField.setText(an != null ? String.valueOf(an) : "");
        }
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        
        // Nombre max étudiants
        gbc.gridy = y++;
        gbc.gridx = 0;
        panel.add(new JLabel("Nombre max étudiants:"), gbc);
        gbc.gridx = 1;
        nbEtudiantMaxField = new JTextField(6);
        panel.add(nbEtudiantMaxField, gbc);
        
        // Nombre max étudiants covoiturage
        gbc.gridy = y++;
        gbc.gridx = 0;
        panel.add(new JLabel("Nombre max étudiants covoiturage:"), gbc);
        gbc.gridx = 1;
        nbEtudiantCovoiturageMaxField = new JTextField(6);
        panel.add(nbEtudiantCovoiturageMaxField, gbc);
        
        // Nombre max groupes
        gbc.gridy = y++;
        gbc.gridx = 0;
        panel.add(new JLabel("Nombre max groupes:"), gbc);
        gbc.gridx = 1;
        nbGroupeMaxField = new JTextField(6);
        panel.add(nbGroupeMaxField, gbc);
        
        // Nombre total groupes
        gbc.gridy = y++;
        gbc.gridx = 0;
        panel.add(new JLabel("Nombre total groupes:"), gbc);
        gbc.gridx = 1;
        nbGroupeTotalField = new JTextField(6);
        panel.add(nbGroupeTotalField, gbc);
        
        // Nombre total étudiants
        gbc.gridy = y++;
        gbc.gridx = 0;
        panel.add(new JLabel("Nombre total étudiants:"), gbc);
        gbc.gridx = 1;
        nbEtudiantTotalField = new JTextField(6);
        panel.add(nbEtudiantTotalField, gbc);
        
        // Année
        gbc.gridy = y++;
        gbc.gridx = 0;
        panel.add(new JLabel("ID Année:"), gbc);
        gbc.gridx = 1;
        anneeField = new JTextField(6);
        panel.add(anneeField, gbc);

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancel = new JButton("Annuler");
        JButton btnCreate = new JButton("Enregistrer");
        btnPanel.add(btnCancel);
        btnPanel.add(btnCreate);

        gbc.gridy = y++;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        add(panel);

        btnCancel.addActionListener(e -> setVisible(false));
        btnCreate.addActionListener(e -> doSave());
    }

    private void doSave() {
        String nbEtudiantMax = nbEtudiantMaxField.getText().trim();
        String nbEtudiantCovoiturageMax = nbEtudiantCovoiturageMaxField.getText().trim();
        String nbGroupeMax = nbGroupeMaxField.getText().trim();
        String nbGroupeTotal = nbGroupeTotalField.getText().trim();
        String nbEtudiantTotal = nbEtudiantTotalField.getText().trim();
        String annee = anneeField.getText().trim();
        
        if (nbEtudiantMax.isEmpty() || nbEtudiantCovoiturageMax.isEmpty() || nbGroupeMax.isEmpty() || nbGroupeTotal.isEmpty() || nbEtudiantTotal.isEmpty() || annee.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont requis", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int nbEtudiantMaxInt, nbEtudiantCovoiturageMaxInt, nbGroupeMaxInt, nbGroupeTotalInt, nbEtudiantTotalInt, anneeInt;
        try { 
            nbEtudiantMaxInt = Integer.parseInt(nbEtudiantMax); 
            nbEtudiantCovoiturageMaxInt = Integer.parseInt(nbEtudiantCovoiturageMax);
            nbGroupeMaxInt = Integer.parseInt(nbGroupeMax);
            nbGroupeTotalInt = Integer.parseInt(nbGroupeTotal);
            nbEtudiantTotalInt = Integer.parseInt(nbEtudiantTotal);
            anneeInt = Integer.parseInt(annee);
        } catch (NumberFormatException ex) { 
            JOptionPane.showMessageDialog(this, "Un des nombres est invalide", "Erreur", JOptionPane.ERROR_MESSAGE); 
            return; 
        }

        final int fNbEtudiantMax = nbEtudiantMaxInt;
        final int fNbEtudiantCovoiturageMax = nbEtudiantCovoiturageMaxInt;
        final int fNbGroupeMax = nbGroupeMaxInt;
        final int fNbGroupeTotal = nbGroupeTotalInt;
        final int fNbEtudiantTotal = nbEtudiantTotalInt;
        final int fAnnee = anneeInt;

        new Thread(() -> {
            try {
                java.util.Map<String, Object> payload = new java.util.HashMap<>();
                payload.put("nombre_etudiant_max_p", fNbEtudiantMax);
                payload.put("nombre_etudiant_max_covoiturage_p", fNbEtudiantCovoiturageMax);
                payload.put("nombre_de_groupe_max_p", fNbGroupeMax);
                payload.put("nombre_de_groupe_total_p", fNbGroupeTotal);
                payload.put("nombre_etudiant_total_p", fNbEtudiantTotal);
                payload.put("num_a", fAnnee);
                
                System.out.println("DEBUG PROMOTION: payload=" + payload);
                
                java.util.Map<String, Object> resp;
                if (editingId == null) {
                    resp = tdtp.modeles.PromotionApi.createPromotion(payload);
                } else {
                    resp = tdtp.modeles.PromotionApi.updatePromotion(editingId, payload);
                }
                
                System.out.println("DEBUG PROMOTION: réponse=" + resp);
                
                boolean ok = resp != null && Boolean.TRUE.equals(resp.get("success"));
                if (ok) {
                    SwingUtilities.invokeLater(() -> { 
                        JOptionPane.showMessageDialog(this, editingId == null ? "Promotion créée" : "Promotion modifiée", "Succès", JOptionPane.INFORMATION_MESSAGE); 
                        setVisible(false); 
                    });
                } else {
                    final String details = resp != null ? String.valueOf(resp.getOrDefault("raw", resp.getOrDefault("error", resp.toString()))) : "Réponse vide";
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erreur: " + details, "Erreur", JOptionPane.ERROR_MESSAGE));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }
}