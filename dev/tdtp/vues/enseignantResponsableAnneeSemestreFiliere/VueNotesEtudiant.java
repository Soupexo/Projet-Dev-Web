package tdtp.vues.enseignantResponsableAnneeSemestreFiliere;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Vue affichant l'emploi du temps complet de la semaine.
 * Utilise un GridBagLayout pour placer les cours dynamiquement.
 */
public class VueNotesEtudiant extends JPanel {

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
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);
    
    // Couleurs de matières
    public static final Color COL_MATHS = new Color(245, 158, 11);
    public static final Color COL_DEV = new Color(59, 130, 246);
    public static final Color COL_ANGLAIS = new Color(139, 92, 246);
    public static final Color COL_EPS = new Color(16, 185, 129);
    public static final Color COL_MANAGEMENT = new Color(236, 72, 153);
    
    private JTable tableNotes;
    private JButton sondages, groupes, promotion, notes, etudiants, enseignants;
    private JComboBox<String> comboClasse2;
    private JComboBox<String> comboMatiere2;
    private PanelCardLayout panelPrincipal; // Stocker la référence pour la navigation
    
    public VueNotesEtudiant(PanelCardLayout panelPrincipal) {
        this.panelPrincipal = panelPrincipal; // Stocker la référence
        // -- Remplacement des données fictives par chargement dynamique possible --
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
        
        JPanel menu = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        menu.setOpaque(false);

        groupes = createMenuButton("Groupes", false);
        promotion = createMenuButton("Promotion", false);
        etudiants = createMenuButton("Étudiants", false);
        notes = createMenuButton("Notes", true);
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
        // 2. CONTENU PRINCIPAL (Notes uniquement — navigation via le header)
        // =========================================================================
        // Notes content is displayed below and other sections are handled by their own views.


        // --- Notes Tab (reuse existing table layout) ---
        JPanel notesPanel = new JPanel(new BorderLayout()); notesPanel.setBackground(BG_PAGE);
        // Toolbar for notes: keep class/matiere combo and create-student button
        JPanel notesToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10)); notesToolbar.setBackground(BG_PAGE);
        JLabel lblSection = new JLabel("Saisie des notes"); lblSection.setFont(new Font("Segoe UI", Font.BOLD, 20)); lblSection.setForeground(TEXT_DARK);

        // Combo Classe / Matière (populated from API)
        comboClasse2 = new JComboBox<>(); comboClasse2.setBackground(Color.WHITE);
        comboMatiere2 = new JComboBox<>(); comboMatiere2.setBackground(Color.WHITE);
        comboClasse2.addItem("Tous"); comboMatiere2.addItem("Tous");
        comboClasse2.addActionListener(ev -> refreshNotes());
        comboMatiere2.addActionListener(ev -> refreshNotes());

        notesToolbar.add(lblSection); notesToolbar.add(Box.createHorizontalStrut(20)); notesToolbar.add(new JLabel("Classe :")); notesToolbar.add(comboClasse2);
        notesToolbar.add(new JLabel("Matière :")); notesToolbar.add(comboMatiere2);

        // populate the filters from API
        populateNoteCombos();

        notesPanel.add(notesToolbar, BorderLayout.NORTH);

        // Notes table (reuse previously-constructed table but separate model)
        String[] noteCols = {"Login", "Nom", "Prénom", "Groupe", "Matière", "Moy. S1", "Moy. S2", "Moy. Année", "Validé"};
        DefaultTableModel noteModel = new DefaultTableModel(new Object[][]{}, noteCols) { @Override public boolean isCellEditable(int row, int column){ return column == 5 || column == 6 || column == 7; } };
        tableNotes = new JTable(noteModel); styleTable(tableNotes);
        tableNotes.getColumnModel().getColumn(0).setMinWidth(0); tableNotes.getColumnModel().getColumn(0).setMaxWidth(0); tableNotes.getColumnModel().getColumn(0).setPreferredWidth(0);

        JScrollPane noteScroll = new JScrollPane(tableNotes); noteScroll.setBorder(BorderFactory.createEmptyBorder()); noteScroll.getViewport().setBackground(Color.WHITE);
        notesPanel.add(noteScroll, BorderLayout.CENTER);

        JPanel notesFooter = new JPanel(new FlowLayout(FlowLayout.LEFT)); notesFooter.setBackground(BG_PAGE);
        JButton btnAdd = new JButton("Ajouter une note"); btnAdd.setBackground(new Color(22,163,74)); btnAdd.setForeground(Color.WHITE); btnAdd.setFont(new Font("Segoe UI", Font.BOLD,14)); btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> showAddNoteDialog());
        JButton btnSave = new JButton("Enregistrer tout"); btnSave.setBackground(new Color(59,130,246)); btnSave.setForeground(Color.WHITE); btnSave.setFont(new Font("Segoe UI", Font.BOLD,14)); btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> { 
            new Thread(()->{ 
                javax.swing.table.DefaultTableModel mModel=(javax.swing.table.DefaultTableModel)tableNotes.getModel(); 
                int rows=mModel.getRowCount(); 
                boolean allOk=true; 
                for (int i=0;i<rows;i++){ 
                    String login=String.valueOf(mModel.getValueAt(i,0)); 
                    String matiere=String.valueOf(mModel.getValueAt(i,4)); 
                    String moy1s=String.valueOf(mModel.getValueAt(i,5)); 
                    String moy2s=String.valueOf(mModel.getValueAt(i,6)); 
                    Double moy1=(moy1s==null||moy1s.isEmpty())?null:Double.valueOf(moy1s); 
                    Double moy2=(moy2s==null||moy2s.isEmpty())?null:Double.valueOf(moy2s); 
                    try{ 
                        java.util.Map<String,Object> resp = tdtp.modeles.EtudiantApi.upsertNote(login,matiere,null,moy1,moy2,1); 
                        if (resp==null||!Boolean.TRUE.equals(resp.get("success"))){ 
                            allOk=false; 
                        } 
                    }catch(Exception ex){ 
                        ex.printStackTrace(); 
                        allOk=false; 
                    } 
                } 
                final boolean ok=allOk; 
                javax.swing.SwingUtilities.invokeLater(()->{ 
                    if (ok){ 
                        JOptionPane.showMessageDialog(this, "Notes enregistrées", "Succès", JOptionPane.INFORMATION_MESSAGE); 
                    } else { 
                        JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement", "Erreur", JOptionPane.ERROR_MESSAGE); 
                    } 
                }); 
            }).start();
        });
        JButton btnDelete = new JButton("Supprimer la sélection"); btnDelete.setBackground(new Color(239,68,68)); btnDelete.setForeground(Color.WHITE); btnDelete.setFont(new Font("Segoe UI", Font.BOLD,14)); btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteSelectedNotes());
        notesFooter.add(btnAdd); notesFooter.add(Box.createHorizontalStrut(10)); notesFooter.add(btnSave); notesFooter.add(Box.createHorizontalStrut(10)); notesFooter.add(btnDelete); notesPanel.add(notesFooter, BorderLayout.SOUTH);

        // Ajouter le panel des notes à la vue principale
        add(notesPanel, BorderLayout.CENTER);

        // Initial load
        refreshNotes();
        // =========================================================================
        // 3. LOGIQUE DE NAVIGATION
        // =========================================================================

        // Hook header navigation buttons to the main panel
        setupNavigation(panelPrincipal);
        
     // --- ACTION : PASSER AU PANEL 2 ---
    }

    /**
     * Applique un style moderne au JTable.
     */
    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(219, 234, 254)); // Bleu très clair sélection
        table.setSelectionForeground(TEXT_DARK);
        
        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(TEXT_DARK);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
        
        // Centrer les colonnes de notes
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=1; i<table.getColumnCount(); i++) {
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
            // Déjà sur la page
            ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_PROMOTION);
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
            refreshNotes();
        });
        
        if (enseignants != null) {
            enseignants.addActionListener(e -> {
                System.out.println("DEBUG: 'Enseignants' clicked — attempting to show " + ACTION_ENSEIGNANTS);
                ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, ACTION_ENSEIGNANTS);
            });
        }
    }
    /**
     * Popule les filtres Classe/Matière depuis l'API
     */
    private void populateNoteCombos() {
        new Thread(() -> {
            try {
                java.util.List<java.util.Map<String,Object>> promos = tdtp.modeles.PromotionApi.getPromotions();
                java.util.List<java.util.Map<String,Object>> notes = tdtp.modeles.EtudiantApi.getNotes();
                final java.util.List<String> promoItems = new java.util.ArrayList<>();
                promoItems.add("Tous");
                for (java.util.Map<String,Object> p : promos) {
                    Object it = p.get("intitule_a");
                    promoItems.add(it != null ? String.valueOf(it) : String.valueOf(p.get("num_p")));
                }
                java.util.Set<String> matSet = new java.util.LinkedHashSet<>();
                for (java.util.Map<String,Object> n : notes) {
                    Object m = n.getOrDefault("intitule", n.getOrDefault("matiere", ""));
                    if (m != null && !String.valueOf(m).trim().isEmpty()) matSet.add(String.valueOf(m));
                }
                final java.util.List<String> matList = new java.util.ArrayList<>();
                matList.add("Tous"); matList.addAll(matSet);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    comboClasse2.removeAllItems(); for (String s : promoItems) comboClasse2.addItem(s);
                    comboMatiere2.removeAllItems(); for (String s : matList) comboMatiere2.addItem(s);
                });
            } catch (Exception ex) { ex.printStackTrace(); }
        }).start();
    }

    /**
     * Charge la liste des étudiants dans le modèle fourni.
     */
    private void loadEtudiants(final javax.swing.table.DefaultTableModel etuModel) {
        etuModel.setRowCount(0);
        etuModel.addRow(new Object[]{"", "Chargement...", "", ""});
        new Thread(() -> {
            try {
                java.util.List<java.util.Map<String,Object>> list = tdtp.modeles.EtudiantApi.getInfosEtudiants();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    etuModel.setRowCount(0);
                    if (list == null || list.isEmpty()) {
                        etuModel.addRow(new Object[]{"", "Aucune donnée", "", ""});
                    } else {
                        for (java.util.Map<String,Object> m : list) {
                            Object login = m.getOrDefault("login_e", m.getOrDefault("login", m.getOrDefault("login_u","")));
                            Object nom = m.getOrDefault("nom_e", m.getOrDefault("nom", m.getOrDefault("nom_u","")));
                            Object prenom = m.getOrDefault("prenom_e", m.getOrDefault("prenom", m.getOrDefault("prenom_u","")));
                            Object mail = m.getOrDefault("mail_e", m.getOrDefault("mail", m.getOrDefault("mail_u","")));
                            etuModel.addRow(new Object[]{login, nom, prenom, mail});
                        }
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    etuModel.setRowCount(0);
                    etuModel.addRow(new Object[]{"", "Erreur: " + ex.getMessage(), "", ""});
                });
            }
        }).start();
    }

    /**
     * Recharge les notes depuis l'API et met à jour le tableau.
     */
    public void refreshNotes() {
        // Exécuter hors EDT
        new Thread(() -> {
            try {
                tdtp.modeles.Etudiant e = new tdtp.modeles.Etudiant();
                java.util.List<java.util.Map<String, Object>> notes = e.consulterNotes();

                // Construire le modèle de table
                javax.swing.table.DefaultTableModel mModel = (javax.swing.table.DefaultTableModel) tableNotes.getModel();
                // Réinitialiser
                javax.swing.SwingUtilities.invokeLater(() -> {
                    mModel.setRowCount(0);
                });

                String selectedPromo = comboClasse2 != null ? (String) comboClasse2.getSelectedItem() : null;
                String selectedMatiere = comboMatiere2 != null ? (String) comboMatiere2.getSelectedItem() : null;
                for (java.util.Map<String, Object> row : notes) {
                    String login = String.valueOf(row.getOrDefault("login_u", ""));
                    String nom = String.valueOf(row.getOrDefault("nom_u", ""));
                    String prenom = String.valueOf(row.getOrDefault("prenom_u", ""));
                    String groupe = String.valueOf(row.getOrDefault("nom_g", ""));
                    String matiere = String.valueOf(row.getOrDefault("intitule_n", ""));
                    String moy1 = String.valueOf(row.getOrDefault("moy_prem_semestre", ""));
                    String moy2 = String.valueOf(row.getOrDefault("moy_deux_semestre", ""));
                    String moyAnn = String.valueOf(row.getOrDefault("moy_annee", ""));
                    String valide = String.valueOf(row.getOrDefault("aValideMatiere", ""));

                    // Filter by selected matiere if any
                    if (selectedMatiere != null && !"Tous".equals(selectedMatiere) && !selectedMatiere.equals(matiere)) continue;

                    // Filter by selected promotion if any (match groupe)
                    if (selectedPromo != null && !"Tous".equals(selectedPromo) && !selectedPromo.equals(groupe)) continue;

                    Object[] r = { login, nom, prenom, groupe, matiere, moy1, moy2, moyAnn, valide };
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        mModel.addRow(r);
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    
    // ==================== MÉTHODES CRUD ====================
    
    private void showAddNoteDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Login étudiant
        JTextField loginField = new JTextField(15);
        
        // Numéro note
        JTextField numNoteField = new JTextField(10);
        
        // Notes
        JTextField noteS1Field = new JTextField(10);
        JTextField noteS2Field = new JTextField(10);
        JTextField noteAnneeField = new JTextField(10);
        
        int y = 0;
        gbc.gridy = y++; panel.add(new JLabel("Login étudiant:"), gbc); gbc.gridx = 1; panel.add(loginField, gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Numéro note:"), gbc); gbc.gridx = 1; panel.add(numNoteField, gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Note S1:"), gbc); gbc.gridx = 1; panel.add(noteS1Field, gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Note S2:"), gbc); gbc.gridx = 1; panel.add(noteS2Field, gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Note année:"), gbc); gbc.gridx = 1; panel.add(noteAnneeField, gbc); gbc.gridx = 0;
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter une note", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String login = loginField.getText().trim();
            String numNote = numNoteField.getText().trim();
            String noteS1 = noteS1Field.getText().trim();
            String noteS2 = noteS2Field.getText().trim();
            String noteAnnee = noteAnneeField.getText().trim();
            
            // Validation
            if (login.isEmpty() || numNote.isEmpty() || (noteS1.isEmpty() && noteS2.isEmpty() && noteAnnee.isEmpty())) {
                JOptionPane.showMessageDialog(this, "Login, numéro note et au moins une note obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            new Thread(() -> {
                try {
                    Double nS1 = noteS1.isEmpty() ? null : Double.parseDouble(noteS1);
                    Double nS2 = noteS2.isEmpty() ? null : Double.parseDouble(noteS2);
                    Double nAnnee = noteAnnee.isEmpty() ? null : Double.parseDouble(noteAnnee);
                    
                    // Créer les données pour l'API
                    Map<String, Object> noteData = new HashMap<>();
                    noteData.put("etudiant", login);  // Login, pas numéro
                    noteData.put("matiere", "Général");  // Matière par défaut
                    if (nS1 != null) noteData.put("moy_prem_semestre", nS1);
                    if (nS2 != null) noteData.put("moy_deux_semestre", nS2);
                    if (nAnnee != null) noteData.put("note", nAnnee);  // note = note année
                    noteData.put("coef", 1);
                    
                    System.out.println("DEBUG: Envoi note=" + noteData);
                    
                    Map<String, Object> resp = tdtp.modeles.EtudiantApi.addNote(noteData);
                    
                    SwingUtilities.invokeLater(() -> {
                        if (resp != null && Boolean.TRUE.equals(resp.get("success"))) {
                            JOptionPane.showMessageDialog(this, "Note ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            refreshNotes();
                        } else {
                            JOptionPane.showMessageDialog(this, "Erreur: " + resp.get("error"), "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        }
    }
    
    private void editNote(int row) {
        DefaultTableModel model = (DefaultTableModel) tableNotes.getModel();
        String login = String.valueOf(model.getValueAt(row, 0));
        String etudiant = String.valueOf(model.getValueAt(row, 1));
        String matiere = String.valueOf(model.getValueAt(row, 2));
        String moy1 = String.valueOf(model.getValueAt(row, 3));
        String moy2 = String.valueOf(model.getValueAt(row, 4));
        String moyAnn = String.valueOf(model.getValueAt(row, 5));
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField moy1Field = new JTextField(moy1, 10);
        JTextField moy2Field = new JTextField(moy2, 10);
        JTextField moyAnnField = new JTextField(moyAnn, 10);
        
        int y = 0;
        gbc.gridy = y++; panel.add(new JLabel("Login:"), gbc); gbc.gridx = 1; panel.add(new JLabel(login), gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Étudiant:"), gbc); gbc.gridx = 1; panel.add(new JLabel(etudiant), gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Matière:"), gbc); gbc.gridx = 1; panel.add(new JLabel(matiere), gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Moy. S1:"), gbc); gbc.gridx = 1; panel.add(moy1Field, gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Moy. S2:"), gbc); gbc.gridx = 1; panel.add(moy2Field, gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Moy. Annuelle:"), gbc); gbc.gridx = 1; panel.add(moyAnnField, gbc); gbc.gridx = 0;
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Modifier une note", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String newMoy1 = moy1Field.getText().trim();
            String newMoy2 = moy2Field.getText().trim();
            String newMoyAnn = moyAnnField.getText().trim();
            
            new Thread(() -> {
                try {
                    Double m1 = newMoy1.isEmpty() ? null : Double.parseDouble(newMoy1);
                    Double m2 = newMoy2.isEmpty() ? null : Double.parseDouble(newMoy2);
                    Double mAnn = newMoyAnn.isEmpty() ? null : Double.parseDouble(newMoyAnn);
                    
                    // Créer les données pour l'API (format attendu par upsertNote)
                    Map<String, Object> noteData = new HashMap<>();
                    noteData.put("etudiant", login);
                    noteData.put("matiere", matiere);  // Garder la matière existante
                    noteData.put("moy_prem_semestre", m1);
                    noteData.put("moy_deux_semestre", m2);
                    noteData.put("coef", 1);  // Coefficient par défaut
                    
                    System.out.println("DEBUG MODIFICATION: données note=" + noteData);
                    
                    Map<String, Object> resp = tdtp.modeles.EtudiantApi.updateNote(noteData);
                    
                    SwingUtilities.invokeLater(() -> {
                        if (resp != null && Boolean.TRUE.equals(resp.get("success"))) {
                            JOptionPane.showMessageDialog(this, "Note modifiée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            refreshNotes();
                        } else {
                            JOptionPane.showMessageDialog(this, "Erreur lors de la modification de la note", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        }
    }
    
    private void deleteNote(int row) {
        DefaultTableModel model = (DefaultTableModel) tableNotes.getModel();
        String login = String.valueOf(model.getValueAt(row, 0));
        String etudiant = String.valueOf(model.getValueAt(row, 1));
        String matiere = String.valueOf(model.getValueAt(row, 2));
        String moy1 = String.valueOf(model.getValueAt(row, 3));
        String moy2 = String.valueOf(model.getValueAt(row, 4));
        String moyAnn = String.valueOf(model.getValueAt(row, 5));
        
        // Formulaire de confirmation avec détails
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int y = 0;
        gbc.gridy = y++; panel.add(new JLabel("Voulez-vous vraiment supprimer cette note ?"), gbc); gbc.gridx = 1;
        gbc.gridy = y++; panel.add(new JLabel(" "), gbc); gbc.gridx = 1; // Ligne vide
        gbc.gridy = y++; panel.add(new JLabel("Login:"), gbc); gbc.gridx = 1; panel.add(new JLabel(login), gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Étudiant:"), gbc); gbc.gridx = 1; panel.add(new JLabel(etudiant), gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Matière:"), gbc); gbc.gridx = 1; panel.add(new JLabel(matiere), gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Moy. S1:"), gbc); gbc.gridx = 1; panel.add(new JLabel(moy1), gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Moy. S2:"), gbc); gbc.gridx = 1; panel.add(new JLabel(moy2), gbc); gbc.gridx = 0;
        gbc.gridy = y++; panel.add(new JLabel("Moy. Annuelle:"), gbc); gbc.gridx = 1; panel.add(new JLabel(moyAnn), gbc); gbc.gridx = 0;
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Supprimer une note", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                try {
                    // Créer les données pour l'API (format attendu)
                    Map<String, Object> noteData = new HashMap<>();
                    noteData.put("etudiant", login);
                    noteData.put("matiere", matiere);
                    
                    System.out.println("DEBUG SUPPRESSION: données note=" + noteData);
                    
                    Map<String, Object> resp = tdtp.modeles.EtudiantApi.deleteNote(noteData);
                    
                    SwingUtilities.invokeLater(() -> {
                        if (resp != null && Boolean.TRUE.equals(resp.get("success"))) {
                            JOptionPane.showMessageDialog(this, "Note supprimée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            refreshNotes();
                        } else {
                            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la note", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        }
    }
    
    // ==================== CLASSES POUR LES BOUTONS D'ACTION ====================
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Modifier/Supprimer");
            setBackground(new Color(59, 130, 246));
            setForeground(Color.WHITE);
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Modifier/Supprimer" : value.toString();
            button.setText(label);
            button.setBackground(new Color(59, 130, 246));
            button.setForeground(Color.WHITE);
            isPushed = true;
            currentRow = row;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Afficher un menu contextuel pour choisir entre modifier et supprimer
                JPopupMenu menu = new JPopupMenu();
                JMenuItem editItem = new JMenuItem("Modifier");
                JMenuItem deleteItem = new JMenuItem("Supprimer");
                
                editItem.addActionListener(e -> editNote(currentRow));
                deleteItem.addActionListener(e -> deleteNote(currentRow));
                
                menu.add(editItem);
                menu.add(deleteItem);
                menu.show(button, 0, button.getHeight());
            }
            isPushed = false;
            return label;
        }
    }
    
    private void deleteSelectedNotes() {
        int[] selectedRows = tableNotes.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins une note à supprimer", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Voulez-vous vraiment supprimer les " + selectedRows.length + " note(s) sélectionnée(s) ?", 
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                int successCount = 0;
                int failCount = 0;
                
                for (int row : selectedRows) {
                    try {
                        DefaultTableModel model = (DefaultTableModel) tableNotes.getModel();
                        String login = String.valueOf(model.getValueAt(row, 0));
                        String matiere = String.valueOf(model.getValueAt(row, 4));
                        
                        Map<String, Object> noteData = new HashMap<>();
                        noteData.put("etudiant", login);
                        noteData.put("matiere", matiere);
                        
                        System.out.println("DEBUG: Suppression note=" + noteData);
                        
                        Map<String, Object> resp = tdtp.modeles.EtudiantApi.deleteNote(noteData);
                        
                        if (resp != null && Boolean.TRUE.equals(resp.get("success"))) {
                            successCount++;
                        } else {
                            failCount++;
                            System.out.println("Erreur suppression: " + resp);
                        }
                    } catch (Exception ex) {
                        failCount++;
                        System.out.println("Exception suppression: " + ex.getMessage());
                    }
                }
                
                final int finalSuccess = successCount;
                final int finalFail = failCount;
                
                SwingUtilities.invokeLater(() -> {
                    if (finalFail == 0) {
                        JOptionPane.showMessageDialog(this, 
                            finalSuccess + " note(s) supprimée(s) avec succès", 
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            finalSuccess + " note(s) supprimée(s), " + finalFail + " échec(s)", 
                            "Résultat", JOptionPane.WARNING_MESSAGE);
                    }
                    refreshNotes();
                });
            }).start();
        }
    }
    
}
