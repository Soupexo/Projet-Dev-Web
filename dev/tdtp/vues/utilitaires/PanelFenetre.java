package tdtp.vues.utilitaires;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Classe représentant un panneau contenant une fenêtre avec un en-tête personnalisé.
 * Permet d'afficher un titre et des boutons de navigation.
 */
public class PanelFenetre extends JPanel {
  private String titrePage;
  private JButton boutonAccueil;
  private JButton buttonCustom;

    /**
     * Initialise la mise en page et les éléments du panneau.
     * 
     * @param titrePage       Titre de la fenêtre.
     * @param hasButtonCustom Indique si un bouton personnalisé doit être affiché.
     */
  private void initialiserPanel(String titrePage, boolean hasButtonCustom) {
    this.titrePage = titrePage;
    setLayout(new BorderLayout());

    JPanel panelHeader = new JPanel(new BorderLayout());

    // Left
    if (hasButtonCustom) {
      JPanel panelCustom = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelCustom.add(buttonCustom);
      panelHeader.add(panelCustom, BorderLayout.WEST);
    } else {
      panelHeader.add(new JPanel(), BorderLayout.WEST);
    }

    // Center (Title)
    JPanel panelTitre = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel titreFenetre = new JLabel(titrePage);
    titreFenetre.setFont(new Font(titreFenetre.getFont().getName(), Font.BOLD, 20));
    panelTitre.add(titreFenetre);
    panelHeader.add(panelTitre, BorderLayout.CENTER);

    // Right (Accueil as image button)
    JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    try {
      ImageIcon icon = new ImageIcon(getClass().getResource("ciup.png"));
      Image scaled = icon.getImage().getScaledInstance(110, 40, Image.SCALE_SMOOTH); // Bigger, clearer
      icon = new ImageIcon(scaled);
      boutonAccueil = new JButton(icon);
      boutonAccueil.setBorderPainted(false);
      boutonAccueil.setContentAreaFilled(false);
      boutonAccueil.setFocusPainted(false);
      boutonAccueil.setOpaque(false);
      boutonAccueil.setCursor(new Cursor(Cursor.HAND_CURSOR));
    } catch (Exception e) {
      // incase image fails
      boutonAccueil = new JButton("Menu");
    }

    panelBouton.add(boutonAccueil);
    panelHeader.add(panelBouton, BorderLayout.EAST);

    panelHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
    add(panelHeader, BorderLayout.NORTH);
  }

    /**
     * Constructeur de la fenêtre avec un bouton personnalisé.
     * 
     * @param titrePage   Titre de la fenêtre.
     * @param buttonCustom Bouton personnalisé à afficher.
     */
  public PanelFenetre(String titrePage, JButton buttonCustom) {
    this.buttonCustom = buttonCustom;
    initialiserPanel(titrePage, true);
  }

    /**
     * Constructeur de la fenêtre sans bouton personnalisé.
     * 
     * @param titrePage Titre de la fenêtre.
     */
  public PanelFenetre(String titrePage) {
    initialiserPanel(titrePage, false);
  }

    /**
     * Rafraîchit le panneau en le réinitialisant.
     */
  public void rafraichirPanel() {
    removeAll();
    initialiserPanel(titrePage, buttonCustom != null);
    revalidate();
    repaint();
  }

    /**
     * Retourne le bouton permettant de revenir à l'accueil.
     * 
     * @return Bouton d'accueil.
     */
  public JButton getBoutonAccueil() {
    return boutonAccueil;
  }
}
