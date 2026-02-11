package tdtp.vues.enseignantResponsableFormation;

import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Vue pour la gestion des enseignants.
 * Affiche la liste des enseignants avec fonctionnalités CRUD.
 */
public class VueEnseignantsEtudiant extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel panelCards;
    
    static public final String ACTION_SONDAGES = "GESTION_SONDAGES_RESPONSABLE_FORMATION";
    static public final String ACTION_NOTES = "GESTION_NOTES_RESPONSABLE_FORMATION";
    static public final String ACTION_ETUDIANTS = "GESTION_ETUDIANTS_RESPONSABLE_FORMATION";
    static public final String ACTION_PROMOTION = "GESTION_PROMOTION_RESPONSABLE_FORMATION";
    static public final String ACTION_GROUPES = "GESTION_GROUPES_RESPONSABLE_FORMATION";
    static public final String ACTION_ENSEIGNANTS = "GESTION_ENSEIGNANTS_RESPONSABLE_FORMATION";
    
    private final Color BG_PAGE = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(30, 41, 59);
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);
    
    private JButton sondages, groupes, promotion, notes, etudiants, enseignants;
    private JTable tableEnseignants;
    
    public VueEnseignantsEtudiant(PanelCardLayout panelPrincipal) {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PAGE);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel titre = new JLabel("AppSaclay");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(88, 28, 135));
        
        JPanel menu = new JPanel();
        groupes = createMenuButton("Groupes", false);
        promotion = createMenuButton("Promotion", false);
        etudiants = createMenuButton("Étudiants", false);
        notes = createMenuButton("Notes", false);
        sondages = createMenuButton("Sondages", false);
        enseignants = createMenuButton("Enseignants", true);
        
        groupes.setActionCommand(ACTION_GROUPES);
        promotion.setActionCommand(ACTION_PROMOTION);
        etudiants.setActionCommand(ACTION_ETUDIANTS);
        notes.setActionCommand(ACTION_NOTES);
        sondages.setActionCommand(ACTION_SONDAGES);
        enseignants.setActionCommand(ACTION_ENSEIGNANTS);
        
        menu.add(groupes);
        menu.add(promotion);
        menu.add(etudiants);
        menu.add(notes);
        menu.add(sondages);
        menu.add(enseignants);
        
        JButton btnRetour = new JButton("Retour");
        btnRetour.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, "ESPACE_ENSEIGNANT"));
        
        header.add(titre, BorderLayout.WEST);
        header.add(menu, BorderLayout.CENTER);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
        
        // --- MAIN CONTENT ---
        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBackground(BG_PAGE);
        
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        toolbar.setBackground(BG_PAGE);
        
        JButton btnDelete = new JButton("Supprimer");
        btnDelete.setEnabled(false);
        
        toolbar.add(new JLabel("Liste des enseignants :"));
        toolbar.add(btnDelete);
        
        // Table Setup
        String[] cols = {"ID", "Login", "Nom", "Prénom", "Mail", "Responsabilité", "Permissions associées"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tableEnseignants = new JTable(model);
        styleTable(tableEnseignants);
        tableEnseignants.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // On cache la colonne ID mais elle reste accessible pour le code
        tableEnseignants.getColumnModel().getColumn(0).setMinWidth(0);
        tableEnseignants.getColumnModel().getColumn(0).setMaxWidth(0);
        
        JScrollPane scroll = new JScrollPane(tableEnseignants);
        scroll.getViewport().setBackground(Color.WHITE);
        
        main.add(toolbar, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
        
        // --- LISTENERS ---
        tableEnseignants.getSelectionModel().addListSelectionListener(ev -> {
            boolean sel = tableEnseignants.getSelectedRow() != -1;
            btnDelete.setEnabled(sel);
        });
        
        
        btnDelete.addActionListener(ev -> {
            int r = tableEnseignants.getSelectedRow();
            if (r == -1) return;
            String id = String.valueOf(model.getValueAt(r, 0));
            String nom = String.valueOf(model.getValueAt(r, 2)) + " " + String.valueOf(model.getValueAt(r, 3));
            
            if (JOptionPane.showConfirmDialog(this, "Supprimer l'enseignant \"" + nom + "\" ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                new Thread(() -> {
                    try {
                        tdtp.modeles.EnseignantApi.deleteEnseignant(id);
                        SwingUtilities.invokeLater(() -> loadEnseignants(model));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        });
        
        loadEnseignants(model);
        setupNavigation(panelPrincipal);
    }
    
    private void loadEnseignants(DefaultTableModel model) {
        model.setRowCount(0);
        model.addRow(new Object[]{"", "Chargement...", "", "", "", ""});
        
        new Thread(() -> {
            try {
                // Utiliser directement l'API qui retourne une List
                List<Map<String, Object>> enseignants = tdtp.modeles.EnseignantApi.getAllEnseignants();
                
                SwingUtilities.invokeLater(() -> {
                    model.setRowCount(0);
                    
                    if (enseignants != null && !enseignants.isEmpty()) {
                        // Vérifier si c'est une réponse d'erreur
                        Map<String, Object> firstItem = enseignants.get(0);
                        if (firstItem.containsKey("success") && Boolean.FALSE.equals(firstItem.get("success"))) {
                            model.addRow(new Object[]{"", "Erreur: " + firstItem.get("error"), "", "", "", ""});
                            return;
                        }
                        
                        // Traiter chaque enseignant
                        for (Map<String, Object> enseignant : enseignants) {
                            // Adapter les données de l'API aux colonnes de votre table
                            String nom = (String) enseignant.get("nom_u");
                            String prenom = (String) enseignant.get("prenom_u");
                            String mail = (String) enseignant.get("mail_u");
                            String responsabilite = (String) enseignant.get("responsabilite");
                            String permission = (String) enseignant.get("permission");
                            
                            String id = String.valueOf(enseignants.indexOf(enseignant) + 1);
                            String login = (nom != null && prenom != null) ? 
                                          (nom.toLowerCase() + "." + prenom.toLowerCase()) : 
                                          "login.inconnu";
                            
                            model.addRow(new Object[]{
                                id,                    // ID (temporaire)
                                login,                 // Login (généré)
                                nom,                   // Nom
                                prenom,                // Prénom
                                mail,                  // Mail
                                responsabilite,         // Responsabilité
                                permission
                            });
                        }
                    } else {
                        model.addRow(new Object[]{"", "Aucun enseignant trouvé", "", "", "", "", ""});
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    model.setRowCount(0);
                    model.addRow(new Object[]{"", "Erreur: " + ex.getMessage(), "", "", "", "", ""});
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
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(TEXT_DARK);
    }
    
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
        sondages.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_SONDAGES));
        groupes.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_GROUPES));
        promotion.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_PROMOTION));
        notes.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_NOTES));
        if (etudiants != null) etudiants.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ETUDIANTS));
    }
    
    // Getters
    public JButton getSondages() {
        return sondages;
    }
    
    public JButton getGroupes() {
        return groupes;
    }
    
    public JButton getPromotion() {
        return promotion;
    }
    
    public JButton getNotes() {
        return notes;
    }
    
    public JButton getEtudiants() {
        return etudiants;
    }
    
    public JButton getEnseignants() {
        return enseignants;
    }
}
