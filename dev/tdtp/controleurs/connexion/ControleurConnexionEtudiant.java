package tdtp.controleurs.connexion;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tdtp.vues.accueil.VueAccueil;
import tdtp.vues.connexion.VueConnexionEtudiant;
import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Contrôleur de la vue d'accueil de l'application.
 * Permet la navigation vers différentes sections comme la gestion des maisons,
 * le catalogue des maisons, l'inscription des étudiants et les restaurants universitaires.
 */
public class ControleurConnexionEtudiant implements ActionListener {
	static public final String ACTION_ACCEDER_ESPACE_ETUDIANT = "ACCEDER_ESPACE_ETUDIANT";
	
  private CardLayout cardLayout;
  private VueConnexionEtudiant vueConnexionEtudiant;
  private PanelCardLayout panelPrincipal;

    /**
     * Constructeur du contrôleur de l'accueil.
     * 
     * @param panelPrincipal  Panel principal contenant les différentes vues.
     * @param accueilObserver Observateur permettant la mise à jour des éléments.
     */
  public ControleurConnexionEtudiant(PanelCardLayout panelPrincipal) {
    this.panelPrincipal = panelPrincipal;
    this.cardLayout = (CardLayout) panelPrincipal.getLayout();
    this.vueConnexionEtudiant = (VueConnexionEtudiant) panelPrincipal.getVue("CONNEXION_ETUDIANT");

    vueConnexionEtudiant.getButtonAcceder().addActionListener(this);
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
      case ACTION_ACCEDER_ESPACE_ETUDIANT:
    	  afficherVue("ESPACE_ENSEIGNANT");
        break;

    }
  }
}
