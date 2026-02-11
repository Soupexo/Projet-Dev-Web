package tdtp.modeles;


import java.util.*;

public class Etudiant extends Utilisateur {

	Responsabilite saResponsabilite;
	Collection<FicheResultatScolaire> sesFichesResultats;
	Collection<ReponseSondage> sesReponsesSondage;
	private int num_e;
	private boolean est_apprenti;
	private String type_e_bac;
	private String genre_e;
	Collection<Annee> sesAnneeDetudes;
	Groupe sonGroupe;
	
	public Etudiant() {
		
	}
	
	public Etudiant(int num_e, boolean est_apprenti, String type_e_bac, String genre_e, Groupe sonGroupe) {
		this.num_e = num_e;
		this.est_apprenti = est_apprenti;
		this.type_e_bac = type_e_bac;
		this.genre_e = genre_e;
		this.sonGroupe = sonGroupe;
	}

	/**
	 * 
	 * @param nom_e
	 * @param login_e
	 * @param prenom_e
	 * @param mail_e
	 * @param est_apprenti
	 * @param type_e_bac
	 * @param genre_e
	 */
public boolean ajouterEtudiant(String nom_e, String login_e, String prenom_e, String mail_e, boolean est_apprenti, String type_e_bac, String genre_e) {
        // version historique : utilise un mot de passe par défaut
        return ajouterEtudiant(nom_e, login_e, "password", prenom_e, mail_e, est_apprenti, type_e_bac, genre_e);
    }

    /**
     * Crée un étudiant en précisant le mot de passe
     */
    public boolean ajouterEtudiant(String nom_e, String login_e, String mdp_e, String prenom_e, String mail_e, boolean est_apprenti, String type_e_bac, String genre_e) {
        try {
            Map<String, Object> resp = tdtp.modeles.EtudiantApi.createStudent(login_e, mdp_e, nom_e, prenom_e, mail_e, est_apprenti, type_e_bac, genre_e);
            if (resp != null && Boolean.TRUE.equals(resp.get("success"))) {
                return true;
            } else {
                System.err.println("createStudent response: " + resp);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Version retournant la réponse brute de l'API pour affichage détaillé
     */
    public Map<String, Object> ajouterEtudiantWithResult(
    	    String login_e,
    	    String mdp_e,
    	    String nom_e,
    	    String prenom_e,
    	    String mail_e,
    	    boolean est_apprenti,
    	    String type_e_bac,
    	    String genre_e
    	){
        System.out.println("DEBUG ETUDIANT: ajouterEtudiantWithResult appelé avec login=" + login_e);
        try {
            Map<String, Object> resp = tdtp.modeles.EtudiantApi.createStudent(login_e, mdp_e, nom_e, prenom_e, mail_e, est_apprenti, type_e_bac, genre_e);
            return resp != null ? resp : Collections.singletonMap("success", false);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", e.getMessage());
            return err;
        }
    }

	/**
	 * 
	 * @param num_e

    /**
     * 
     * @param login_e
     * @param mdp_e
     * @param nom_e
     * @param prenom_e
     * @param mail_e
     * @param est_apprenti
     * @param type_e_bac
     * @param genre_e
     */
    public boolean modifierEtudiant(String login_e, String mdp_e, String nom_e, String prenom_e, String mail_e, boolean est_apprenti, String type_e_bac, String genre_e) {
        // Met à jour l'étudiant via l'API (utilise le login pour l'URL)
        System.out.println("DEBUG MODIFIER: login_e='" + login_e + "', mdp_e=" + (mdp_e != null ? "'" + mdp_e + "'" : "null"));
        System.out.println("DEBUG MODIFIER: nom_e=" + nom_e + " (class=" + (nom_e != null ? "null" : nom_e.getClass().getSimpleName()) + ")");

        try {
            Map<String, Object> data = new HashMap<>();
            if (mdp_e != null) data.put("mdp", mdp_e);
            if (nom_e != null) data.put("nom_u", nom_e);
            if (prenom_e != null) data.put("prenom_u", prenom_e);
            if (mail_e != null) data.put("mail_u", mail_e);
            data.put("est_apprenti_e", est_apprenti);
            if (type_e_bac != null) data.put("type_bac_e", type_e_bac);
            if (genre_e != null) data.put("genre_e", genre_e);

            System.out.println("DEBUG MODIFIER: data envoyée=" + data);

            Map<String, Object> resp = tdtp.modeles.EtudiantApi.updateStudent(login_e, data);
            System.out.println("DEBUG MODIFIER: réponse API=" + resp);

            if (resp != null && (Boolean.TRUE.equals(resp.get("success")) || resp.get("updated") != null)) {
                System.out.println("DEBUG MODIFIER: SUCCÈS");
                return true;
            } else {
                System.out.println("DEBUG MODIFIER: ÉCHEC - réponse invalide");
                return false;
            }
        } catch (Exception e) {
            System.out.println("DEBUG MODIFIER: EXCEPTION - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 
     * @param sondage
     * @param reponse
     */
	public void repondreSondage(Sondage sondage, String reponse) {
        // Pour l'instant, pas d'endpoint public dédié aux réponses individuels via l'API exposée.
        // Implémentation potentielle : POST /sondage/reponse (non présent actuellement)
        throw new UnsupportedOperationException("Endpoint de réponse sondage non implémenté côté API");
    }

	/**
	 * 
	 * @param num_e
	 */
public void retirerEtudiant(int num_e) {
		// TODO - implement Etudiant.retirerEtudiant
		throw new UnsupportedOperationException();
	}

public boolean choisirColleguesCovoiturage(int num_e) {
        // Interaction covoiturage: récupérer la liste des groupes de covoiturage via l'API
        try {
            List<Map<String, Object>> groupes = tdtp.modeles.EtudiantApi.getGroupesCovoiturage();
            // Logique de choix non implémentée (UI devra proposer un formulaire pour choisir)
            // Pour l'instant on retourne true si on a reçu une liste
            return groupes != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}

public List<Map<String, Object>> consulterNotes() {
        try {
            return tdtp.modeles.EtudiantApi.getNotes();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> consulterListeGroupes() {
        try {
            return tdtp.modeles.EtudiantApi.getGroupesCovoiturage();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
	}

	public Responsabilite getSaResponsabilite() {
		return saResponsabilite;
	}

	public void setSaResponsabilite(Responsabilite saResponsabilite) {
		this.saResponsabilite = saResponsabilite;
	}

	public Collection<FicheResultatScolaire> getSesFichesResultats() {
		return sesFichesResultats;
	}

	public void setSesFichesResultats(Collection<FicheResultatScolaire> sesFichesResultats) {
		this.sesFichesResultats = sesFichesResultats;
	}

	public Collection<ReponseSondage> getSesReponsesSondage() {
		return sesReponsesSondage;
	}

	public void setSesReponsesSondage(Collection<ReponseSondage> sesReponsesSondage) {
		this.sesReponsesSondage = sesReponsesSondage;
	}

	public int getNum_e() {
		return num_e;
	}

	public void setNum_e(int num_e) {
		this.num_e = num_e;
	}

	public boolean getEst_apprenti() {
		return est_apprenti;
	}

	public void setEst_apprenti(boolean est_apprenti) {
		this.est_apprenti = est_apprenti;
	}

	public String getType_e_bac() {
		return type_e_bac;
	}

	public void setType_e_bac(String type_e_bac) {
		this.type_e_bac = type_e_bac;
	}

	public String getGenre_e() {
		return genre_e;
	}

	public void setGenre_e(String genre_e) {
		this.genre_e = genre_e;
	}

	public Collection<Annee> getSesAnneeDetudes() {
		return sesAnneeDetudes;
	}

	public void setSesAnneeDetudes(Collection<Annee> sesAnneeDetudes) {
		this.sesAnneeDetudes = sesAnneeDetudes;
	}

	public Groupe getSonGroupe() {
		return sonGroupe;
	}

	public void setSonGroupe(Groupe sonGroupe) {
		this.sonGroupe = sonGroupe;
	}

	

}