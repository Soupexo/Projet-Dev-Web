package tdtp.controleurs.espaceenseignant;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tdtp.vues.enseignantResponsableFormation.VueAccueilEtudiant;
import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Contrôleur de la vue d'accueil étudiant.
 * Permet la navigation entre les vues accessibles aux étudiants :
 * accueil, notes et sondages uniquement.
 */
public class ControleurAccueilEtudiant{
    static public final String ACTION_ACCEDER_ESPACE_ETUDIANT = "ACCEDER_ESPACE_ENSEIGNANT";


    private CardLayout cardLayout;
    private VueAccueilEtudiant vueAccueilEtudiant;
    private PanelCardLayout panelPrincipal;

    /**
     * Constructeur du contrôleur de l'accueil étudiant.
     *
     * @param panelPrincipal Panel principal contenant les différentes vues.
     */
    public ControleurAccueilEtudiant(PanelCardLayout panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        this.cardLayout = (CardLayout) panelPrincipal.getLayout();
        this.vueAccueilEtudiant = (VueAccueilEtudiant) panelPrincipal.getVue("ESPACE_ENSEIGNANT");

    }

    /**
     * Affiche une vue spécifique.
     *
     * @param nomVue Nom de la vue à afficher.
     */
    public void afficherVue(String nomVue) {
        cardLayout.show(panelPrincipal, nomVue);
    }

    /**
     * Gère les événements utilisateur et exécute les actions correspondantes.
     *
     * @param e Événement déclenché par l'utilisateur.
     */
}