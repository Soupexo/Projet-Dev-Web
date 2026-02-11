package tdtp;

import tdtp.controleurs.accueil.ControleurAccueil;
import tdtp.vues.accueil.VueAccueil;
import tdtp.vues.connexion.*;
import tdtp.vues.espaceetudiant.*;
import tdtp.controleurs.connexion.ControleurConnexionResponsableFormation;
import tdtp.vues.utilitaires.*;

import tdtp.vues.enseignantResponsableFormation.VueFormulaireEnseignant;
import tdtp.vues.enseignantResponsableFormation.VueFormulaireEtudiant;
import tdtp.vues.enseignantResponsableFormation.VueFormulaireGroupe;
import tdtp.vues.enseignantResponsableFormation.VueFormulairePromotion;
import tdtp.vues.enseignantResponsableFormation.VueFormulaireSondage;
import tdtp.vues.enseignantResponsableFormation.VueGroupesEtudiant;
import tdtp.vues.enseignantResponsableFormation.VuePromotionEtudiant;
import tdtp.vues.enseignantResponsableFormation.VueEnseignantsEtudiant;
import tdtp.vues.enseignantResponsableFormation.VueEtudiantsEtudiant;

import tdtp.vues.espaceenseignant.*;

public class ApplicationTPTD {

	public static void main(String[] args) {
	    Fenetre fenetre = new Fenetre("Gestion de groupes TD/TP");
	    PanelCardLayout panelPrincipal = new PanelCardLayout();

	    // VUES COMMUNES
	    VueAccueil vueAccueil = new VueAccueil(panelPrincipal);
	    VueConnexionEnseignant vueConnexionEnseignant = new VueConnexionEnseignant(panelPrincipal);
	    VueConnexionEtudiant vueConnexionEtudiant = new VueConnexionEtudiant(panelPrincipal);
	    VueConnexionResponsableFormation vueConnexionResponsableFormation = new VueConnexionResponsableFormation(panelPrincipal);

	    // PANEL ENSEIGNANT
	    PanelCardLayout panelEnseignant = new PanelCardLayout();
	    tdtp.vues.espaceenseignant.VueEtudiantsEtudiant vueAccueilEnseignant = new tdtp.vues.espaceenseignant.VueEtudiantsEtudiant(panelEnseignant);
	    panelEnseignant.add(vueAccueilEnseignant, "GESTION_ETUDIANTS_ENSEIGNANT");

	    // PANEL ÉTUDIANT
	    PanelCardLayout panelEtudiant = new PanelCardLayout();
	    tdtp.vues.espaceetudiant.VueEtudiantsEtudiant vueEtudiantsEtudiant = new  tdtp.vues.espaceetudiant.VueEtudiantsEtudiant(panelEtudiant);
	    VueAccueilEtudiant vueAccueilEtudiant = new VueAccueilEtudiant(panelEtudiant);
	    VueNotesEtudiant vueNotesEtudiant = new VueNotesEtudiant(panelEtudiant);
	    VueSondageEtudiant vueSondageEtudiant = new VueSondageEtudiant(panelEtudiant);
	    
	    panelEtudiant.add(vueAccueilEtudiant, "ACCUEIL_ETUDIANT");
	    panelEtudiant.add(vueEtudiantsEtudiant, "GESTION_ETUDIANTS_ETUDIANT");
	    panelEtudiant.add(vueNotesEtudiant, "GESTION_NOTES_ETUDIANT");
	    panelEtudiant.add(vueSondageEtudiant, "GESTION_SONDAGES_ETUDIANT");

	    // PANEL RESPONSABLE FORMATION
	    PanelCardLayout panelResponsableFormation = new PanelCardLayout();
	    
	    // Création des vues pour l'espace responsable de formation
	    tdtp.vues.enseignantResponsableFormation.VueAccueilEtudiant vueAccueilResponsableFormation = new tdtp.vues.enseignantResponsableFormation.VueAccueilEtudiant(panelResponsableFormation);
	    tdtp.vues.enseignantResponsableFormation.VueEnseignantsEtudiant vueEnseignantsEtudiantRF = new tdtp.vues.enseignantResponsableFormation.VueEnseignantsEtudiant(panelResponsableFormation);
	    tdtp.vues.enseignantResponsableFormation.VueEtudiantsEtudiant vueEtudiantsEtudiantRF = new tdtp.vues.enseignantResponsableFormation.VueEtudiantsEtudiant(panelResponsableFormation);
	    tdtp.vues.enseignantResponsableFormation.VueGroupesEtudiant vueGroupesEtudiantRF = new tdtp.vues.enseignantResponsableFormation.VueGroupesEtudiant(panelResponsableFormation);
	    tdtp.vues.enseignantResponsableFormation.VueNotesEtudiant vueNotesEtudiantRF = new tdtp.vues.enseignantResponsableFormation.VueNotesEtudiant(panelResponsableFormation);
	    tdtp.vues.enseignantResponsableFormation.VuePromotionEtudiant vuePromotionEtudiantRF = new tdtp.vues.enseignantResponsableFormation.VuePromotionEtudiant(panelResponsableFormation);
	    tdtp.vues.enseignantResponsableFormation.VueSondageEtudiant vueSondageEtudiantRF = new tdtp.vues.enseignantResponsableFormation.VueSondageEtudiant(panelResponsableFormation);
	    
	    // Ajout des vues au panel responsable de formation avec le suffixe _RESPONSABLE_FORMATION
	    panelResponsableFormation.add(vueAccueilResponsableFormation, "ACCUEIL_RESPONSABLE_FORMATION");
	    panelResponsableFormation.add(vueEnseignantsEtudiantRF, "GESTION_ENSEIGNANTS_RESPONSABLE_FORMATION");
	    panelResponsableFormation.add(vueEtudiantsEtudiantRF, "GESTION_ETUDIANTS_RESPONSABLE_FORMATION");
	    panelResponsableFormation.add(vueGroupesEtudiantRF, "GESTION_GROUPES_RESPONSABLE_FORMATION");
	    panelResponsableFormation.add(vueNotesEtudiantRF, "GESTION_NOTES_RESPONSABLE_FORMATION");
	    panelResponsableFormation.add(vuePromotionEtudiantRF, "GESTION_PROMOTION_RESPONSABLE_FORMATION");
	    panelResponsableFormation.add(vueSondageEtudiantRF, "GESTION_SONDAGES_RESPONSABLE_FORMATION");

	    // AJOUT AU PANEL PRINCIPAL
	    panelPrincipal.add(vueAccueil, "ACCUEIL");
	    panelPrincipal.add(vueConnexionEnseignant, "CONNECTE_ENSEIGNANT");
	    panelPrincipal.add(vueConnexionEtudiant, "CONNECTE_ETUDIANT");
	    panelPrincipal.add(vueConnexionResponsableFormation, "CONNECTE_RESPONSABLE_FORMATION");
	    
	    // Ajout des espaces utilisateurs
	    panelPrincipal.add(panelEnseignant, "ESPACE_ENSEIGNANT");
	    panelPrincipal.add(panelEtudiant, "ESPACE_ETUDIANT");
	    panelPrincipal.add(panelResponsableFormation, "ESPACE_RESPONSABLE_FORMATION");

	    // CONTRÔLEURS
	    ControleurAccueil controleurAccueil = new ControleurAccueil(panelPrincipal);
	    ControleurConnexionResponsableFormation controleurConnexionRF = new ControleurConnexionResponsableFormation(panelPrincipal);

	    fenetre.add(panelPrincipal);
	    fenetre.setVisible(true);
	}
}