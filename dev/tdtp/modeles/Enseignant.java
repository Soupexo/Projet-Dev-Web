package tdtp.modeles;

import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.reflect.TypeToken;

public class Enseignant extends Utilisateur {

	Collection<Responsabilite> sesResponsabilites;
	private int num_en;
	Collection<Groupe> sesGroupes;

	public void accederAuxInfosPedagogiques() {
		// TODO - implement Enseignant.accederAuxInfosPedagogiques
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param num_e
	 * @param num_r
	 */
	public void ajouterResponsabilitesEnseignants(int num_e, int num_r) {
		// TODO - implement Enseignant.ajouterResponsabilitesEnseignants
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param num_e
	 * @param num_r
	 */
	public void retirerResponsabilitesEnseignants(int num_e, int num_r) {
		// TODO - implement Enseignant.retirerResponsabilitesEnseignants
		throw new UnsupportedOperationException();
	}

	public void definirContraintes() {
		// TODO - implement Enseignant.definirContraintes
		throw new UnsupportedOperationException();
	}

	public void definirRegleDistribution() {
		// TODO - implement Enseignant.definirRegleDistribution
		throw new UnsupportedOperationException();
	}

	// ============================================
	// Méthodes d'interaction avec l'API
	// ============================================

	/**
	 * Crée un enseignant via l'API
	 */
	public Map<String, Object> creerEnseignant(
			String login_e, String mdp_e, String nom_e, String prenom_e, String mail_e
	) {
		System.out.println("DEBUG ENSEIGNANT: creerEnseignant appelé avec login=" + login_e);
		try {
			Map<String, Object> resp = EnseignantApi.createEnseignant(login_e, mdp_e, nom_e, prenom_e, mail_e);
			System.out.println("DEBUG ENSEIGNANT: Réponse API: " + resp);
			return resp;
		} catch (Exception e) {
			System.err.println("Erreur création enseignant: " + e.getMessage());
			e.printStackTrace();
			Map<String, Object> err = new HashMap<>();
			err.put("success", false);
			err.put("error", e.getMessage());
			return err;
		}
	}

	 public static List<Map<String, Object>> getAllEnseignants() {
	        try {
	            return EnseignantApi.getAllEnseignants();
	        } catch (Exception e) {
	            System.err.println("Erreur récupération enseignants: " + e.getMessage());
	            e.printStackTrace();
	            List<Map<String, Object>> errorList = new ArrayList<>();
	            Map<String, Object> err = new HashMap<>();
	            err.put("success", false);
	            err.put("error", e.getMessage());
	            errorList.add(err);
	            return errorList;
	        }
	    }

	// Dans la classe Enseignant, modifie la méthode modifierEnseignant :

	// Dans la classe Enseignant, remplace les méthodes API :

	 /**
	  * Modifie un enseignant existant
	  */
	 public Map<String, Object> modifierEnseignant(
	         String login_e, String nom_e, String prenom_e, String mail_e, String mdp_e
	 ) {
	     System.out.println("DEBUG ENSEIGNANT: modifierEnseignant appelé avec login=" + login_e);
	     try {
	         Map<String, Object> resp = EnseignantApi.updateEnseignant(login_e, nom_e, prenom_e, mail_e, mdp_e);
	         System.out.println("DEBUG ENSEIGNANT: Réponse API: " + resp);
	         return resp;
	     } catch (Exception e) {
	         System.err.println("Erreur modification enseignant: " + e.getMessage());
	         e.printStackTrace();
	         Map<String, Object> err = new HashMap<>();
	         err.put("success", false);
	         err.put("error", e.getMessage());
	         return err;
	     }
	 }

	 // Version sans mdp
	 public Map<String, Object> modifierEnseignant(
	         String login_e, String nom_e, String prenom_e, String mail_e
	 ) {
	     return modifierEnseignant(login_e, nom_e, prenom_e, mail_e, null);
	 }

	

	/**
	 * Supprime un enseignant
	 */
	public Map<String, Object> supprimerEnseignant(String login_e) {
		System.out.println("DEBUG ENSEIGNANT: supprimerEnseignant appelé avec login=" + login_e);
		try {
			Map<String, Object> resp = EnseignantApi.deleteEnseignant(login_e);
			System.out.println("DEBUG ENSEIGNANT: Réponse API: " + resp);
			return resp;
		} catch (Exception e) {
			System.err.println("Erreur suppression enseignant: " + e.getMessage());
			e.printStackTrace();
			Map<String, Object> err = new HashMap<>();
			err.put("success", false);
			err.put("error", e.getMessage());
			return err;
		}
	}

	/**
	 * Version retournant la réponse brute de l'API pour affichage détaillé
	 */
	public Map<String, Object> ajouterEnseignantWithResult(
			String login_e,
			String mdp_e,
			String nom_e,
			String prenom_e,
			String mail_e
	) {
		System.out.println("DEBUG ENSEIGNANT: ajouterEnseignantWithResult appelé avec login=" + login_e);
		try {
			Map<String, Object> resp = EnseignantApi.createEnseignant(login_e, mdp_e, nom_e, prenom_e, mail_e);
			System.out.println("DEBUG ENSEIGNANT: Réponse API: " + resp);
			return resp;
		} catch (Exception e) {
			System.err.println("Erreur création enseignant: " + e.getMessage());
			e.printStackTrace();
			Map<String, Object> err = new HashMap<>();
			err.put("success", false);
			err.put("error", e.getMessage());
			return err;
		}
	}

}