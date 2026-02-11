    package tdtp.vues.enseignantResponsableAnneeSemestreFiliere;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.util.*;

import tdtp.vues.utilitaires.PanelCardLayout;



/**
 * Vue affichant l'emploi du temps complet de la semaine.
 * Utilise un GridBagLayout pour placer les cours dynamiquement.
 */
public class VueGroupesEtudiant extends JPanel {

    private CardLayout cardLayout;
    private JPanel panelCards;
    
    static public final String ACTION_SONDAGES = "GESTION_SONDAGES_RESPONSABLE_FORMATION";
    static public final String ACTION_NOTES = "GESTION_NOTES_RESPONSABLE_FORMATION";
    static public final String ACTION_ETUDIANTS= "GESTION_ETUDIANTS_RESPONSABLE_FORMATION";
    static public final String ACTION_PROMOTION = "GESTION_PROMOTION_RESPONSABLE_FORMATION";
    static public final String ACTION_GROUPES = "GESTION_GROUPES_RESPONSABLE_FORMATION";
    static public final String ACTION_ENSEIGNANTS = "GESTION_ENSEIGNANTS_RESPONSABLE_FORMATION";

    // --- COULEURS ---
    private final Color BG_PAGE = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(30, 41, 59);
    private final Color COLOR_GRID_LINE = new Color(226, 232, 240);
    private final Color TEXT_GRAY = new Color(100, 116, 139);
    private final Color COLOR_PRIMARY = new Color(37, 99, 235); // Bleu
    private final Color COLOR_ACCENT = new Color(124, 58, 237); // Violet
    
    // Couleurs de matières
    public static final Color COL_MATHS = new Color(245, 158, 11);
    public static final Color COL_DEV = new Color(59, 130, 246);
    public static final Color COL_ANGLAIS = new Color(139, 92, 246);
    public static final Color COL_EPS = new Color(16, 185, 129);
    public static final Color COL_MANAGEMENT = new Color(236, 72, 153);
    
    private JButton sondages, groupes, promotion, notes, etudiants, enseignants;

    public VueGroupesEtudiant(PanelCardLayout panelPrincipal) {
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
        groupes = createMenuButton("Groupes", true);
        promotion = createMenuButton("Promotion", false);
        etudiants = createMenuButton("Étudiants", false);
        notes = createMenuButton("Notes", false);
        sondages = createMenuButton("Sondages", false);
        enseignants = createMenuButton("Enseignant", false);

        groupes.setActionCommand(ACTION_GROUPES); groupes.setAlignmentX(CENTER_ALIGNMENT);
        promotion.setActionCommand(ACTION_PROMOTION); promotion.setAlignmentX(CENTER_ALIGNMENT);
        etudiants.setActionCommand(ACTION_ETUDIANTS); etudiants.setAlignmentX(CENTER_ALIGNMENT);
        notes.setActionCommand(ACTION_NOTES); notes.setAlignmentX(CENTER_ALIGNMENT);
        sondages.setActionCommand(ACTION_SONDAGES); sondages.setAlignmentX(CENTER_ALIGNMENT);
        enseignants.setActionCommand(ACTION_ENSEIGNANTS); enseignants.setAlignmentX(CENTER_ALIGNMENT);

        menu.add(groupes); menu.add(promotion); menu.add(etudiants); menu.add(notes); menu.add(sondages); menu.add(enseignants);
        
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

     // =========================================================================
        // 2. CONTENU PRINCIPAL (Grille des groupes)
        // =========================================================================
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BG_PAGE);
        mainContent.setBorder(new EmptyBorder(0, 40, 20, 40));

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(BG_PAGE);
        toolbar.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblSection = new JLabel("Mes Groupes");
        lblSection.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSection.setForeground(TEXT_DARK);

        JButton btnCreate = new JButton("+ Nouveau Groupe");
        btnCreate.setBackground(COLOR_PRIMARY);
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCreate.setFocusPainted(false);
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnEdit = new JButton("Modifier");
        btnEdit.setEnabled(false);
        JButton btnDelete = new JButton("Supprimer");
        btnDelete.setEnabled(false);

        JPanel rightTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightTools.setOpaque(false);
        rightTools.add(btnCreate);
        rightTools.add(btnEdit);
        rightTools.add(btnDelete);

        toolbar.add(lblSection, BorderLayout.WEST);
        toolbar.add(rightTools, BorderLayout.EAST);
        mainContent.add(toolbar, BorderLayout.NORTH);

        String[] cols = {"id", "Nom", "Nombre d'étudiant max", "Nombre d'étudiant total", "Est-il complet ?", "Annee"};
        javax.swing.table.DefaultTableModel grpModel = new javax.swing.table.DefaultTableModel(new Object[][]{}, cols) { @Override public boolean isCellEditable(int r,int c){return false;} };
        JTable grpTable = new JTable(grpModel);
        grpTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(grpTable);

        JScrollPane scrollPane = new JScrollPane(grpTable);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BG_PAGE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContent.add(scrollPane, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        btnCreate.addActionListener(ev -> {
            tdtp.vues.enseignantResponsableFormation.VueFormulaireGroupe dialog = new tdtp.vues.enseignantResponsableFormation.VueFormulaireGroupe(SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            loadGroupes(grpModel);
        });

        grpTable.getSelectionModel().addListSelectionListener(ev -> {
            boolean sel = grpTable.getSelectedRow() != -1;
            btnEdit.setEnabled(sel);
            btnDelete.setEnabled(sel);
        });

        grpTable.addMouseListener(new java.awt.event.MouseAdapter() { 
            @Override 
            public void mouseClicked(java.awt.event.MouseEvent e){ 
                if (e.getClickCount()==2 && grpTable.getSelectedRow()!=-1){ 
                    int row=grpTable.convertRowIndexToModel(grpTable.getSelectedRow()); 
                    int id = Integer.parseInt(String.valueOf(grpModel.getValueAt(row,0))); 
                    java.util.Map<String,Object> g = new java.util.HashMap<>(); 
                    g.put("num_g", id); 
                    g.put("nom_g", grpModel.getValueAt(row,1)); 
                    g.put("nombre_etudiant_max_g", grpModel.getValueAt(row,2));
                    g.put("nombre_etudiant_g", grpModel.getValueAt(row,3));
                    g.put("est_finalise_g", grpModel.getValueAt(row,4));
                    // Récupérer le num_p directement (colonne 5)
                    Object anneeObj = grpModel.getValueAt(row,5);
                    if (anneeObj instanceof Number) {
                        g.put("num_p", ((Number) anneeObj).intValue());
                    } else {
                        g.put("num_p", null);
                    }
                    tdtp.vues.enseignantResponsableFormation.VueFormulaireGroupe dialog = new tdtp.vues.enseignantResponsableFormation.VueFormulaireGroupe(SwingUtilities.getWindowAncestor(VueGroupesEtudiant.this), g); 
                    dialog.setVisible(true); 
                    loadGroupes(grpModel); 
                } 
            } 
        });

        btnEdit.addActionListener(ev -> {
            int r = grpTable.getSelectedRow(); if (r == -1) return;
            int m = grpTable.convertRowIndexToModel(r);
            float id = Float.parseFloat(String.valueOf(grpModel.getValueAt(m, 0)));
            java.util.Map<String,Object> g = new java.util.HashMap<>();
            g.put("num_g", id);
            g.put("nom_g", grpModel.getValueAt(m,1)); 
            g.put("nombre_etudiant_max_g", grpModel.getValueAt(m,2));
            g.put("nombre_etudiant_g", grpModel.getValueAt(m,3));
            g.put("est_finalise_g", grpModel.getValueAt(m,4));
            // Récupérer le num_p directement (colonne 5)
            Object anneeObj = grpModel.getValueAt(m,5);
            if (anneeObj instanceof Number) {
                g.put("num_p", ((Number) anneeObj).intValue());
            } else if (anneeObj instanceof String) {
                // Si c'est un string comme "2023-2024", extraire le num_p
                String anneeStr = (String) anneeObj;
                if (anneeStr.contains("-")) {
                    try {
                        g.put("num_p", Integer.parseInt(anneeStr.split("-")[0].trim()));
                    } catch (Exception e) {
                        g.put("num_p", null);
                    }
                } else {
                    try {
                        g.put("num_p", Integer.parseInt(anneeStr.trim()));
                    } catch (Exception e) {
                        g.put("num_p", null);
                    }
                }
            } else {
                g.put("num_p", null);
            }
            tdtp.vues.enseignantResponsableFormation.VueFormulaireGroupe dialog = new tdtp.vues.enseignantResponsableFormation.VueFormulaireGroupe(SwingUtilities.getWindowAncestor(this), g);
            dialog.setVisible(true);
            loadGroupes(grpModel);
        });

        btnDelete.addActionListener(ev -> {
            int r = grpTable.getSelectedRow(); if (r == -1) return;
            int m = grpTable.convertRowIndexToModel(r);
            int id = Integer.parseInt(String.valueOf(grpModel.getValueAt(m, 0)));
            if (JOptionPane.showConfirmDialog(this, "Supprimer le groupe sélectionné ?", "Confirmer", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            new Thread(() -> {
                try {
                    java.util.Map<String,Object> resp = tdtp.modeles.GroupeApi.deleteGroup(id);
                    boolean ok = resp != null && Boolean.TRUE.equals(resp.get("success"));
                    SwingUtilities.invokeLater(() -> {
                        if (ok) {
                            JOptionPane.showMessageDialog(this, "Groupe supprimé", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            loadGroupes(grpModel);
                        } else {
                            JOptionPane.showMessageDialog(this, "Erreur suppression: " + String.valueOf(resp!=null?resp.getOrDefault("raw",resp):"réponse vide"), "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (Exception ex) { ex.printStackTrace(); SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE)); }
            }).start();
        });

        // Initial load
        loadGroupes(grpModel);
        
        
        
     // --- ACTION : PASSER AU PANEL 2 ---
        
        
        setupNavigation(panelPrincipal);
    }
    
    public JButton getSondages() { return sondages; }
    public JButton getGroupes() { return groupes; }
    public JButton getPromotion() { return promotion; }
    public JButton getNotes() { return notes; }

    private void loadGroupes(final javax.swing.table.DefaultTableModel grpModel) {
        grpModel.setRowCount(0);
        grpModel.addRow(new Object[]{"", "Chargement...", "", ""});
        new Thread(() -> {
            try {
                java.util.List<java.util.Map<String,Object>> list = tdtp.modeles.Groupe.getListeGroupes();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    grpModel.setRowCount(0);
                    if (list == null || list.isEmpty()) {
                        grpModel.addRow(new Object[]{"", "Aucune donnée", "", ""});
                    } else {
                    	for (Map<String, Object> m : list) {

                    	    Object idObj = m.get("num_g");
                    	    Object nom = m.get("nom_g");

                    	    Object anneeObj = m.get("num_p");

                    	    Object maxObj = m.get("nombre_etudiant_max_g");
                    	    Object totalObj = m.get("nombre_etudiant_g");
                    	    Object finaliseObj = m.get("est_finalise_g");

                    	    Integer id = (idObj instanceof Number) ? ((Number) idObj).intValue() : null;
                    	    Integer nombreMax = (maxObj instanceof Number) ? ((Number) maxObj).intValue() : null;
                    	    Integer nombreTotal = (totalObj instanceof Number) ? ((Number) totalObj).intValue() : null;
                    	    Boolean estFinalise = (finaliseObj instanceof Number)
                    	            ? ((Number) finaliseObj).intValue() == 1
                    	            : null;

                    	    Object[] row = {
                    	        id,
                    	        nom,
                    	        nombreMax,
                    	        nombreTotal,
                    	        estFinalise,
                    	        anneeObj
                    	    };

                    	    grpModel.addRow(row);
                    	}
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    grpModel.setRowCount(0);
                    grpModel.addRow(new Object[]{"", "Erreur: " + ex.getMessage(), "", ""});
                    JOptionPane.showMessageDialog(this, "Erreur chargement groupes: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    /**
     * Applique un style visuel cohérent aux JTable (hauteur, entête, centrage).
     */
    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT_DARK);
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(TEXT_DARK);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
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
        enseignants.addActionListener(e -> {
            ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ENSEIGNANTS);
        });
    }
}