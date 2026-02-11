package tdtp.vues.espaceetudiant;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import tdtp.vues.utilitaires.PanelCardLayout;

public class VueSondageEtudiant extends JPanel {
    private CardLayout cardLayout;
    private JPanel panelCards;

    static public final String ACTION_SONDAGES = "GESTION_SONDAGES_ETUDIANT";
    static public final String ACTION_NOTES = "GESTION_NOTES_ETUDIANT";
    static public final String ACTION_ETUDIANTS= "GESTION_ETUDIANTS_ETUDIANT";

    private final Color BG_PAGE = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(30, 41, 59);
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);

    private JButton sondages, notes, etudiants;
    private JTable tableSondages;

    public VueSondageEtudiant(PanelCardLayout panelPrincipal) {
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

        etudiants = createMenuButton("Étudiants", false);
        notes = createMenuButton("Notes", false);
        sondages = createMenuButton("Sondages", true);

       menu.add(etudiants); menu.add(notes); menu.add(sondages);

        JButton btnRetour = new JButton("Retour");
        btnRetour.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, "ESPACE_ETUDIANT"));

        header.add(titre, BorderLayout.WEST);
        header.add(menu, BorderLayout.CENTER);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBackground(BG_PAGE);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        toolbar.setBackground(BG_PAGE);

        JLabel lblTitle = new JLabel("Liste des sondages");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_DARK);
        
        JButton btnRepondre = new JButton("Répondre à un sondage");
        btnRepondre.setBackground(COLOR_PRIMARY);
        btnRepondre.setForeground(Color.WHITE);
        btnRepondre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRepondre.setFocusPainted(false);
        btnRepondre.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnRepondre.addActionListener(e -> {
            int selectedRow = tableSondages.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un sondage", "Attention", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Conversion sécurisée même si la valeur est "13.0"
                String rawId = tableSondages.getValueAt(selectedRow, 0).toString();
                int idSondage = (int) Double.parseDouble(rawId); 
                
                String questionSondage = tableSondages.getValueAt(selectedRow, 1).toString();
                
                // Même chose pour l'ID étudiant si nécessaire
                int idEtudiantConnecte = 1; 

                Window parentWindow = SwingUtilities.getWindowAncestor(this);
                VueFormulaireReponseSondage dial = new VueFormulaireReponseSondage(
                    parentWindow, 
                    idSondage, 
                    questionSondage, 
                    idEtudiantConnecte
                );
                
                dial.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de l'ouverture du formulaire : " + ex.getMessage(), 
                    "Erreur de conversion", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        toolbar.add(lblTitle);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnRepondre);

        // Table Setup
        String[] cols = {"ID", "Nom", "Type", "Délai"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, cols) { 
            @Override public boolean isCellEditable(int r,int c){return false;} 
        };
        tableSondages = new JTable(model);
        styleTable(tableSondages);
        tableSondages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // On cache la colonne ID mais elle reste accessible pour le code
        tableSondages.getColumnModel().getColumn(0).setMinWidth(0);
        tableSondages.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scroll = new JScrollPane(tableSondages);
        scroll.getViewport().setBackground(Color.WHITE);

        main.add(toolbar, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);     

        loadSondages(model);
        setupNavigation(panelPrincipal);
    }

    public JButton getSondages() {
		return sondages;
	}

	public void setSondages(JButton sondages) {
		this.sondages = sondages;
	}

	public JButton getNotes() {
		return notes;
	}

	public void setNotes(JButton notes) {
		this.notes = notes;
	}

	public JButton getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(JButton etudiants) {
		this.etudiants = etudiants;
	}

	private void loadSondages(DefaultTableModel etuModel) {
        etuModel.setRowCount(0);
        new Thread(() -> {
            try {
                java.util.List<Map<String,Object>> list = tdtp.modeles.SondageApi.getSondages();
                SwingUtilities.invokeLater(() -> {
                    if (list != null) {
                        for (Map<String,Object> m : list) {
                            etuModel.addRow(new Object[]{
                                m.get("num_s"), m.get("nom_s"), m.get("type_s"), m.get("delai_s")
                            });
                        }
                    }
                });
            } catch (Exception ex) { ex.printStackTrace(); }
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
    }

    private JButton createMenuButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", isActive ? Font.BOLD : Font.PLAIN, 14));
        btn.setForeground(isActive ? COLOR_PRIMARY : TEXT_DARK);
        btn.setContentAreaFilled(false);
        btn.setBorder(isActive ? BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_PRIMARY) : new EmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setupNavigation(PanelCardLayout panelPrincipal) {
        sondages.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_SONDAGES));
        notes.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_NOTES));
        if (etudiants != null) etudiants.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ETUDIANTS));
    }
}