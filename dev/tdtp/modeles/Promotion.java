package tdtp.modeles;

import java.util.*;

public class Promotion {

	Annee sonAnneePromo;
	Collection<Groupe> listeGroupes;
	private int num_p;
	private int nombre_groupe_max_p;
	private int nombre_groupe_covoiturage_max_p;
	private int nombre_etudiant_max_p;
	private int nombre_etudiant_actuel_p;
	private int nombre_groupe_actuel_p;
	
	public final static double ratioFemme = 0.2;
	public final static double ratioHomme = 0.8;
	public final static double ratioApprenti = 0.1;
	public final static double ratioInitial = 0.9;
	

	public int getNbEtudiants() {
		// TODO - implement Promotion.getNbEtudiants
		throw new UnsupportedOperationException();
	}

	public String afficher() {
		// TODO - implement Promotion.afficher
		throw new UnsupportedOperationException();
	}

	public Promotion(int nombre_groupe_max_p, int nombre_groupe_covoiturage_max_p, int nombre_etudiant_max_p,
			int nombre_etudiant_actuel_p, int nombre_groupe_actuel_p) {
		super();
		this.nombre_groupe_max_p = nombre_groupe_max_p;
		this.nombre_groupe_covoiturage_max_p = nombre_groupe_covoiturage_max_p;
		this.nombre_etudiant_max_p = nombre_etudiant_max_p;
		this.nombre_etudiant_actuel_p = nombre_etudiant_actuel_p;
		this.nombre_groupe_actuel_p = nombre_groupe_actuel_p;
	}


	public void exporterFichierPromotion() {
		// TODO - implement Promotion.exporterFichierPromotion
		throw new UnsupportedOperationException();
	}

	public void exporterDonneesEtudiantes() {
		// TODO - implement Promotion.exporterDonneesEtudiantes
		throw new UnsupportedOperationException();
	}

	public Annee getSonAnneePromo() {
		return sonAnneePromo;
	}

	public void setSonAnneePromo(Annee sonAnneePromo) {
		this.sonAnneePromo = sonAnneePromo;
	}

	public Collection<Groupe> getListeGroupes() {
		return listeGroupes;
	}

	public void setListeGroupes(Collection<Groupe> listeGroupes) {
		this.listeGroupes = listeGroupes;
	}

	public int getNum_p() {
		return num_p;
	}

	public void setNum_p(int num_p) {
		this.num_p = num_p;
	}

	public int getNombre_groupe_max_p() {
		return nombre_groupe_max_p;
	}

	public void setNombre_groupe_max_p(int nombre_groupe_max_p) {
		this.nombre_groupe_max_p = nombre_groupe_max_p;
	}

	public int getNombre_groupe_covoiturage_max_p() {
		return nombre_groupe_covoiturage_max_p;
	}

	public void setNombre_groupe_covoiturage_max_p(int nombre_groupe_covoiturage_max_p) {
		this.nombre_groupe_covoiturage_max_p = nombre_groupe_covoiturage_max_p;
	}

	public int getNombre_etudiant_max_p() {
		return nombre_etudiant_max_p;
	}

	public void setNombre_etudiant_max_p(int nombre_etudiant_max_p) {
		this.nombre_etudiant_max_p = nombre_etudiant_max_p;
	}

	public int getNombre_etudiant_actuel_p() {
		return nombre_etudiant_actuel_p;
	}

	public void setNombre_etudiant_actuel_p(int nombre_etudiant_actuel_p) {
		this.nombre_etudiant_actuel_p = nombre_etudiant_actuel_p;
	}

	public int getNombre_groupe_actuel_p() {
		return nombre_groupe_actuel_p;
	}

	public void setNombre_groupe_actuel_p(int nombre_groupe_actuel_p) {
		this.nombre_groupe_actuel_p = nombre_groupe_actuel_p;
	}

}