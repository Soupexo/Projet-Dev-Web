package tdtp.modeles;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class ApiClient {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    private static final Gson gson = new Gson();

    public static String BASE_URL = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api";

    public static String get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String post(String path, Object body) throws IOException, InterruptedException {
        String json = gson.toJson(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String put(String path, Object body) throws IOException, InterruptedException {
        String json = gson.toJson(body);
        System.out.println("DEBUG API CLIENT PUT: path=" + path);
        System.out.println("DEBUG API CLIENT PUT: body=" + body);
        System.out.println("DEBUG API CLIENT PUT: json=" + json);
        System.out.println("DEBUG API CLIENT PUT: URL=" + BASE_URL + path);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("DEBUG API CLIENT PUT: status code=" + response.statusCode());
        System.out.println("DEBUG API CLIENT PUT: response=" + response.body());
        
        return response.body();
    }

    public static String delete(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String delete(String path, Map<String, Object> data) throws IOException, InterruptedException {
        String jsonBody = gson.toJson(data);
        System.out.println("DEBUG API CLIENT DELETE: path=" + path);
        System.out.println("DEBUG API CLIENT DELETE: jsonBody=" + jsonBody);
        
        // Utiliser POST car DELETE avec corps n'est pas supporté dans toutes les versions
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-HTTP-Method-Override", "DELETE")  // Indiquer l'intention DELETE
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("DEBUG API CLIENT DELETE: status=" + response.statusCode());
        System.out.println("DEBUG API CLIENT DELETE: response=" + response.body());
        return response.body();
    }

    /**
     * Performs DELETE and returns both HTTP status and body so callers can handle non-2xx codes.
     */
    public static HttpResult deleteWithStatus(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new HttpResult(response.statusCode(), response.body());
    }

    public static class HttpResult {
        private final int statusCode;
        private final String body;
        public HttpResult(int statusCode, String body) { this.statusCode = statusCode; this.body = body; }
        public int getStatusCode() { return statusCode; }
        public String getBody() { return body; }
        @Override public String toString() { return "HttpResult{"+statusCode+", body="+(body==null?"null":body.replaceAll("\n","\\n"))+"}"; }
    }

    public static Gson gson() {
        return gson;
    }
}
