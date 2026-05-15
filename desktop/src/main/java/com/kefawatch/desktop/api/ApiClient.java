package com.kefawatch.desktop.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static String jwtToken = null;

    public static String getJwtToken() {
        return jwtToken;
    }
    public static String login(String username, String password) {
        try {
            String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());
            if (response.statusCode() == 200) {
                jwtToken = root.path("data").path("accessToken").asText();
                return null;
            } else {
                return root.path("error").path("message").asText("Giriş başarısız.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Sunucuya bağlanılamadı.";
        }
    }

    public static String register(String username, String email, String firstName, String lastName, String password) {
        try {
            ObjectNode node = mapper.createObjectNode();
            node.put("username", username);
            node.put("email", email);
            node.put("firstName", firstName);
            node.put("lastName", lastName);
            node.put("password", password);
            String body = mapper.writeValueAsString(node);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());
            if (response.statusCode() == 201) {
                jwtToken = root.path("data").path("accessToken").asText();
                return null;
            } else {
                return root.path("error").path("message").asText("Kayıt başarısız.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Sunucuya bağlanılamadı.";
        }
    }

    public static JsonNode getTitles() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/titles?page=0&size=50"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return mapper.readTree(response.body()).path("data").path("content");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode getTitleDetails(long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/titles/" + id))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return mapper.readTree(response.body()).path("data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addToWatchlist(long titleId) {
        try {
            String body = String.format("{\"titleId\":%d}", titleId);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/watchlist"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean markAsWatched(long titleId) {
        try {
            String body = String.format("{\"titleId\":%d,\"episodeId\":null,\"positionSeconds\":0,\"completed\":true}", titleId);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/progress"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static java.util.List<Long> getWatchlist() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/watchlist"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode arr = mapper.readTree(response.body()).path("data");
                java.util.List<Long> ids = new java.util.ArrayList<>();
                if (arr.isArray()) {
                    for (JsonNode n : arr) {
                        ids.add(n.path("titleId").asLong());
                    }
                }
                return ids;
            }
        } catch (Exception e) {}
        return new java.util.ArrayList<>();
    }

    public static java.util.List<Long> getWatched() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/progress"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode arr = mapper.readTree(response.body()).path("data");
                java.util.List<Long> ids = new java.util.ArrayList<>();
                if (arr.isArray()) {
                    for (JsonNode n : arr) {
                        if (n.path("completed").asBoolean(false)) {
                            ids.add(n.path("titleId").asLong());
                        }
                    }
                }
                return ids;
            }
        } catch (Exception e) {}
        return new java.util.ArrayList<>();
    }
    public static JsonNode searchTitles(String query) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/titles?page=0&size=50&search=" + java.net.URLEncoder.encode(query, "UTF-8")))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return mapper.readTree(response.body()).path("data").path("content");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createTitle(String type, String name, String description, String posterUrl, String trailerUrl, String externalRef) {
        try {
            ObjectNode node = mapper.createObjectNode();
            node.put("type", type);
            node.put("name", name);
            node.put("description", description);
            node.put("posterUrl", posterUrl);
            node.put("trailerUrl", trailerUrl);
            node.put("externalRef", externalRef);
            String body = mapper.writeValueAsString(node);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/titles"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());
            if (response.statusCode() == 200) {
                return null;
            } else {
                return root.path("error").path("message").asText("Ekleme başarısız.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Sunucu hatası.";
        }
    }
}
