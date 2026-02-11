package tdtp.controleurs.enseignantResponsableFormation;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tdtp.vues.enseignantResponsableFormation.VueEnseignantsEtudiant;
import tdtp.vues.utilitaires.PanelCardLayout;

/**
 * Contrôleur de navigation pour l'espace Enseignant (Responsable de formation).
 * Gère l'accès aux sondages, notes, promotions, groupes et étudiants.
 */
public class ControleurEnseignant implements ActionListener {

    public static final String ACTION_SONDAGES   = "GESTION_SONDAGES_RESPONSABLE_FORMATION";
    public static final String ACTION_NOTES      = "GESTION_NOTES_RESPONSABLE_FORMATION";
    public static final String ACTION_PROMOTION  = "GESTION_PROMOTION_RESPONSABLE_FORMATION";
    public static final String ACTION_GROUPES    = "GESTION_GROUPES_RESPONSABLE_FORMATION";
    public static final String ACTION_ETUDIANTS  = "GESTION_ETUDIANTS_RESPONSABLE_FORMATION";
    public static final String ACTION_ENSEIGNANTS = "GESTION_ENSEIGNANTS_RESPONSABLE_FORMATION";

    private CardLayout cardLayout;
    private VueEnseignantsEtudiant vueEnseignant;
    private PanelCardLayout panelPrincipal;

    /**
     * Constructeur du contrôleur Enseignant.
     *
     * @param panelPrincipal Panel principal contenant les vues Enseignant
     */
    public ControleurEnseignant(PanelCardLayout panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        this.cardLayout = (CardLayout) panelPrincipal.getLayout();
        this.vueEnseignant =
                (VueEnseignantsEtudiant ) panelPrincipal.getVue(ACTION_SONDAGES);

        // Listeners
        vueEnseignant.getSondages().addActionListener(this);
        vueEnseignant.getNotes().addActionListener(this);
        vueEnseignant.getPromotion().addActionListener(this);
        vueEnseignant.getGroupes().addActionListener(this);
        vueEnseignant.getEtudiants().addActionListener(this);
    }

    /**
     * Affiche une vue via le CardLayout
     */
    private void afficherVue(String nomVue) {
        cardLayout.show(panelPrincipal, nomVue);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ACTION_SONDAGES:
                afficherVue(ACTION_SONDAGES);
                break;
            case ACTION_NOTES:
                afficherVue(ACTION_NOTES);
                break;
            case ACTION_PROMOTION:
                afficherVue(ACTION_PROMOTION);
                break;
            case ACTION_GROUPES:
                afficherVue(ACTION_GROUPES);
                break;
            case ACTION_ETUDIANTS:
                afficherVue(ACTION_ETUDIANTS);
                break;
            case ACTION_ENSEIGNANTS:
                afficherVue(ACTION_ENSEIGNANTS);
                break;
        }
    }
}
