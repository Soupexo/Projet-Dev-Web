package tdtp.vues.enseignantResponsableAnneeSemestreFiliere;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import tdtp.vues.utilitaires.PanelCardLayout;

public class VueEtudiantsEtudiant extends JPanel {
    private CardLayout cardLayout;
    private JPanel panelCards;

    static public final String ACTION_SONDAGES = "GESTION_SONDAGES_RESPONSABLE_FORMATION";
    static public final String ACTION_NOTES = "GESTION_NOTES_RESPONSABLE_FORMATION";
    static public final String ACTION_ETUDIANTS= "GESTION_ETUDIANTS_RESPONSABLE_FORMATION";
    static public final String ACTION_PROMOTION = "GESTION_PROMOTION_RESPONSABLE_FORMATION";
    static public final String ACTION_GROUPES = "GESTION_GROUPES_RESPONSABLE_FORMATION";
    static public final String ACTION_ENSEIGNANTS = "GESTION_ENSEIGNANTS_RESPONSABLE_FORMATION";


    private final Color BG_PAGE = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(30, 41, 59);
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);

    private JButton sondages, groupes, promotion, notes, etudiants;
    private JTable tableEtudiants;

    public VueEtudiantsEtudiant(PanelCardLayout panelPrincipal) {
        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);

        setLayout(new BorderLayout());
        setBackground(BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PAGE);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titre = new JLabel("AppSaclay");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(88, 28, 135));

        JPanel menu = new JPanel();
        groupes = createMenuButton("Groupes", false);
        promotion = createMenuButton("Promotion", false);
        etudiants = createMenuButton("Étudiants", true);
        notes = createMenuButton("Notes", false);
        sondages = createMenuButton("Sondages", false);

        groupes.setActionCommand(ACTION_GROUPES); groupes.setAlignmentX(CENTER_ALIGNMENT);
        promotion.setActionCommand(ACTION_PROMOTION); promotion.setAlignmentX(CENTER_ALIGNMENT);
        etudiants.setActionCommand(ACTION_ETUDIANTS); etudiants.setAlignmentX(CENTER_ALIGNMENT);
        notes.setActionCommand(ACTION_NOTES); notes.setAlignmentX(CENTER_ALIGNMENT);
        sondages.setActionCommand(ACTION_SONDAGES ); sondages.setAlignmentX(CENTER_ALIGNMENT);

        menu.add(groupes); menu.add(promotion); menu.add(etudiants); menu.add(notes); menu.add(sondages);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnRetour.setFocusPainted(false);
        btnRetour.setBackground(Color.WHITE);
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetour.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelPrincipal.getLayout();
            cl.show(panelPrincipal, "ESPACE_ENSEIGNANT");
        });

        header.add(titre, BorderLayout.WEST);
        header.add(menu, BorderLayout.CENTER);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Main content: students table + toolbar
        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBackground(BG_PAGE);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        toolbar.setBackground(BG_PAGE);
        JLabel lblTitle = new JLabel("Liste des étudiants"); 
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        lblTitle.setForeground(TEXT_DARK);
        JButton btnCreate = new JButton("Créer étudiant"); 
        btnCreate.setBackground(new Color(37,99,235)); 
        btnCreate.setForeground(Color.WHITE); 
        btnCreate.setFocusPainted(false);
        JButton btnEdit = new JButton("Modifier"); 
        btnEdit.setEnabled(false);
        JButton btnDelete = new JButton("Supprimer"); 
        btnDelete.setEnabled(false);
        
        toolbar.add(lblTitle);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(btnCreate);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);

        // Colonnes correspondant à la vue SQL + login (caché)
        String[] cols = {"Login", "Nom", "Prénom", "Apprenti", "Genre", "Type Bac", "Mail", "Groupe", "Année"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, cols) { 
            @Override public boolean isCellEditable(int r,int c){return false;} 
        };
        tableEtudiants = new JTable(model);
        styleTable(tableEtudiants);
        tableEtudiants.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Cacher la colonne Login (index 0)
        tableEtudiants.getColumnModel().getColumn(0).setMinWidth(0);
        tableEtudiants.getColumnModel().getColumn(0).setMaxWidth(0);
        tableEtudiants.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scroll = new JScrollPane(tableEtudiants);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);

        main.add(toolbar, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);

        // Selection wiring
        tableEtudiants.getSelectionModel().addListSelectionListener(ev -> {
            boolean sel = tableEtudiants.getSelectedRow() != -1;
            btnEdit.setEnabled(sel);
            btnDelete.setEnabled(sel);
        });

        btnCreate.addActionListener(ev -> {
            VueFormulaireEtudiant dialog = new VueFormulaireEtudiant(SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            loadEtudiants(model);
        });

        btnEdit.addActionListener(ev -> {
            int r = tableEtudiants.getSelectedRow(); if (r == -1) return;
            int m = tableEtudiants.convertRowIndexToModel(r);
            String login = String.valueOf(model.getValueAt(m, 0)); 
            String nom = String.valueOf(model.getValueAt(m, 1));
            String prenom = String.valueOf(model.getValueAt(m, 2));
            
            System.out.println("DEBUG btnEdit: Login récupéré = '" + login + "'");
            
            new Thread(() -> {
                try {
                    // Récupérer toutes les infos des étudiants
                    java.util.List<java.util.Map<String,Object>> infos = tdtp.modeles.EtudiantApi.getInfosEtudiants();
                    java.util.Map<String,Object> found = null;
                    
                    // Chercher l'étudiant par login
                    for (java.util.Map<String,Object> etudiant : infos) {
                        String loginEtu = String.valueOf(etudiant.getOrDefault("login_u", ""));
                        
                        if (login.equals(loginEtu)) {
                            found = etudiant;
                            System.out.println("DEBUG btnEdit: Étudiant trouvé: " + etudiant);
                            break;
                        }
                    }
                    
                    final java.util.Map<String,Object> f = found;
                    SwingUtilities.invokeLater(() -> {
                        if (f != null) {
                            VueFormulaireEtudiant d = new VueFormulaireEtudiant(SwingUtilities.getWindowAncestor(this), f);
                            d.setVisible(true);
                            loadEtudiants(model);
                        } else {
                            JOptionPane.showMessageDialog(this, "Informations introuvables pour: " + nom + " " + prenom, "Info", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                } catch (Exception ex) { 
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> 
                        JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE)
                    );
                }
            }).start();
        });

        btnDelete.addActionListener(ev -> {
            int r = tableEtudiants.getSelectedRow(); if (r == -1) return;
            int m = tableEtudiants.convertRowIndexToModel(r);
            String login = String.valueOf(model.getValueAt(m, 0)); // Colonne 0 = login (cachée)
            String nom = String.valueOf(model.getValueAt(m, 1));
            String prenom = String.valueOf(model.getValueAt(m, 2));
            
            if (JOptionPane.showConfirmDialog(this, "Supprimer l'étudiant " + nom + " " + prenom + " ?", "Confirmer", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            
            System.out.println("DEBUG btnDelete: Login récupéré = '" + login + "'");
            
            new Thread(() -> {
                try {
                    java.util.Map<String,Object> resp = tdtp.modeles.EtudiantApi.deleteStudent(login);
                    
                    boolean ok = resp != null && Boolean.TRUE.equals(resp.get("success"));
                    SwingUtilities.invokeLater(() -> {
                        if (ok) {
                            JOptionPane.showMessageDialog(this, "Étudiant supprimé", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            loadEtudiants(model);
                        } else {
                            String errorMsg = "Erreur de suppression.\n";
                            if (resp != null) {
                                errorMsg += "Réponse: " + resp.toString();
                            } else {
                                errorMsg += "Réponse vide du serveur";
                            }
                            JOptionPane.showMessageDialog(this, errorMsg, "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (Exception ex) { 
                    ex.printStackTrace(); 
                    SwingUtilities.invokeLater(() -> 
                        JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE)
                    ); 
                }
            }).start();
        });

        // Initial load
        loadEtudiants(model);

        setupNavigation(panelPrincipal);
    }

    private void loadEtudiants(final javax.swing.table.DefaultTableModel etuModel) {
        etuModel.setRowCount(0);
        etuModel.addRow(new Object[]{"", "Chargement...", "", "", "", "", "", "", ""});
        new Thread(() -> {
            try {
                java.util.List<java.util.Map<String,Object>> list = tdtp.modeles.EtudiantApi.getInfosEtudiants();
                
                // DEBUG: Afficher les clés disponibles
                if (list != null && !list.isEmpty()) {
                    System.out.println("=== DEBUG loadEtudiants ===");
                    System.out.println("Clés disponibles dans le premier étudiant: " + list.get(0).keySet());
                    System.out.println("Données complètes du premier étudiant: " + list.get(0));
                }
                
                javax.swing.SwingUtilities.invokeLater(() -> {
                    etuModel.setRowCount(0);
                    if (list == null || list.isEmpty()) {
                        etuModel.addRow(new Object[]{"", "Aucune donnée", "", "", "", "", "", "", ""});
                    } else {
                        for (java.util.Map<String,Object> m : list) {
                            // Récupération des champs de la vue SQL
                            Object login = m.getOrDefault("login_u", "");
                            Object nom = m.getOrDefault("nom_u", "");
                            Object prenom = m.getOrDefault("prenom_u", "");
                            Object estApprenti = m.getOrDefault("est_apprenti_e", "");
                            Object genre = m.getOrDefault("genre_e", "");
                            Object typeBac = m.getOrDefault("type_bac_e", "");
                            Object mail = m.getOrDefault("mail_u", "");
                            Object groupe = m.getOrDefault("nom_g", "");
                            Object annee = m.getOrDefault("annee", "");
                            
                            // Formatage de l'apprenti (true/false -> Oui/Non)
                            String apprentiStr = "";
                            if (estApprenti != null) {
                                if (estApprenti instanceof Boolean) {
                                    apprentiStr = ((Boolean) estApprenti) ? "Oui" : "Non";
                                } else {
                                    apprentiStr = String.valueOf(estApprenti);
                                }
                            }
                            
                            etuModel.addRow(new Object[]{login, nom, prenom, apprentiStr, genre, typeBac, mail, groupe, annee});
                        }
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    etuModel.setRowCount(0);
                    etuModel.addRow(new Object[]{"", "Erreur: " + ex.getMessage(), "", "", "", "", "", "", ""});
                });
            }
        }).start();
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT_DARK);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(TEXT_DARK);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
    }

    public JButton getSondages() { return sondages; }
    public JButton getGroupes() { return groupes; }
    public JButton getPromotion() { return promotion; }
    public JButton getNotes() { return notes; }

    private JButton createMenuButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", isActive ? Font.BOLD : Font.PLAIN, 14));
        btn.setForeground(isActive ? COLOR_PRIMARY : TEXT_DARK);
        btn.setContentAreaFilled(false);
        btn.setBorder(isActive ? 
                BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_PRIMARY) : 
                new EmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setupNavigation(PanelCardLayout panelPrincipal) {
        sondages.addActionListener(e -> {
            ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_SONDAGES);
        });

        groupes.addActionListener(e -> {
            ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_GROUPES);
        });

        promotion.addActionListener(e -> {
            System.out.println("DEBUG: 'Promotion' clicked — attempting to show " + ACTION_PROMOTION);
            java.awt.Component c = panelPrincipal.getVue(ACTION_PROMOTION);
            if (c == null) {
                System.out.println("DEBUG: vue '" + ACTION_PROMOTION + "' introuvable.");
                JOptionPane.showMessageDialog(
                    this,
                    "La vue 'Promotion' est introuvable",
                    "Erreur navigation",
                    JOptionPane.ERROR_MESSAGE
                );
            } else {
                System.out.println("DEBUG: vue Promotion trouvée");
                ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_PROMOTION);
            }
        });
        
        if (etudiants != null) etudiants.addActionListener(e -> {
            System.out.println("DEBUG: 'Étudiants' clicked — attempting to show " + ACTION_ETUDIANTS);
            java.awt.Component c = panelPrincipal.getVue(ACTION_ETUDIANTS);
            if (c == null) {
                System.out.println("DEBUG: vue '" + ACTION_ETUDIANTS + "' introuvable.");
                JOptionPane.showMessageDialog(this, "La vue 'Étudiants' est introuvable (debug)", "Debug", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("DEBUG: vue trouvée: " + c.getName());
                ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ETUDIANTS);
            }
        });
        
        notes.addActionListener(e -> {
            ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_NOTES);
        });
    }
}