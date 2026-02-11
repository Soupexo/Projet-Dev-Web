package tdtp.modeles;

import java.util.*;

public class Groupe {
	Promotion saPromotion;
	Collection<Regle> sesRegles;
	Collection<Contrainte> sesContraintes;
	private int num_g;
	private String nom_g;
	private int nombre_etudiant_max_g;
	private boolean est_finalise_g;
	Collection<Etudiant> sesEtudiants;
	Collection<Enseignant> sesEnseignants;
	private int nombre_etudiant_g;
	
	public Groupe() {
		
	}
	
	public Groupe(String nom_g, int nombre_etudiant_max_g, boolean est_finalise_g) {
		this.nom_g = nom_g;
		this.nombre_etudiant_max_g = nombre_etudiant_max_g;
		this.est_finalise_g = est_finalise_g;
		this.sesEtudiants = sesEtudiants;
		this.sesEnseignants = sesEnseignants;
	}

	/**
	 * Crée un groupe via l'API (POST /groupe)
	 */
	public Map<String, Object> creerGroupeManuellement(String nom_g, int num_p, Integer nb_etudiants_max, Boolean est_finalise) {
		try {
			Map<String, Object> resp = GroupeApi.createGroup(nom_g, num_p, nb_etudiants_max, est_finalise);
			Groupe g = new Groupe(nom_g, nombre_etudiant_max_g, est_finalise_g);
			GroupeApi.listeGroupe.add(g);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> err = new HashMap<>();
			err.put("success", false);
			err.put("error", e.getMessage());
			return err;
		}
	}

	

	public Map<String, Object> updateGroupe(int id, Map<String, Object> data) {
		try {
			return GroupeApi.updateGroup(id, data);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> err = new HashMap<>();
			err.put("success", false);
			err.put("error", e.getMessage());
			return err;
		}
	}

	public Map<String, Object> deleteGroupe(int id) {
		try {
			return GroupeApi.deleteGroup(id);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> err = new HashMap<>();
			err.put("success", false);
			err.put("error", e.getMessage());
			return err;
		}
	}
	/**
	 * Algorithme de création automatique (peut appeler l'API ou utiliser une logique locale)
	 */
	public void creerGroupeAutomatiquement(String nom_g, int nb_etudiants_max) {
		// Comportement par défaut : déléguer la création au serveur si une promotion est associée
		throw new UnsupportedOperationException("création automatique non implémentée");
	}

	/**
	 * Récupère la liste des groupes (GET /groupe)
	 */
	public static List<Map<String, Object>> getListeGroupes() {
		try {
			return GroupeApi.getListeGroupes();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	/**
	 * Récupère les groupes non finalisés (GET /groupe/non-finalises)
	 */
	public static List<Map<String, Object>> getGroupesNonFinalises() {
		try {
			return GroupeApi.getGroupesNonFinalises();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public Promotion getSaPromotion() {
		return saPromotion;
	}

	public void setSaPromotion(Promotion saPromotion) {
		this.saPromotion = saPromotion;
	}

	public Collection<Regle> getSesRegles() {
		return sesRegles;
	}

	public void setSesRegles(Collection<Regle> sesRegles) {
		this.sesRegles = sesRegles;
	}

	public Collection<Contrainte> getSesContraintes() {
		return sesContraintes;
	}

	public void setSesContraintes(Collection<Contrainte> sesContraintes) {
		this.sesContraintes = sesContraintes;
	}

	public int getNum_g() {
		return num_g;
	}

	public void setNum_g(int num_g) {
		this.num_g = num_g;
	}

	public String getNom_g() {
		return nom_g;
	}

	public void setNom_g(String nom_g) {
		this.nom_g = nom_g;
	}

	public int getNombre_etudiant_max_g() {
		return nombre_etudiant_max_g;
	}

	public void setNombre_etudiant_max_g(int nombre_etudiant_max_g) {
		this.nombre_etudiant_max_g = nombre_etudiant_max_g;
	}

	public boolean isEst_finalise_g() {
		return est_finalise_g;
	}

	public void setEst_finalise_g(boolean est_finalise_g) {
		this.est_finalise_g = est_finalise_g;
	}

	public Collection<Etudiant> getSesEtudiants() {
		return sesEtudiants;
	}

	public void setSesEtudiants(Collection<Etudiant> sesEtudiants) {
		this.sesEtudiants = sesEtudiants;
	}

	public Collection<Enseignant> getSesEnseignants() {
		return sesEnseignants;
	}

	public void setSesEnseignants(Collection<Enseignant> sesEnseignants) {
		this.sesEnseignants = sesEnseignants;
	}

	public int getNombre_etudiant_g() {
		return nombre_etudiant_g;
	}

	public void setNombre_etudiant_g(int nombre_etudiant_g) {
		this.nombre_etudiant_g = nombre_etudiant_g;
	}
	
	public Groupe(String n, int nb) {
		this.nom_g = n;
		sesEtudiants = new ArrayList<Etudiant>();
		nombre_etudiant_max_g = nb;
		this.est_finalise_g = false;
		nombre_etudiant_g = 0;
	}
	
	public void ajoutEtudiant(Etudiant etu) {
		if (nombre_etudiant_g + 1 <= nombre_etudiant_max_g) {
			this.sesEtudiants.add(etu);
			nombre_etudiant_g+=1;
		}
	}
	
	public void retireEtudiant(Etudiant etu) {
		if (sesEtudiants.contains(etu)) {
			this.sesEtudiants.remove(etu);
			nombre_etudiant_g-=1;
		}
	}
	
	
	public void setEstComplet(boolean arg) {
		this.est_finalise_g = arg;
	}

}