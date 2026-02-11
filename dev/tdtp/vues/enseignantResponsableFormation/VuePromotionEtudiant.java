package tdtp.vues.enseignantResponsableFormation;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Vue affichant l'emploi du temps complet de la semaine.
 * Utilise un GridBagLayout pour placer les cours dynamiquement.
 */
public class VuePromotionEtudiant extends JPanel {

//    private JPanel panelGrille;
    private CardLayout cardLayout;
    private JPanel panelCards;
    
    static public final String ACTION_SONDAGES = "GESTION_SONDAGES_RESPONSABLE_FORMATION";
    static public final String ACTION_NOTES = "GESTION_NOTES_RESPONSABLE_FORMATION";
    static public final String ACTION_ETUDIANTS= "GESTION_ETUDIANTS_RESPONSABLE_FORMATION";
    static public final String ACTION_PROMOTION = "GESTION_PROMOTION_RESPONSABLE_FORMATION";
    static public final String ACTION_GROUPES = "GESTION_GROUPES_RESPONSABLE_FORMATION";
    static public final String ACTION_ENSEIGNANTS = "GESTION_ENSEIGNANTS_RESPONSABLE_FORMATION";

    // --- COULEURS (Mêmes que le Dashboard) ---
    private final Color BG_PAGE = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(30, 41, 59);
    private final Color COLOR_GRID_LINE = new Color(226, 232, 240);
    private final Color COLOR_ACCENT = new Color(139, 92, 246);
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);
    
    // Couleurs de matières
    public static final Color COL_MATHS = new Color(245, 158, 11);
    public static final Color COL_DEV = new Color(59, 130, 246);
    public static final Color COL_ANGLAIS = new Color(139, 92, 246);
    public static final Color COL_MANAGEMENT = new Color(236, 72, 153);
    
    private JButton sondages, groupes, promotion, notes, etudiants, enseignants;

    
    public VuePromotionEtudiant(PanelCardLayout panelPrincipal) {
    	// --- CARD LAYOUT ---
        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);

        setLayout(new BorderLayout());
        setBackground(BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- 1. EN-TÊTE (Titre + Bouton Retour) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PAGE);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titre = new JLabel("AppSaclay");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(88, 28, 135));

        JPanel menu = new JPanel();

        groupes = createMenuButton("Groupes", false);
        promotion = createMenuButton("Promotion", true);
        etudiants = createMenuButton("Étudiants", false);
        notes = createMenuButton("Notes", false);
        sondages = createMenuButton("Sondages", false);
        enseignants = createMenuButton("Enseignants", false);

        groupes.setActionCommand(ACTION_GROUPES); groupes.setAlignmentX(CENTER_ALIGNMENT);
        promotion.setActionCommand(ACTION_PROMOTION); promotion.setAlignmentX(CENTER_ALIGNMENT);
        etudiants.setActionCommand(ACTION_ETUDIANTS); etudiants.setAlignmentX(CENTER_ALIGNMENT);
        notes.setActionCommand(ACTION_NOTES); notes.setAlignmentX(CENTER_ALIGNMENT);
        sondages.setActionCommand(ACTION_SONDAGES ); sondages.setAlignmentX(CENTER_ALIGNMENT);
     

        menu.add(groupes); menu.add(promotion); menu.add(etudiants); menu.add(notes); menu.add(sondages); menu.add(enseignants);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnRetour.setFocusPainted(false);
        btnRetour.setBackground(Color.WHITE);
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Action Retour : Revient à la vue "ESPACE_ENSEIGNANT"
        btnRetour.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelPrincipal.getLayout();
            cl.show(panelPrincipal, "ESPACE_ENSEIGNANT");
        });

        header.add(titre, BorderLayout.WEST);
        header.add(menu, BorderLayout.CENTER);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

     // =========================================================================
        // 2. CONTENU PRINCIPAL
        // =========================================================================
        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setBackground(BG_PAGE);

        // Student list removed from this view — use the dedicated "Étudiants" view (header navigation / GESTION_ETUDIANTS).
        JPanel promoToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        promoToolbar.setBackground(BG_PAGE);
        JLabel lblPromo = new JLabel("Promotions"); lblPromo.setFont(new Font("Segoe UI", Font.BOLD, 18)); lblPromo.setForeground(TEXT_DARK);
        JButton btnCreatePromo = new JButton("Créer promotion"); btnCreatePromo.setBackground(new Color(37,99,235)); btnCreatePromo.setForeground(Color.WHITE); btnCreatePromo.setFocusPainted(false);
        JButton btnEditPromo = new JButton("Modifier"); btnEditPromo.setEnabled(false);
        promoToolbar.add(lblPromo); promoToolbar.add(Box.createHorizontalStrut(10)); promoToolbar.add(btnCreatePromo); promoToolbar.add(btnEditPromo);

        String[] promoCols = {"ID", "Année", "Max Étudiants", "Max Covoiturage", "Max Groupes", "Total Groupes", "Total Étudiants"};
        DefaultTableModel promoModel = new DefaultTableModel(new Object[][]{}, promoCols) { @Override public boolean isCellEditable(int r,int c){return false;} };
        JTable promoTable = new JTable(promoModel); promoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane promoScroll = new JScrollPane(promoTable); promoScroll.setBorder(BorderFactory.createEmptyBorder()); promoScroll.getViewport().setBackground(Color.WHITE);
        
        JPanel promoPanel = new JPanel(new BorderLayout(0, 8));
        promoPanel.setBackground(BG_PAGE);

        promoToolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        promoPanel.add(promoToolbar, BorderLayout.NORTH);

        promoScroll.setPreferredSize(new Dimension(650, 320));
        promoPanel.add(promoScroll, BorderLayout.CENTER);

        mainContent.add(promoPanel, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        promoTable.getSelectionModel().addListSelectionListener(ev -> {
            boolean sel = promoTable.getSelectedRow() != -1;
            btnEditPromo.setEnabled(sel);
        });

        btnCreatePromo.addActionListener(ev -> {
            VueFormulairePromotion dialog = new VueFormulairePromotion(SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            loadPromotions(promoModel);

        });
        btnEditPromo.addActionListener(ev -> {
            int r = promoTable.getSelectedRow(); if (r == -1) return;
            int m = promoTable.convertRowIndexToModel(r);
            Object idObj = promoModel.getValueAt(m, 0);
            int id = idObj instanceof Number ? ((Number) idObj).intValue() : Integer.parseInt(String.valueOf(idObj));
            java.util.Map<String,Object> prom = new java.util.HashMap<>();
            prom.put("num_p", id);
            prom.put("nombre_etudiant_max_p", convertToInt(promoModel.getValueAt(m, 2)));
            prom.put("nombre_etudiant_max_covoiturage_p", convertToInt(promoModel.getValueAt(m, 3)));
            prom.put("nombre_de_groupe_max_p", convertToInt(promoModel.getValueAt(m, 4)));
            prom.put("nombre_de_groupe_total_p", convertToInt(promoModel.getValueAt(m, 5)));
            prom.put("nombre_etudiant_total_p", convertToInt(promoModel.getValueAt(m, 6)));
            Object annee = promoModel.getValueAt(m, 1);
            if (annee != null) {
                // Extraire l'ID de l'année depuis l'intitulé (ex: "2023-2024" → 2023)
                String anneeStr = String.valueOf(annee);
                if (anneeStr.contains("-")) {
                    String[] parts = anneeStr.split("-");
                    try {
                        prom.put("id_annee", Integer.parseInt(parts[0].trim()));
                    } catch (Exception e) {
                        prom.put("id_annee", null);
                    }
                } else {
                    prom.put("id_annee", null);
                }
            } else {
                prom.put("id_annee", null);
            }
            VueFormulairePromotion dialog = new VueFormulairePromotion(SwingUtilities.getWindowAncestor(this), prom);
            dialog.setVisible(true);
            loadPromotions(promoModel);
        });

        loadPromotions(promoModel);
        
        
        
        
     // --- ACTION : PASSER AU PANEL 2 ---
        
        
        setupNavigation(panelPrincipal);
    }

    public JButton getSondages() { return sondages; }
    public JButton getGroupes() { return groupes; }
    public JButton getPromotion() { return promotion; }
    public JButton getNotes() { return notes; }
    
	private JPanel createStatCard(String label, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(TEXT_DARK);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLabel.setForeground(Color.GRAY);

        card.add(lblValue, BorderLayout.CENTER);
        card.add(lblLabel, BorderLayout.SOUTH);
        
        return card;
    }

    /**
     * Applique un style moderne au JTable.
     */
    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(237, 233, 254)); // Violet très clair
        table.setSelectionForeground(TEXT_DARK);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(TEXT_DARK);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
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
            ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_PROMOTION);
        });
        
        if (etudiants != null) etudiants.addActionListener(e -> {
             ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ETUDIANTS);
        });
        notes.addActionListener(e -> {
            ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_NOTES);
        });
        enseignants.addActionListener(e -> {
            ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ENSEIGNANTS);
        });
    }


    private Integer parsePromoIdFromDisplay(String s) {
        if (s == null) return null;
        try {
            String[] parts = s.split("-", 2);
            String left = parts[0].trim();
            String digits = left.replaceAll("[^0-9]", "");
            if (!digits.isEmpty()) return Integer.parseInt(digits);
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    private void loadPromotions(final javax.swing.table.DefaultTableModel promoModel) {
        promoModel.setRowCount(0);
        promoModel.addRow(new Object[]{"", "Chargement...", "", "", "", "", ""});
        new Thread(() -> {
            try {
                java.util.List<java.util.Map<String,Object>> list = tdtp.modeles.PromotionApi.getPromotions();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    promoModel.setRowCount(0);
                    if (list == null || list.isEmpty()) {
                        promoModel.addRow(new Object[]{"", "Aucune donnée", "", "", "", "", ""});
                    } else {
                        for (java.util.Map<String,Object> m : list) {
                            Object id = m.get("num_p");
                            Object annee = convertToInt(m.get("num_a"));
                            Object nbMax = convertToInt(m.get("nombre_etudiant_max_p"));
                            Object nbMaxCovo = convertToInt(m.get("nombre_etudiant_max_covoiturage_p"));
                            Object nbMaxGroupes = convertToInt(m.get("nombre_de_groupe_max_p"));
                            Object nbTotalGroupes = convertToInt(m.get("nombre_de_groupe_total_p"));
                            Object nbTotalEtudiants = convertToInt(m.get("nombre_etudiant_total_p"));
                            final Object[] row = { id, annee, nbMax, nbMaxCovo, nbMaxGroupes, nbTotalGroupes, nbTotalEtudiants };
                            promoModel.addRow(row);
                        }
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    promoModel.setRowCount(0);
                    promoModel.addRow(new Object[]{"", "Erreur: " + ex.getMessage(), "", "", "", "", ""});
                    JOptionPane.showMessageDialog(this, "Erreur chargement promotions: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
    
    // Méthode utilitaire pour convertir les nombres en entiers
    private Object convertToInt(Object value) {
        if (value == null) return "";
        if (value instanceof Number) {
            Number num = (Number) value;
            return num.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return value;
        }
    }
}