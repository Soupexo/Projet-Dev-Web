package tdtp.controleurs.accueil;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tdtp.vues.accueil.VueAccueil;
import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Contrôleur de la vue d'accueil de l'application.
 * Permet la navigation vers différentes sections comme la gestion des maisons,
 * le catalogue des maisons, l'inscription des étudiants et les restaurants universitaires.
 */
public class ControleurAccueil implements ActionListener {
	static public final String ACTION_DEMARRER = "DEMARRER";
	static public final String ACTION_CONNEXION_ENSEIGNANT = "CONNEXION_ENSEIGNANT";
	static public final String ACTION_CONNEXION_ETUDIANT = "CONNEXION_ETUDIANT";
	static public final String ACTION_CONNEXION_RESPONSABLE_FORMATION = "CONNEXION_RESPONSABLE_FORMATION";

  private CardLayout cardLayout;
  private VueAccueil vueAccueil;
  private PanelCardLayout panelPrincipal;

    /**
     * Constructeur du contrôleur de l'accueil.
     * 
     * @param panelPrincipal  Panel principal contenant les différentes vues.
     * @param accueilObserver Observateur permettant la mise à jour des éléments.
     */
  public ControleurAccueil(PanelCardLayout panelPrincipal) {
    this.panelPrincipal = panelPrincipal;
    this.cardLayout = (CardLayout) panelPrincipal.getLayout();
    this.vueAccueil = (VueAccueil) panelPrincipal.getVue("ACCUEIL");
;
    vueAccueil.getButtonConnexionEnseignant().addActionListener(this);
    vueAccueil.getButtonConnexionEtudiant().addActionListener(this);
//    vueAccueil.getButtonRestos().addActionListener(this);
  }

    /**
     * Gère les événements utilisateur et exécute les actions correspondantes.
     * 
     * @param e Événement déclenché par l'utilisateur.
     */
  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
    
      case ACTION_CONNEXION_ENSEIGNANT:
    	  cardLayout = (CardLayout) panelPrincipal.getLayout();
    	  cardLayout.show(panelPrincipal, "CONNECTE_ENSEIGNANT");
        break;
      case ACTION_CONNEXION_ETUDIANT:
    	  cardLayout = (CardLayout) panelPrincipal.getLayout();
    	  cardLayout.show(panelPrincipal, "CONNECTE_ETUDIANT");
        break;
      case ACTION_CONNEXION_RESPONSABLE_FORMATION:
    	  cardLayout = (CardLayout) panelPrincipal.getLayout();
    	  cardLayout.show(panelPrincipal, "CONNECTE_ETUDIANT");
        break;
    }
  }
}
