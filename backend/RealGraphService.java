import com.mongodb.client.*;
import org.bson.Document;
import java.util.*;

public class RealGraphService {

    static class Node {
        String name;
        double lat, lng;

        Node(String name, double lat, double lng) {
            this.name = name;
            this.lat = lat;
            this.lng = lng;
        }
    }

    static class Edge {
        String node;
        double weight;

        Edge(String node, double weight) {
            this.node = node;
            this.weight = weight;
        }
    }

    // 📏 Distance formula (Haversine)
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) *
                   Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    public static void runRealDijkstra() {

        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> loc = db.getCollection("locations");

        List<Node> nodes = new ArrayList<>();

        for (Document d : loc.find()) {
            nodes.add(new Node(
                d.getString("name"),
                d.getDouble("lat"),
                d.getDouble("lng")
            ));
        }

        System.out.println("✅ Loaded " + nodes.size() + " locations");

        // Build graph (connect nearby nodes)
        Map<String, List<Edge>> graph = new HashMap<>();

        for (Node a : nodes) {
            graph.putIfAbsent(a.name, new ArrayList<>());

            for (Node b : nodes) {
                if (!a.name.equals(b.name)) {
                    double dist = distance(a.lat, a.lng, b.lat, b.lng);

                    if (dist < 15) { // connect only nearby places
                        graph.get(a.name).add(new Edge(b.name, dist));
                    }
                }
            }
        }

        // Dijkstra
        String start = "Clock Tower";

        Map<String, Double> distMap = new HashMap<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));

        for (Node n : nodes) {
            distMap.put(n.name, Double.MAX_VALUE);
        }

        distMap.put(start, 0.0);
        pq.add(new Edge(start, 0));

        while (!pq.isEmpty()) {
            Edge curr = pq.poll();

            for (Edge neighbor : graph.get(curr.node)) {
                double newDist = distMap.get(curr.node) + neighbor.weight;

                if (newDist < distMap.get(neighbor.node)) {
                    distMap.put(neighbor.node, newDist);
                    pq.add(new Edge(neighbor.node, newDist));
                }
            }
        }

        System.out.println("\n📍 Real Shortest Distances from " + start + ":");

        for (String node : distMap.keySet()) {
            System.out.println(start + " → " + node + " = " + String.format("%.2f km", distMap.get(node)));
        }
    }
}