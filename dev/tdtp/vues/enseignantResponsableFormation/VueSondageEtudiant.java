package tdtp.vues.enseignantResponsableFormation;

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

static public final String ACTION_SONDAGES = "GESTION_SONDAGES_RESPONSABLE_FORMATION";
    static public final String ACTION_NOTES = "GESTION_NOTES_RESPONSABLE_FORMATION";
    static public final String ACTION_ETUDIANTS= "GESTION_ETUDIANTS_RESPONSABLE_FORMATION";
    static public final String ACTION_PROMOTION = "GESTION_PROMOTION_RESPONSABLE_FORMATION";
    static public final String ACTION_GROUPES = "GESTION_GROUPES_RESPONSABLE_FORMATION";
    static public final String ACTION_ENSEIGNANTS = "GESTION_ENSEIGNANTS_RESPONSABLE_FORMATION";

    private final Color BG_PAGE = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(30, 41, 59);
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);

    private JButton sondages, groupes, promotion, notes, etudiants, enseignants;
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
        groupes = createMenuButton("Groupes", false);
        promotion = createMenuButton("Promotion", false);
        etudiants = createMenuButton("Étudiants", false);
        notes = createMenuButton("Notes", false);
        sondages = createMenuButton("Sondages", true);
        enseignants = createMenuButton("Enseignants", false);

        menu.add(groupes); menu.add(promotion); menu.add(etudiants); menu.add(notes); menu.add(sondages); menu.add(enseignants);

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
        
        JButton btnCreate = new JButton("Créer sondage"); 
        btnCreate.setBackground(COLOR_PRIMARY); 
        btnCreate.setForeground(Color.WHITE); 
        
        JButton btnEdit = new JButton("Modifier"); btnEdit.setEnabled(false);
        
        toolbar.add(new JLabel("Liste sondages courants :"));
        toolbar.add(btnCreate); toolbar.add(btnEdit);

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

        // --- LISTENERS ---

        tableSondages.getSelectionModel().addListSelectionListener(ev -> {
            boolean sel = tableSondages.getSelectedRow() != -1;
            btnEdit.setEnabled(sel);
        });

        // ACTION CRÉER
        btnCreate.addActionListener(ev -> {
            VueFormulaireSondage dialog = new VueFormulaireSondage(SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            loadSondages(model);
        });

        // ACTION MODIFIER
        btnEdit.addActionListener(ev -> {
            int r = tableSondages.getSelectedRow(); 
            if (r == -1) return;
            int m = tableSondages.convertRowIndexToModel(r);
            
            // On prépare la Map à passer au formulaire
            Map<String, Object> data = new HashMap<>();
            data.put("num_s", model.getValueAt(m, 0));
            data.put("nom_s", model.getValueAt(m, 1));
            data.put("type_s", model.getValueAt(m, 2));
            data.put("delai_s", model.getValueAt(m, 3));
            
            VueFormulaireSondage d = new VueFormulaireSondage(SwingUtilities.getWindowAncestor(this), data);
            d.setVisible(true);
            loadSondages(model);
        });

        loadSondages(model);
        setupNavigation(panelPrincipal);
    }

    public JButton getSondages() {
		return sondages;
	}

	public void setSondages(JButton sondages) {
		this.sondages = sondages;
	}

	public JButton getGroupes() {
		return groupes;
	}

	public void setGroupes(JButton groupes) {
		this.groupes = groupes;
	}

	public JButton getPromotion() {
		return promotion;
	}

	public void setPromotion(JButton promotion) {
		this.promotion = promotion;
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
        groupes.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_GROUPES));
        promotion.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_PROMOTION));
        notes.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_NOTES));
        if (etudiants != null) etudiants.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ETUDIANTS));
        if (enseignants != null) enseignants.addActionListener(e -> ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ENSEIGNANTS));
    }
}