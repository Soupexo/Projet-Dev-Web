package tdtp.modeles;


import java.util.*;

public class Regle {

	Collection<Groupe> sesGroupes;
	Critere sonCritere;
	private String nom_regle;
	private String objectif_regle;
	
	public Collection<Groupe> getSesGroupes() {
		return sesGroupes;
	}
	public void setSesGroupes(Collection<Groupe> sesGroupes) {
		this.sesGroupes = sesGroupes;
	}
	public Critere getSonCritere() {
		return sonCritere;
	}
	public void setSonCritere(Critere sonCritere) {
		this.sonCritere = sonCritere;
	}
	public String getNom_regle() {
		return nom_regle;
	}
	public void setNom_regle(String nom_regle) {
		this.nom_regle = nom_regle;
	}
	public String getObjectif_regle() {
		return objectif_regle;
	}
	public void setObjectif_regle(String objectif_regle) {
		this.objectif_regle = objectif_regle;
	}
	public int getNum_r() {
		return num_r;
	}
	public void setNum_r(int num_r) {
		this.num_r = num_r;
	}
	private int num_r;

}