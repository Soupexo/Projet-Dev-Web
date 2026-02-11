package tdtp.modeles;

public class Utilisateur {
	private String mdp_u;
	private String nom_u;
	private String login_u;
	private String prenom_u;
	private String mail_u;
	
	public Utilisateur() {}
	
	public Utilisateur(String login_u, String nom_u, String prenom_u, String mail_u) {
		this.login_u = login_u;
		this.nom_u = nom_u;
		this.prenom_u = prenom_u;
		this.mail_u = mail_u;
	}
}