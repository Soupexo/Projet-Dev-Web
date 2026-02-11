package tdtp.modeles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class PromotionApi {
	
	public static List<Promotion> listePromotion;
    private static final Gson gson = ApiClient.gson();

    public static List<Map<String, Object>> getPromotions() throws IOException, InterruptedException {
        String resp = ApiClient.get("/promotion");
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        try {
            List<Map<String, Object>> list = gson.fromJson(resp, type);
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static Map<String, Object> createPromotion(Map<String, Object> payload) throws IOException, InterruptedException {
        String resp = ApiClient.post("/promotion/", payload);
        System.out.println("DEBUG PROMOTION API: réponse brute=" + resp);
        
        try {
            Type objectType = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> map = gson.fromJson(resp, objectType);
            
            if (map != null && Boolean.TRUE.equals(map.get("success"))) {
                return map;
            }
            Type arrayType = new TypeToken<List<Map<String, Object>>>(){}.getType();
            List<Map<String, Object>> list = gson.fromJson(resp, arrayType);
            
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
            
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid response format from server");
            err.put("raw", resp);
            return err;
            
        } catch (com.google.gson.JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response from server");
            err.put("raw", resp);
            err.put("parseError", ex.getMessage());
            return err;
        }
    }

    public static Map<String, Object> updatePromotion(int id, Map<String, Object> data) throws IOException, InterruptedException {
        String resp = ApiClient.put("/promotion/" + id, data);
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

    public static Map<String, Object> deletePromotion(int id) throws IOException, InterruptedException {
        ApiClient.HttpResult r = ApiClient.deleteWithStatus("/promotion/" + id);
        String body = r.getBody();
        int status = r.getStatusCode();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();

        if (status >= 400) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("status", status);
            if (body != null && body.contains("File not found")) {
                err.put("error", "Promotion introuvable sur le serveur (id=" + id + ")");
            } else {
                err.put("error", "Erreur HTTP " + status + " lors de la suppression");
            }
            err.put("raw", body);
            return err;
        }

        try {
            Map<String, Object> map = gson.fromJson(body, type);
            return map;
        } catch (com.google.gson.JsonSyntaxException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Réponse JSON invalide du serveur");
            err.put("raw", body);
            err.put("parseError", ex.getMessage());
            return err;
        }
    }
}