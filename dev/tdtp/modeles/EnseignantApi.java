package tdtp.modeles;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class EnseignantApi {
    private static final Gson gson = new Gson();
    
    public static Map<String, Object> createEnseignant(
            String login, String mdp, String nom, String prenom, String mail
    ) throws IOException, InterruptedException {
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("login", login);
        payload.put("mdp", mdp);
        payload.put("nom", nom);
        payload.put("prenom", prenom);
        payload.put("email", mail);  // CORRECTION: "email" pas "mail"

        String resp = ApiClient.post("/enseignant", payload);
        
        // Nettoyer aussi pour la création
        resp = nettoyerReponsePHP(resp);
        
        try {
            return gson.fromJson(resp, Map.class);
        } catch (JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response");
            err.put("raw", resp);
            return err;
        }
    }
    
    // GET ALL
    public static List<Map<String, Object>> getAllEnseignants() throws IOException, InterruptedException {
        String resp = ApiClient.get("/enseignant");
        
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        try {
            List<Map<String, Object>> list = gson.fromJson(resp, type);
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
    
    public static Map<String, Object> updateEnseignant(
            String login, String nom, String prenom, String mail, String mdp
    ) throws IOException, InterruptedException {
        
        Map<String, Object> data = new HashMap<>();
        data.put("nom", nom);
        data.put("prenom", prenom);
        data.put("email", mail);  // CORRECTION: "email" pas "mail"
        
        // Ajouter mdp seulement si fourni
        if (mdp != null && !mdp.trim().isEmpty()) {
            data.put("mdp", mdp.trim());
        }
        
        String resp = ApiClient.put("/enseignant/" + login, data);
        
        // Nettoyer la réponse du warning PHP
        resp = nettoyerReponsePHP(resp);
        
        try {
            return gson.fromJson(resp, Map.class);
        } catch (JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response");
            err.put("raw", resp);
            return err;
        }
    }

    // Méthode pour nettoyer les warnings PHP du JSON
    private static String nettoyerReponsePHP(String resp) {
        if (resp == null) return null;
        
        // Supprimer les warnings PHP avant le JSON
        int jsonStart = resp.indexOf("{");
        int jsonEnd = resp.lastIndexOf("}");
        
        if (jsonStart >= 0 && jsonEnd > jsonStart) {
            return resp.substring(jsonStart, jsonEnd + 1);
        }
        
        return resp;
    }
    
    // Version sans mdp pour compatibilité
    public static Map<String, Object> updateEnseignant(
            String login, String nom, String prenom, String mail
    ) throws IOException, InterruptedException {
        return updateEnseignant(login, nom, prenom, mail, null);
    }
    
    // SUPPRESSION
    public static Map<String, Object> deleteEnseignant(String login) throws IOException, InterruptedException {
        
        ApiClient.HttpResult result = ApiClient.deleteWithStatus("/enseignant/" + login);
        
        try {
            if (result.getStatusCode() == 200 || result.getStatusCode() == 204) {
                Map<String, Object> success = new HashMap<>();
                success.put("success", true);
                success.put("message", "Enseignant supprimé");
                return success;
            } else {
                // Essayer de parser l'erreur
                Map<String, Object> error = gson.fromJson(result.getBody(), Map.class);
                if (error == null) {
                    error = new HashMap<>();
                    error.put("success", false);
                    error.put("error", "HTTP " + result.getStatusCode() + ": " + result.getBody());
                }
                return error;
            }
        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Erreur: " + e.getMessage());
            err.put("raw", result.getBody());
            return err;
        }
    }
}