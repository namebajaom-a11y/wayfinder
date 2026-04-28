import com.mongodb.client.*;
import org.bson.Document;
import java.util.*;

public class MongoGraphService {

    static class Edge {
        String node;
        int weight;

        Edge(String node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }

    public static void runDijkstraFromDB() {

        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> loc = db.getCollection("locations");

        System.out.println("\n📡 Fetching locations from MongoDB...");

        List<String> nodes = new ArrayList<>();

        for (Document d : loc.find()) {
            nodes.add(d.getString("name"));
        }

        System.out.println("✅ Nodes loaded: " + nodes.size());

        // Create graph
        Map<String, List<Edge>> graph = new HashMap<>();

        // ⚠️ SIMPLE CONNECTION LOGIC (demo)
        for (int i = 0; i < nodes.size() - 1; i++) {
            String a = nodes.get(i);
            String b = nodes.get(i + 1);

            graph.putIfAbsent(a, new ArrayList<>());
            graph.get(a).add(new Edge(b, (i + 1) * 3)); // random weight
        }

        // Start node
        String start = nodes.get(0);

        Map<String, Integer> dist = new HashMap<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        for (String node : nodes) {
            dist.put(node, Integer.MAX_VALUE);
        }

        dist.put(start, 0);
        pq.add(new Edge(start, 0));

        while (!pq.isEmpty()) {
            Edge curr = pq.poll();

            for (Edge neighbor : graph.getOrDefault(curr.node, new ArrayList<>())) {
                int newDist = dist.get(curr.node) + neighbor.weight;

                if (newDist < dist.get(neighbor.node)) {
                    dist.put(neighbor.node, newDist);
                    pq.add(new Edge(neighbor.node, newDist));
                }
            }
        }

        System.out.println("\n📍 Shortest Paths from " + start + ":");

        for (String node : dist.keySet()) {
            System.out.println(start + " → " + node + " = " + dist.get(node));
        }
    }
}