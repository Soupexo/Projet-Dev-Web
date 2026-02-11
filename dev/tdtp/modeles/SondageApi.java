package tdtp.modeles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class SondageApi {

    private static final Gson gson = ApiClient.gson();

    /**
     * GET /sondage
     * Récupère tous les sondages courants
     */
    public static List<Map<String, Object>> getSondages() throws IOException, InterruptedException {
        String resp = ApiClient.get("/sondage");
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        try {
            List<Map<String, Object>> list = gson.fromJson(resp, type);
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * GET /sondage/{id}
     */
    public static Map<String, Object> getSondageById(String id) throws IOException, InterruptedException {
        String resp = ApiClient.get("/sondage/" + id);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            return gson.fromJson(resp, type);
        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response");
            err.put("raw", resp);
            return err;
        }
    }

    /**
     * POST /sondage
     * body: { nom_s, type_s, reponses_multiples_s, delai_s }
     */
    public static Map<String, Object> createSondage(
            String nom,
            String type,
            boolean reponsesMultiples,
            String delai
    ) throws IOException, InterruptedException {

        Map<String, Object> payload = new HashMap<>();
        payload.put("nom_s", nom);
        payload.put("type_s", type);
        payload.put("a_des_reponses_multiples_s", reponsesMultiples ? 1 : 0);
        payload.put("delai_s", delai);

        System.out.println("DEBUG API CREATE SONDAGE: payload=" + payload);
        String resp = ApiClient.post("/sondage/", payload);
        System.out.println("DEBUG API CREATE SONDAGE: réponse brute=" + resp);
        Type typeToken = new TypeToken<Map<String, Object>>(){}.getType();

        try {
            return gson.fromJson(resp, typeToken);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response");
            err.put("raw", resp);
            return err;
        }
    }

    /**
     * PUT /sondage/{id}
     */
    public static Map<String, Object> updateSondage(int id, Map<String, Object> data)
            throws IOException, InterruptedException {

        System.out.println("DEBUG API UPDATE SONDAGE: id=" + id + ", data=" + data);

        String resp = ApiClient.put("/sondage/" + id, data);
        System.out.println("DEBUG API UPDATE SONDAGE: réponse brute=" + resp);

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            return gson.fromJson(resp, type);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response");
            err.put("raw", resp);
            return err;
        }
    }

    /**
     * DELETE /sondage/{id}
     */
    public static Map<String, Object> deleteSondage(String id)
            throws IOException, InterruptedException {

        String resp = ApiClient.delete("/sondage/" + id);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();

        try {
            return gson.fromJson(resp, type);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response");
            err.put("raw", resp);
            return err;
        }
    }
    
    public static List<Sondage> getAllSondagesAsObjects() {
        List<Sondage> sondagesList = new ArrayList<>();
        try {
            List<Map<String, Object>> list = getSondages(); 
            for (Map<String, Object> map : list) {
                Sondage s = new Sondage();
                
                Object idObj = map.get("num_s");
                if (idObj instanceof Number) s.setId_s(((Number) idObj).intValue());
                
                Object nomObj = map.get("nom_s");
                if (nomObj != null) s.setNom_s(nomObj.toString());
                
                Object typeObj = map.get("type_s");
                if (typeObj != null) s.setType_s(typeObj.toString());
                
                Object reponsesMultiplesObj = map.get("a_des_reponses_multiples_s");
                if (reponsesMultiplesObj instanceof Boolean)
                    s.setReponses_multiples_s((Boolean) reponsesMultiplesObj);
                
                Object delaiObj = map.get("delai_s");
                if (delaiObj != null) s.setDelai_s(delaiObj.toString());
                
                sondagesList.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sondagesList;
    }
    
    /**
     * POST /reponse-sondage
     * Ajoute une réponse à un sondage
     * body: { id_sondage, id_etudiant, question, reponse, date_reponse }
     */
    public static Map<String, Object> ajouterReponse(
            int idSondage,
            int idEtudiant,
            String question,
            String reponse,
            String dateReponse
    ) throws IOException, InterruptedException {
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("id_sondage", idSondage);
        payload.put("id_etudiant", idEtudiant);
        payload.put("question", question);
        payload.put("reponse", reponse);
        payload.put("date_reponse", dateReponse);
        
        System.out.println("DEBUG API AJOUTER REPONSE: payload=" + payload);
        String resp = ApiClient.post("/reponse-sondage/index.php", payload);
        System.out.println("DEBUG API AJOUTER REPONSE: réponse brute=" + resp);
        
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        try {
            return gson.fromJson(resp, type);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("error", "Invalid JSON response");
            err.put("raw", resp);
            return err;
        }
    }

}
