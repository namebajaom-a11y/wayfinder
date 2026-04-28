package wayfinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.bson.Document;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WayfinderApiServer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        DBConnection.connect();
        InsertLocation.seedLocations();

        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);

        // API Endpoints
        server.createContext("/api/health", exchange -> handleJson(exchange, 200, Map.of("status", "ok")));
        
        server.createContext("/api/locations", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                handleJson(exchange, 200, LocationService.getAllLocations());
            } else {
                handleJson(exchange, 405, Map.of("error", "Method not allowed"));
            }
        });

        server.createContext("/api/traffic", exchange -> {
            if ("POST".equals(exchange.getRequestMethod()) || "GET".equals(exchange.getRequestMethod())) {
                handleJson(exchange, 200, TrafficService.generateTrafficReport());
            } else {
                handleJson(exchange, 405, Map.of("error", "Method not allowed"));
            }
        });

        server.createContext("/api/feedback", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                handleJson(exchange, 200, FeedbackService.getAllFeedbacks());
            } else if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    FeedbackBody req = GSON.fromJson(body, FeedbackBody.class);
                    Document feedback = FeedbackService.saveFeedback(
                            blankToDefault(req.name, "Anonymous"),
                            req.rating,
                            blankToDefault(req.message, "")
                    );
                    handleJson(exchange, 200, Map.of("message", "Feedback saved", "feedback", feedback));
                } catch (Exception e) {
                    handleJson(exchange, 500, Map.of("error", e.getMessage()));
                }
            } else {
                handleJson(exchange, 405, Map.of("error", "Method not allowed"));
            }
        });

        server.createContext("/api/chatbot", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    QueryBody req = GSON.fromJson(body, QueryBody.class);
                    Map<String, Object> response = ChatbotService.processQuery(req.query);
                    handleJson(exchange, 200, response);
                } catch (Exception e) {
                    handleJson(exchange, 500, Map.of("error", e.getMessage()));
                }
            } else {
                handleJson(exchange, 405, Map.of("error", "Method not allowed"));
            }
        });

        // Static Files Handler
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";
            String resourcePath = "/static" + path;
            
            try (InputStream in = WayfinderApiServer.class.getResourceAsStream(resourcePath)) {
                if (in == null) {
                    handleJson(exchange, 404, Map.of("error", "Not found"));
                    return;
                }
                byte[] bytes = in.readAllBytes();
                exchange.getResponseHeaders().set("Content-Type", contentType(path));
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } catch (Exception e) {
                handleJson(exchange, 500, Map.of("error", "Internal Server Error"));
            }
        });

        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool()); 
        server.start();
        System.out.println("Wayfinder web server running at http://localhost:" + PORT);
        System.out.println(">>> SERVER READY <<<");
    }

    private static void handleJson(HttpExchange exchange, int statusCode, Object response) throws IOException {
        String json = GSON.toJson(response);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        
        // CORS Headers
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
        } else {
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private static String contentType(String path) {
        if (path.endsWith(".css")) return "text/css; charset=utf-8";
        if (path.endsWith(".js")) return "application/javascript; charset=utf-8";
        return "text/html; charset=utf-8";
    }

    private static String blankToDefault(String value, String def) {
        return (value == null || value.trim().isEmpty()) ? def : value.trim();
    }

    private static class FeedbackBody {
        String name;
        int rating;
        String message;
    }

    private static class QueryBody {
        String query;
    }
}
