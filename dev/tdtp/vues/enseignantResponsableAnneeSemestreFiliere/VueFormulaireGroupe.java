package tdtp.vues.enseignantResponsableAnneeSemestreFiliere;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tdtp.modeles.Groupe;

public class VueFormulaireGroupe extends JDialog {
    private JTextField nomField;
    private JTextField numPField;
    private JTextField nbMaxField;
    private JCheckBox finaliseCheck;

    private Integer editingId = null; // si non null -> mode édition

    public VueFormulaireGroupe(Window parent) {
        super(parent, "Créer un groupe", ModalityType.APPLICATION_MODAL);
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    /** Constructeur en mode édition (pré-remplit les champs) */
    public VueFormulaireGroupe(Window parent, java.util.Map<String, Object> groupe) {
        super(parent, "Éditer un groupe", ModalityType.APPLICATION_MODAL);
        initUI();
        // Pré-remplir
        if (groupe != null) {
            this.editingId = groupe.get("num_g") != null ? ((Number)groupe.get("num_g")).intValue() : null;
            nomField.setText(String.valueOf(groupe.getOrDefault("nom_g", "")));
            Object np = groupe.get("num_p");
            numPField.setText(np != null ? String.valueOf(np) : "");
            Object nb = groupe.get("nombre_etudiant_max_g");
            nbMaxField.setText(nb != null ? String.valueOf(nb) : "");
            Object ef = groupe.get("est_finalise_g");
            finaliseCheck.setSelected(ef != null && Boolean.TRUE.equals(ef));
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
        gbc.gridy = y++;
        panel.add(new JLabel("Nom du groupe:"), gbc);
        nomField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(nomField, gbc);
        gbc.gridx = 0;

        gbc.gridy = y++;
        panel.add(new JLabel("Num promotion (num_p):"), gbc);
        numPField = new JTextField(6);
        gbc.gridx = 1;
        panel.add(numPField, gbc);
        gbc.gridx = 0;

        gbc.gridy = y++;
        panel.add(new JLabel("Nombre max étudiants (optionnel):"), gbc);
        nbMaxField = new JTextField(6);
        gbc.gridx = 1;
        panel.add(nbMaxField, gbc);
        gbc.gridx = 0;

        gbc.gridy = y++;
        panel.add(new JLabel("Finalisé:"), gbc);
        finaliseCheck = new JCheckBox();
        gbc.gridx = 1;
        panel.add(finaliseCheck, gbc);
        gbc.gridx = 0;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancel = new JButton("Annuler");
        JButton btnCreate = new JButton("Créer");
        btnPanel.add(btnCancel);
        btnPanel.add(btnCreate);

        gbc.gridy = y++;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        add(panel);

        btnCancel.addActionListener(e -> setVisible(false));
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCreate();
            }
        });
    }

    private void handleCreate() {
        String nom = nomField.getText().trim();
        String numP = numPField.getText().trim();
        String nbMax = nbMaxField.getText().trim();
        boolean finalise = finaliseCheck.isSelected();

        if (nom.isEmpty() || numP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nom et Num promotion requis", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numPInt;
        try { numPInt = Integer.parseInt(numP); } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Num promotion invalide", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        Integer nbMaxInt = null;
        if (!nbMax.isEmpty()) {
            try { nbMaxInt = Integer.parseInt(nbMax); } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Nombre max invalide", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        }
        final String finalNom = nom;
        final int finalNumPInt = numPInt;
        final Integer finalNbMaxInt = nbMaxInt;
        final boolean finalFinalise = finalise;

        new Thread(() -> {
            Groupe g = new Groupe();
            java.util.Map<String, Object> resp;
            if (editingId == null) {
                // Création
                resp = g.creerGroupeManuellement(finalNom, finalNumPInt, finalNbMaxInt, finalFinalise);
            } else {
                // Édition : préparer payload
                java.util.Map<String, Object> data = new java.util.HashMap<>();
                data.put("nom_g", finalNom);
                data.put("num_p", finalNumPInt);
                if (finalNbMaxInt != null) data.put("nombre_etudiant_max_g", finalNbMaxInt);
                data.put("est_finalise_g", finalFinalise);
                resp = g.updateGroupe(editingId, data);
            }

            boolean ok = resp != null && Boolean.TRUE.equals(resp.get("success"));
            if (ok) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, editingId == null ? "Groupe créé" : "Groupe modifié", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                });
            } else {
                final String details = resp != null ? String.valueOf(resp.getOrDefault("raw", resp.getOrDefault("error", resp.toString()))) : "Réponse vide";
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Échec création/édition groupe :\n" + details, "Erreur", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }
}