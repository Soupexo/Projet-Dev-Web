package tdtp.modeles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class GroupeApi {
	
	public static List<Groupe >listeGroupe;
    private static final Gson gson = ApiClient.gson();

    public static List<Map<String, Object>> getListeGroupes() throws IOException, InterruptedException {
        String resp = ApiClient.get("/groupe");
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        try {
            List<Map<String, Object>> list = gson.fromJson(resp, type);
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static List<Map<String, Object>> getGroupesNonFinalises() throws IOException, InterruptedException {
        String resp = ApiClient.get("/groupe/non-finalises");
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        try {
            List<Map<String, Object>> list = gson.fromJson(resp, type);
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static Map<String, Object> createGroup(String nom_g, int num_p, Integer nombre_etudiant_max_g, Boolean est_finalise_g) throws IOException, InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("nom_g", nom_g);
        payload.put("num_p", num_p);
        if (nombre_etudiant_max_g != null) payload.put("nombre_etudiant_max_g", nombre_etudiant_max_g);
        if (est_finalise_g != null) payload.put("est_finalise_g", est_finalise_g);

        String resp = ApiClient.post("/groupe", payload);
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

    public static Map<String, Object> updateGroup(int id, Map<String, Object> data) throws IOException, InterruptedException {
        String resp = ApiClient.put("/groupe/" + id, data);
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

    public static Map<String, Object> deleteGroup(int id) throws IOException, InterruptedException {
        String resp = ApiClient.delete("/groupe/" + id);
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
}
