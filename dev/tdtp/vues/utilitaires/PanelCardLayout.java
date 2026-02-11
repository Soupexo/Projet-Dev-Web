package tdtp.vues.utilitaires;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JPanel;

/**
 * Classe représentant un panneau utilisant un gestionnaire de disposition {@link CardLayout}.
 * Permet la gestion dynamique des vues en les ajoutant, les remplaçant et les récupérant.
 */
public class PanelCardLayout extends JPanel {  

  /**
   * Constructeur du panneau avec un gestionnaire de disposition {@link CardLayout}.
   */
  public PanelCardLayout() {
	  super(new CardLayout());
  }

    /**
     * Ajoute une vue à ce panneau en lui attribuant un nom.
     * 
     * @param comp        Composant à ajouter.
     * @param constraints Nom associé à la vue.
     */
  @Override
  public void add(Component comp, Object constraints) {
    if (constraints instanceof String) {
      comp.setName((String) constraints);
    }
    super.add(comp, constraints);
  }

    /**
     * Remplace une vue existante par une nouvelle et l'affiche immédiatement.
     * 
     * @param NomVue       Nom de la vue à remplacer.
     * @param nouvelleVue  Nouvelle vue à afficher.
     */
  public void remplacerVue(String NomVue, Component nouvelleVue) {
    CardLayout cl = (CardLayout) getLayout();
    cl.show(this, NomVue);
    for (Component comp : getComponents()) {
      if (comp.getName() != null && comp.getName().equals(NomVue)) {
        remove(comp);
        break;
      }
    }
    nouvelleVue.setName(NomVue);
    add(nouvelleVue, NomVue);
    cl.show(this, NomVue);
  }


    /**
     * Récupère une vue existante à partir de son nom.
     * 
     * @param NomVue Nom de la vue à récupérer.
     * @return Composant correspondant à la vue, ou {@code null} si introuvable.
     */
  public Component getVue(String NomVue) {
    for (Component comp : getComponents()) {
      if (comp.getName() != null && comp.getName().equals(NomVue)) {
        return comp;
      }
    }
    return null;
  }
}
