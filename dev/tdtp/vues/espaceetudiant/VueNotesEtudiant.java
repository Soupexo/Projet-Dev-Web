package tdtp.vues.espaceetudiant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Map;
import tdtp.vues.utilitaires.PanelCardLayout;

public class VueNotesEtudiant extends JPanel {

    static public final String ACTION_SONDAGES = "GESTION_SONDAGES_ETUDIANT";
    static public final String ACTION_NOTES = "GESTION_NOTES_ETUDIANT";
    static public final String ACTION_ETUDIANTS= "GESTION_ETUDIANTS_ETUDIANT";

    private final Color BG_PAGE = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(30, 41, 59);
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);
    private final Color COLOR_ACCENT = new Color(88, 28, 135); 
    private final Color BORDER_LIGHT = new Color(226, 232, 240);

    private JTable tableNotes;
    private JButton sondages, notes, etudiants;

    public VueNotesEtudiant(PanelCardLayout panelPrincipal) {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        // --- 1. HEADER SUPÉRIEUR (AppSaclay + Menu) ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(BG_PAGE);
        topHeader.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titreLogo = new JLabel("AppSaclay");
        titreLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titreLogo.setForeground(COLOR_ACCENT);

        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        menuPanel.setOpaque(false);

        etudiants = createMenuButton("Étudiants", false);
        notes = createMenuButton("Notes", true);
        sondages = createMenuButton("Sondages", false);

        menuPanel.add(etudiants);
        menuPanel.add(notes);
        menuPanel.add(sondages);

        topHeader.add(titreLogo, BorderLayout.WEST);
        topHeader.add(menuPanel, BorderLayout.CENTER);

        // --- 2. BANDEAU GRIS (Titre de section + Retour) ---
        JPanel subHeader = new JPanel(new BorderLayout());
        subHeader.setBackground(Color.WHITE);
        subHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER_LIGHT),
                new EmptyBorder(10, 25, 10, 25)
        ));

        JLabel lblSectionTitle = new JLabel("Saisie des notes");
        lblSectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSectionTitle.setForeground(TEXT_DARK);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnRetour.setBackground(Color.WHITE);
        btnRetour.setFocusPainted(false);
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetour.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelPrincipal.getLayout();
            cl.show(panelPrincipal, "ESPACE_ETUDIANT");
        });

        subHeader.add(lblSectionTitle, BorderLayout.WEST);
        subHeader.add(btnRetour, BorderLayout.EAST);

        // Regroupement des headers
        JPanel headersGroup = new JPanel(new BorderLayout());
        headersGroup.add(topHeader, BorderLayout.NORTH);
        headersGroup.add(subHeader, BorderLayout.SOUTH);
        add(headersGroup, BorderLayout.NORTH);

        // --- 3. CONTENU (TABLEAU) ---
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BG_PAGE);
        mainContent.setBorder(new EmptyBorder(20, 25, 20, 25));

        String[] noteCols = {"Login", "Nom", "Prénom", "Groupe", "Matière", "Moy. S1", "Moy. S2", "Moy. Année", "Validé"};
        DefaultTableModel noteModel = new DefaultTableModel(new Object[][]{}, noteCols) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col >= 5 && col <= 7; 
            }
        };

        tableNotes = new JTable(noteModel);
        styleTable(tableNotes);
        
        // Cache la colonne Login
        tableNotes.getColumnModel().getColumn(0).setMinWidth(0);
        tableNotes.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scroll = new JScrollPane(tableNotes);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT));
        scroll.getViewport().setBackground(Color.WHITE);

        mainContent.add(scroll, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        setupNavigation(panelPrincipal);
        refreshNotes();
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER_LIGHT);
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(TEXT_DARK);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_LIGHT));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // Centre les colonnes de données numériques
        for(int i = 5; i < table.getColumnCount(); i++) {
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
        sondages.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_SONDAGES));
        etudiants.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ETUDIANTS));
        notes.addActionListener(e -> refreshNotes());
    }

    public void refreshNotes() {
        new Thread(() -> {
            try {
                tdtp.modeles.Etudiant e = new tdtp.modeles.Etudiant();
                java.util.List<java.util.Map<String, Object>> notesList = e.consulterNotes();
                
                SwingUtilities.invokeLater(() -> {
                    DefaultTableModel model = (DefaultTableModel) tableNotes.getModel();
                    model.setRowCount(0);
                    if (notesList != null) {
                        for (Map<String, Object> row : notesList) {
                            model.addRow(new Object[]{
                                row.get("login_u"), row.get("nom_u"), row.get("prenom_u"),
                                row.get("nom_g"), row.get("intitule_n"), row.get("moy_prem_semestre"),
                                row.get("moy_deux_semestre"), row.get("moy_annee"), row.get("aValideMatiere")
                            });
                        }
                    }
                });
            } catch (Exception ex) { ex.printStackTrace(); }
        }).start();
    }
}