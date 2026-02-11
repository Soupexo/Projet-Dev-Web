package tdtp.modeles;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class UtilisateurApi {

    private static final String BASE_URL = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/utilisateur/";
    private static final Gson gson = new Gson();

    /**
     * Authentifie un utilisateur via l'API PHP
     */
    public static Map<String, Object> authenticateUser(String login, String password) throws IOException, InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("login", login);
        payload.put("mdp", password);

        String jsonPayload = gson.toJson(payload);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "auth"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("DEBUG AUTH: Status code: " + response.statusCode());
        System.out.println("DEBUG AUTH: Response: " + response.body());

        if (response.statusCode() == 200) {
            try {
                Map<String, Object> map = gson.fromJson(response.body(), Map.class);
                
                if ((Boolean) map.get("success")) {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("success", true);
                    userInfo.put("login", login);
                    userInfo.put("nom", map.get("nom"));
                    userInfo.put("prenom", map.get("prenom"));
                    userInfo.put("mail", map.get("mail"));
                    userInfo.put("role", determineRole(login)); 
                    return userInfo;
                } else {
                    Map<String, Object> err = new HashMap<>();
                    err.put("success", false);
                    err.put("error", map.get("error"));
                    return err;
                }
            } catch (JsonSyntaxException ex) {
                Map<String, Object> err = new HashMap<>();
                err.put("success", false);
                err.put("error", "Invalid JSON response from server");
                err.put("raw_response", response.body());
                return err;
            }
        } else {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "HTTP " + response.statusCode() + ": " + response.body());
            return err;
        }
    }
    
    /**
     * Détermine le rôle d'un utilisateur en appelant les fonctions SQL appropriées
     */
    private static String determineRole(String login) {
        try {
            Map<String, Object> etudiantCheck = callRoleFunction("estEtudiant", login);
            if ((Boolean) etudiantCheck.get("success") && (Boolean) etudiantCheck.get("result")) {
                return "etudiant";
            }
            
            Map<String, Object> respFormCheck = callRoleFunction("estResponsableFormation", login);
            if ((Boolean) respFormCheck.get("success") && (Boolean) respFormCheck.get("result")) {
                return "responsable_formation";
            }
            
            Map<String, Object> enseignantCheck = callRoleFunction("estEnseignant", login);
            if ((Boolean) enseignantCheck.get("success") && (Boolean) enseignantCheck.get("result")) {
                return "enseignant";
            }
            
            return "administrateur";
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la détermination du rôle: " + e.getMessage());
            return "inconnu";
        }
    }
    
    /**
     * Appelle une fonction SQL de rôle via l'API
     */
    private static Map<String, Object> callRoleFunction(String functionName, String login) throws IOException, InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("function", functionName);
        payload.put("login", login);

        String jsonPayload = gson.toJson(payload);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "checkRole"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            try {
                return gson.fromJson(response.body(), Map.class);
            } catch (JsonSyntaxException ex) {
                Map<String, Object> err = new HashMap<>();
                err.put("success", false);
                err.put("error", "Invalid JSON response from server");
                return err;
            }
        } else {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "HTTP " + response.statusCode() + ": " + response.body());
            return err;
        }
    }
}
