package api;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KVTaskClient {
    URL url;
    String token;
    HttpClient client;

    public KVTaskClient(URL url) {
        this.url = url;
        client = HttpClient.newHttpClient();
        token = registerAndGetToken(String.valueOf(url));
    }

    private String registerAndGetToken(String url) {
        try {
            URI uri = URI.create(url + "register");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
            if (response.statusCode() == 200) {
                token = response.body();
            } else {
                System.out.println("Ошибка получения токена: "
                        + response.statusCode());
            }
        } catch (IOException | InterruptedException exception) {
            System.out.println("ошибка отправки запроса регистрации: "
                    + exception.getMessage());
        }
        return token;
    }

    public void put (String key, String json) {
        URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + token);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Ошибка при отправке данных");
            e.printStackTrace();
        }
        System.out.println(response);
    }

    public String load (String key) {
        URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            System.out.println("Ошибка при загрузки данных");
            e.printStackTrace();
            return null;
        }
    }
}
