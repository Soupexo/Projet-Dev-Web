package tdtp.controleurs.connexion;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tdtp.vues.connexion.VueConnexionResponsableFormation;
import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Contrôleur de la vue d'accueil de l'application.
 * Permet la navigation vers différentes sections comme la gestion des maisons,
 * le catalogue des maisons, l'inscription des étudiants et les restaurants universitaires.
 */
public class ControleurConnexionResponsableFormation implements ActionListener {
	static public final String ACTION_ACCEDER_ESPACE_RESPONSABLE_FORMATION = "ACCEDER_ESPACE_RESPONSABLE_FORMATION";
	
  private CardLayout cardLayout;
  private VueConnexionResponsableFormation vueConnexionResponsableFormation;
  private PanelCardLayout panelPrincipal;

    /**
     * Constructeur du contrôleur de l'accueil.
     * 
     * @param panelPrincipal  Panel principal contenant les différentes vues.
     * @param accueilObserver Observateur permettant la mise à jour des éléments.
     */
  public ControleurConnexionResponsableFormation(PanelCardLayout panelPrincipal) {
    this.panelPrincipal = panelPrincipal;
    this.cardLayout = (CardLayout) panelPrincipal.getLayout();
    this.vueConnexionResponsableFormation = (VueConnexionResponsableFormation) panelPrincipal.getVue("CONNECTE_RESPONSABLE_FORMATION");

    vueConnexionResponsableFormation.getButtonAcceder().addActionListener(this);
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
      case ACTION_ACCEDER_ESPACE_RESPONSABLE_FORMATION:
        afficherVue("ESPACE_RESPONSABLE_FORMATION");
        break;
    }
  }
}
