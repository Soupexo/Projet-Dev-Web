package tdtp.modeles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class EtudiantApi {
	
	public static List<Etudiant> listeEtudiant;
    private static final Gson gson = ApiClient.gson();

    /**
     * Récupère toutes les notes (endpoint: GET /etudiant/notes)
     */
    public static List<Map<String, Object>> getNotes() throws IOException, InterruptedException {
        String resp = ApiClient.get("/etudiant/notes");
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        try {
            List<Map<String, Object>> list = gson.fromJson(resp, type);
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {

            return Collections.emptyList();
        }
    }

    /**
     * Crée un étudiant via POST /etudiant
     * body: { login, mdp, nom, prenom, mail, est_apprenti_e, type_bac_e, genre_e }
     */
    public static Map<String, Object> createStudent(String login, String mdp, String nom, String prenom, String mail, boolean estApprenti, String typeBac, String genre) throws IOException, InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("login", login);      
        payload.put("mdp", mdp);          
        payload.put("nom", nom);          
        payload.put("prenom", prenom);    
        payload.put("mail", mail);       
        payload.put("est_apprenti_e", estApprenti);
        if (typeBac != null) payload.put("type_bac_e", typeBac);
        if (genre != null) payload.put("genre_e", genre);

        String resp = ApiClient.post("/etudiant/", payload);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            Map<String, Object> map = gson.fromJson(resp, type);
            return map;
        } catch (com.google.gson.JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response from server");
            err.put("raw", resp);
            err.put("parseError", ex.getMessage());
            return err;
        }
    }

    /**
     * Met à jour un étudiant via PUT /etudiant/{login}
     */
    public static Map<String, Object> updateStudent(String login, Map<String, Object> data) throws IOException, InterruptedException {

        String resp = ApiClient.put("/etudiant/" + login, data);
        
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            Map<String, Object> map = gson.fromJson(resp, type);
            return map;
        } catch (com.google.gson.JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response from server");
            err.put("raw", resp);
            err.put("parseError", ex.getMessage());
            return err;
        }
    }

    /**
     * Supprime un étudiant via DELETE /etudiant/{login}
     */
    public static Map<String, Object> deleteStudent(String login) throws IOException, InterruptedException {
        String resp = ApiClient.delete("/etudiant/" + login);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            Map<String, Object> map = gson.fromJson(resp, type);
            return map;
        } catch (com.google.gson.JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response from server");
            err.put("raw", resp);
            err.put("parseError", ex.getMessage());
            return err;
        }
    }

    
    /**
     * Récupère les groupes de covoiturage (GET /etudiant/covoiturages)
     */
    public static List<Map<String, Object>> getGroupesCovoiturage() throws IOException, InterruptedException {
        String resp = ApiClient.get("/etudiant/covoiturages");
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        try {
            List<Map<String, Object>> list = gson.fromJson(resp, type);
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les infos complètes des étudiants (GET /etudiant)
     */
    public static List<Map<String, Object>> getInfosEtudiants() throws IOException, InterruptedException {
        String resp = ApiClient.get("/etudiant");
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        try {
            List<Map<String, Object>> list = gson.fromJson(resp, type);
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Ajoute une note via POST /etudiant/notes (utilise upsertNote)
     */
    public static Map<String, Object> addNote(Map<String, Object> noteData) throws IOException, InterruptedException {
        String resp = ApiClient.post("/etudiant/notes", noteData);
        
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            Map<String, Object> map = gson.fromJson(resp, type);
            return map;
        } catch (com.google.gson.JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response from server");
            err.put("raw", resp);
            err.put("parseError", ex.getMessage());
            return err;
        }
    }

    /**
     * Met à jour une note via PUT /etudiant/notes
     */
    public static Map<String, Object> updateNote(Map<String, Object> noteData) throws IOException, InterruptedException {
        String resp = ApiClient.put("/etudiant/notes", noteData);
        
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            Map<String, Object> map = gson.fromJson(resp, type);
            return map;
        } catch (com.google.gson.JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response from server");
            err.put("raw", resp);
            err.put("parseError", ex.getMessage());
            return err;
        }
    }

    /**
     * Supprime une note via DELETE /etudiant/notes
     */
    public static Map<String, Object> deleteNote(Map<String, Object> noteData) throws IOException, InterruptedException {
        
        // L'API DELETE attend les données dans le corps de la requête, pas dans l'URL
        String resp = ApiClient.delete("/etudiant/notes", noteData);
        
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            Map<String, Object> map = gson.fromJson(resp, type);
            return map;
        } catch (com.google.gson.JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response from server");
            err.put("raw", resp);
            err.put("parseError", ex.getMessage());
            return err;
        }
    }

    /**
     * Upsert (create/update) d'une note via POST /etudiant/notes
     */
    public static Map<String, Object> upsertNote(String etudiantLogin, String matiere, Double note, Double moyPrem, Double moyDeux, int coef) throws IOException, InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("etudiant", etudiantLogin);
        payload.put("matiere", matiere);
        if (note != null) payload.put("note", note);
        if (moyPrem != null) payload.put("moy_prem_semestre", moyPrem);
        if (moyDeux != null) payload.put("moy_deux_semestre", moyDeux);
        payload.put("coef", coef);

        String resp = ApiClient.post("/etudiant/notes", payload);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            Map<String, Object> map = gson.fromJson(resp, type);
            return map;
        } catch (com.google.gson.JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response from server");
            err.put("raw", resp);
            err.put("parseError", ex.getMessage());
            return err;
        }
    }

    /**
     * Supprime une note via DELETE /etudiant/notes (body: etudiant, matiere)
     */
    public static Map<String, Object> deleteNote(String etudiantLogin, String matiere) throws IOException, InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("etudiant", etudiantLogin);
        payload.put("matiere", matiere);
        String json = gson.toJson(payload);
        // HttpClient DELETE with body is not directly supported in Java 11; use a custom request
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(ApiClient.BASE_URL + "/etudiant/notes"))
                .method("DELETE", java.net.http.HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        java.net.http.HttpResponse<String> response = java.net.http.HttpClient.newBuilder().build().send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        String resp = response.body();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            Map<String, Object> map = gson.fromJson(resp, type);
            return map;
        } catch (com.google.gson.JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response from server");
            err.put("raw", resp);
            err.put("parseError", ex.getMessage());
            return err;
        }
    }
    
    /**
     * Récupère le login d'un étudiant à partir de son nom et prénom
     * @param nom Le nom de l'étudiant
     * @param prenom Le prénom de l'étudiant
     * @return Le login de l'étudiant ou null si non trouvé
     */
    public static String getLoginByNomPrenom(String nom, String prenom) throws IOException, InterruptedException {
        List<Map<String, Object>> etudiants = getInfosEtudiants();
       
        
        for (Map<String, Object> etudiant : etudiants) {
            // Essayer plusieurs clés possibles
            String nomEtu = String.valueOf(etudiant.getOrDefault("nom_u", 
                            etudiant.getOrDefault("nom", 
                            etudiant.getOrDefault("nom_e", ""))));
            String prenomEtu = String.valueOf(etudiant.getOrDefault("prenom_u", 
                               etudiant.getOrDefault("prenom", 
                               etudiant.getOrDefault("prenom_e", ""))));
            String loginEtu = String.valueOf(etudiant.getOrDefault("login_u", 
                              etudiant.getOrDefault("login", "")));
            
            
            // Comparaison insensible à la casse et aux espaces
            if (nom.trim().equalsIgnoreCase(nomEtu.trim()) && 
                prenom.trim().equalsIgnoreCase(prenomEtu.trim())) {
                System.out.println("  -> TROUVÉ! Login: " + loginEtu);
                return loginEtu;
            }
        }
        
        return null; // Étudiant non trouvé
    }
}
