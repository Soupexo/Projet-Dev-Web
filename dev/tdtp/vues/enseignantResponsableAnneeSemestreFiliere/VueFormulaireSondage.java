package tdtp.vues.enseignantResponsableAnneeSemestreFiliere;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VueFormulaireSondage extends JDialog {
    private JTextField nomField;
    private JComboBox<String> typeCombo;
    private JSpinner spinnerDate;
    private JCheckBox reponsesMultiplesCheck;
    private String editingId = null;
    private JButton btnSave;

    public VueFormulaireSondage(Window parent) {
        super(parent, "Créer un sondage", ModalityType.APPLICATION_MODAL);
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    public VueFormulaireSondage(Window parent, Map<String, Object> sondage) {
        super(parent, "Modifier un sondage", ModalityType.APPLICATION_MODAL);
        initUI();
        fillFromSondage(sondage);
        this.editingId = String.valueOf(sondage.get("num_s"));
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        // Nom
        gbc.gridx = 0; gbc.gridy = y++;
        panel.add(new JLabel("Nom du sondage:"), gbc);
        gbc.gridx = 1;
        nomField = new JTextField(25);
        panel.add(nomField, gbc);

        // Type
        gbc.gridx = 0; gbc.gridy = y++;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        typeCombo = new JComboBox<>(new String[]{"Groupe", "Pédagogique", "Orientation", "Autres"});
        panel.add(typeCombo, gbc);

        // Date limite
        gbc.gridx = 0; gbc.gridy = y++;
        panel.add(new JLabel("Date limite:"), gbc);
        gbc.gridx = 1;
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        spinnerDate = new JSpinner(dateModel);
        spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "yyyy-MM-dd"));
        panel.add(spinnerDate, gbc);
        gbc.gridx = 0;

        // Réponses multiples
        gbc.gridy = y++;
        panel.add(new JLabel("Réponses multiples:"), gbc);
        reponsesMultiplesCheck = new JCheckBox();
        gbc.gridx = 1;
        panel.add(reponsesMultiplesCheck, gbc);
        gbc.gridx = 0;

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.btnSave = new JButton(editingId == null ? "Créer" : "Enregistrer");
        btnSave.setBackground(new Color(37, 99, 235));
        btnSave.setForeground(Color.WHITE);
        JButton btnCancel = new JButton("Annuler");
        
        btnPanel.add(btnCancel);
        btnPanel.add(this.btnSave);

        gbc.gridx = 0; gbc.gridy = y++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnPanel, gbc);

        add(panel);

        btnCancel.addActionListener(e -> setVisible(false));
        this.btnSave.addActionListener(e -> handleSave());
    }

    private void fillFromSondage(Map<String, Object> sondage) {
        if (sondage == null) return;
        nomField.setText(String.valueOf(sondage.getOrDefault("nom_s", "")));
        typeCombo.setSelectedItem(String.valueOf(sondage.getOrDefault("type_s", "")));

        String delai = String.valueOf(sondage.getOrDefault("delai_s", ""));
        if (delai != null && !delai.isEmpty() && !delai.equals("null")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                spinnerDate.setValue(sdf.parse(delai));
            } catch (Exception e) {
                System.err.println("Erreur parsing date: " + delai);
            }
        }

        // Remplir le checkbox réponses multiples
        Object reponsesMultiplesObj = sondage.get("a_des_reponses_multiples_s");
        boolean reponsesMultiples = false;
        if (reponsesMultiplesObj instanceof Boolean) {
            reponsesMultiples = (Boolean) reponsesMultiplesObj;
        } else if (reponsesMultiplesObj instanceof Number) {
            reponsesMultiples = ((Number) reponsesMultiplesObj).intValue() == 1;
        }
        reponsesMultiplesCheck.setSelected(reponsesMultiples);
    }

    private void handleSave() {
        String nom = nomField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        Date dateDelai = (Date) spinnerDate.getValue();
        boolean reponsesMultiples = reponsesMultiplesCheck.isSelected();

        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String delaiStr = sdf.format(dateDelai);

        new Thread(() -> {
            try {
                Map<String, Object> resp;
                if (editingId == null) {
                    resp = tdtp.modeles.SondageApi.createSondage(nom, type, reponsesMultiples, delaiStr);
                } else {
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("nom_s", nom);
                    payload.put("type_s", type);
                    payload.put("delai_s", delaiStr);
                    payload.put("a_des_reponses_multiples_s", reponsesMultiples ? 1 : 0);
                    
                    int idCorrect = (int) Double.parseDouble(editingId);
                    resp = tdtp.modeles.SondageApi.updateSondage(idCorrect, payload);
                }

                final Map<String, Object> finalResp = resp;
                SwingUtilities.invokeLater(() -> {
                    if (finalResp != null && (Boolean.TRUE.equals(finalResp.get("success")) || !finalResp.containsKey("error"))) {
                        JOptionPane.showMessageDialog(this, "Succès !");
                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur API", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}