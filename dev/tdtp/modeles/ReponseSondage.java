package tdtp.modeles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReponseSondage {

    private String loginEtudiant;      // login de l'étudiant qui répond
    private Sondage sondage;           // sondage concerné
    private String reponse;            // réponse donnée
    private String question;           // optionnel, question spécifique
    private String dateReponse;        // date/heure de la réponse

    // Constructeur principal utilisé par Sondage.java
    public ReponseSondage(String loginEtudiant, Sondage sondage, String reponse) {
        this.loginEtudiant = loginEtudiant;
        this.sondage = sondage;
        this.reponse = reponse;
        this.dateReponse = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters / Setters

    public String getLoginEtudiant() {
        return loginEtudiant;
    }

    public void setLoginEtudiant(String loginEtudiant) {
        this.loginEtudiant = loginEtudiant;
    }

    public Sondage getSondage() {
        return sondage;
    }

    public void setSondage(Sondage sondage) {
        this.sondage = sondage;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(String dateReponse) {
        this.dateReponse = dateReponse;
    }

    @Override
    public String toString() {
        return "ReponseSondage{" +
                "loginEtudiant='" + loginEtudiant + '\'' +
                ", sondage=" + (sondage != null ? sondage.getNom_s() : "null") +
                ", reponse='" + reponse + '\'' +
                ", dateReponse='" + dateReponse + '\'' +
                '}';
    }
}
