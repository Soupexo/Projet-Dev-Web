package tdtp.controleurs.enseignantResponsableFormation;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tdtp.vues.accueil.VueAccueil;
import tdtp.vues.connexion.VueConnexionEtudiant;
import tdtp.vues.enseignantResponsableFormation.VueAccueilEtudiant;
import tdtp.vues.enseignantResponsableFormation.VueSondageEtudiant;
import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Contrôleur de la vue d'accueil de l'application.
 * Permet la navigation vers différentes sections comme la gestion des maisons,
 * le catalogue des maisons, l'inscription des étudiants et les restaurants universitaires.
 */
public class ControleurSondageEtudiant implements ActionListener {
    static public final String ACTION_SONDAGES = "GESTION_SONDAGES_RESPONSABLE_FORMATION";
    static public final String ACTION_NOTES = "GESTION_NOTES_RESPONSABLE_FORMATION";
    static public final String ACTION_PROMOTION = "GESTION_PROMOTION_RESPONSABLE_FORMATION";
    static public final String ACTION_GROUPES = "GESTION_GROUPES_RESPONSABLE_FORMATION";
    static public final String ACTION_ETUDIANTS = "GESTION_ETUDIANTS_RESPONSABLE_FORMATION";
    static public final String ACTION_ENSEIGNANTS = "GESTION_ENSEIGNANTS_RESPONSABLE_FORMATION";

  private CardLayout cardLayout;
  private VueSondageEtudiant vueSondageEtudiant;
  private PanelCardLayout panelPrincipal;

    /**
     * Constructeur du contrôleur de l'accueil.
     * 
     * @param panelPrincipal  Panel principal contenant les différentes vues.
     * @param accueilObserver Observateur permettant la mise à jour des éléments.
     */
  public ControleurSondageEtudiant(PanelCardLayout panelPrincipal) {
    this.panelPrincipal = panelPrincipal;
    this.cardLayout = (CardLayout) panelPrincipal.getLayout();
    this.vueSondageEtudiant = (VueSondageEtudiant) panelPrincipal.getVue("GESTION_SONDAGES_RESPONSABLE_FORMATION");


    vueSondageEtudiant.getNotes().addActionListener(this);
    vueSondageEtudiant.getPromotion().addActionListener(this);
    vueSondageEtudiant.getSondages().addActionListener(this);
    vueSondageEtudiant.getGroupes().addActionListener(this);
  }

    /**
     * Gère les événements utilisateur et exécute les actions correspondantes.
     * 
     * @param e Événement déclenché par l'utilisateur.
     */
  	public void afficherVue(String nomVue) {
		cardLayout.show(panelPrincipal, nomVue);
	}
  
  	@Override
    public void actionPerformed(ActionEvent e) {
      switch (e.getActionCommand()) {
        case ACTION_SONDAGES:
            afficherVue("GESTION_SONDAGES_RESPONSABLE_FORMATION");
            break;
        case ACTION_NOTES:
            afficherVue("GESTION_NOTES_RESPONSABLE_FORMATION");
            break;
        case ACTION_PROMOTION:
            afficherVue("GESTION_PROMOTION_RESPONSABLE_FORMATION");
            break;
        case ACTION_GROUPES:
            afficherVue("GESTION_GROUPES_RESPONSABLE_FORMATION");
            break;
        case ACTION_ETUDIANTS:
            afficherVue("GESTION_ETUDIANTS_RESPONSABLE_FORMATION");
            break;
        case ACTION_ENSEIGNANTS:
            afficherVue("GESTION_ENSEIGNANTS_RESPONSABLE_FORMATION");
            break;
      }
    }
}
