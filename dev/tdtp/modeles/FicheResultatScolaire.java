package tdtp.modeles;

import java.util.*;

public class FicheResultatScolaire {

	Etudiant sonEtudiant;
	Collection<Note> sesNotes;
	Annee sonAnnee;
	private float[] moyennes_premier_semestre;
	private float[] moyennes_deuxieme_semestre;
	private float moyennes_annee;
	
	public Etudiant getSonEtudiant() {
		return sonEtudiant;
	}

	public void setSonEtudiant(Etudiant sonEtudiant) {
		this.sonEtudiant = sonEtudiant;
	}

	public Collection<Note> getSesNotes() {
		return sesNotes;
	}

	public void setSesNotes(Collection<Note> sesNotes) {
		this.sesNotes = sesNotes;
	}

	public Annee getSonAnnee() {
		return sonAnnee;
	}

	public void setSonAnnee(Annee sonAnnee) {
		this.sonAnnee = sonAnnee;
	}

	public float[] getMoyennes_premier_semestre() {
		return moyennes_premier_semestre;
	}

	public void setMoyennes_premier_semestre(float[] moyennes_premier_semestre) {
		this.moyennes_premier_semestre = moyennes_premier_semestre;
	}

	public float[] getMoyennes_deuxieme_semestre() {
		return moyennes_deuxieme_semestre;
	}

	public void setMoyennes_deuxieme_semestre(float[] moyennes_deuxieme_semestre) {
		this.moyennes_deuxieme_semestre = moyennes_deuxieme_semestre;
	}

	public float getMoyennes_annee() {
		return moyennes_annee;
	}

	public void setMoyennes_annee(float moyennes_annee) {
		this.moyennes_annee = moyennes_annee;
	}

	public FicheResultatScolaire(float[] moyennes_premier_semestre, float[] moyennes_deuxieme_semestre,
			float moyennes_annee) {
		super();
		this.moyennes_premier_semestre = moyennes_premier_semestre;
		this.moyennes_deuxieme_semestre = moyennes_deuxieme_semestre;
		this.moyennes_annee = moyennes_annee;
	}

}