package tdtp.modeles;
import java.util.*;

public class Permission {

	Collection<Responsabilite> listeResponsabilites;
	private int num_pe;
	private String intitule_pe;
	
	public Collection<Responsabilite> getListeResponsabilites() {
		return listeResponsabilites;
	}
	public void setListeResponsabilites(Collection<Responsabilite> listeResponsabilites) {
		this.listeResponsabilites = listeResponsabilites;
	}
	public int getNum_pe() {
		return num_pe;
	}
	public void setNum_pe(int num_pe) {
		this.num_pe = num_pe;
	}
	public String getIntitule_pe() {
		return intitule_pe;
	}
	public void setIntitule_pe(String intitule_pe) {
		this.intitule_pe = intitule_pe;
	}

}