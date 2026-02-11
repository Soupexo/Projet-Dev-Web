package tdtp.modeles;

import java.util.*;

public class Sondage {

    private int id_s;
    private String nom_s;
    private String type_s; // exemple: "QCM", "Texte libre"
    private boolean reponses_multiples_s;
    private String delai_s; // date limite ou délai en string
    private Collection<ReponseSondage> sesReponses;

    public Sondage() {
        this.sesReponses = new ArrayList<>();
    }

    public Sondage(String nom_s, String type_s, boolean reponses_multiples_s, String delai_s) {
        this.nom_s = nom_s;
        this.type_s = type_s;
        this.reponses_multiples_s = reponses_multiples_s;
        this.delai_s = delai_s;
        this.sesReponses = new ArrayList<>();
    }

    /**
     * Crée un sondage via l'API
     */
    public boolean ajouterSondage() {
        try {
            Map<String, Object> resp = tdtp.modeles.SondageApi.createSondage(nom_s, type_s, reponses_multiples_s, delai_s);
            if (resp != null && Boolean.TRUE.equals(resp.get("success"))) {
                Object idObj = resp.get("id_s");
                if (idObj instanceof Number) {
                    this.id_s = ((Number) idObj).intValue();
                }
                return true;
            } else {
                System.err.println("createSondage response: " + resp);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Version retournant la réponse brute de l'API
     */
    public Map<String, Object> ajouterSondageWithResult() {
        try {
            Map<String, Object> resp = tdtp.modeles.SondageApi.createSondage(nom_s, type_s, reponses_multiples_s, delai_s);
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
     * Met à jour le sondage via l'API
     */
    public boolean modifierSondage() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("nom_s", nom_s);
            data.put("type_s", type_s);
            data.put("reponses_multiples_s", reponses_multiples_s);
            data.put("delai_s", delai_s);

            Map<String, Object> resp = tdtp.modeles.SondageApi.updateSondage(id_s, data);
            if (resp != null && (Boolean.TRUE.equals(resp.get("success")) || resp.get("updated") != null)) {
                return true;
            } else {
                System.err.println("modifierSondage response: " + resp);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime le sondage via l'API
     */
    public boolean supprimerSondage() {
        try {
            Map<String, Object> resp = tdtp.modeles.SondageApi.deleteSondage(String.valueOf(id_s));
            return resp != null && Boolean.TRUE.equals(resp.get("success"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ajoute une réponse à ce sondage
     */
    public boolean ajouterReponse(String loginEtudiant, String reponse) {
        ReponseSondage r = new ReponseSondage(loginEtudiant, this, reponse);
        sesReponses.add(r);
        try {
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getId_s() {
        return id_s;
    }

    public void setId_s(int id_s) {
        this.id_s = id_s;
    }

    public String getNom_s() {
        return nom_s;
    }

    public void setNom_s(String nom_s) {
        this.nom_s = nom_s;
    }

    public String getType_s() {
        return type_s;
    }

    public void setType_s(String type_s) {
        this.type_s = type_s;
    }

    public boolean isReponses_multiples_s() {
        return reponses_multiples_s;
    }

    public void setReponses_multiples_s(boolean reponses_multiples_s) {
        this.reponses_multiples_s = reponses_multiples_s;
    }

    public String getDelai_s() {
        return delai_s;
    }

    public void setDelai_s(String delai_s) {
        this.delai_s = delai_s;
    }

    public Collection<ReponseSondage> getSesReponses() {
        return sesReponses;
    }

    public void setSesReponses(Collection<ReponseSondage> sesReponses) {
        this.sesReponses = sesReponses;
    }
}
