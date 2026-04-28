import com.mongodb.client.*;
import org.bson.Document;
import java.util.*;

public class AStarService {

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
        double cost;

        Edge(String node, double cost) {
            this.node = node;
            this.cost = cost;
        }
    }

    // Heuristic = straight-line distance
    public static double heuristic(Node a, Node b) {
        double dx = a.lat - b.lat;
        double dy = a.lng - b.lng;
        return Math.sqrt(dx*dx + dy*dy);
    }

    public static void runAStar() {

        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> loc = db.getCollection("locations");

        List<Node> nodes = new ArrayList<>();
        Map<String, Node> nodeMap = new HashMap<>();

        for (Document d : loc.find()) {
            Node n = new Node(
                d.getString("name"),
                d.getDouble("lat"),
                d.getDouble("lng")
            );
            nodes.add(n);
            nodeMap.put(n.name, n);
        }

        // Build graph (same as before)
        Map<String, List<Edge>> graph = new HashMap<>();

        for (Node a : nodes) {
            graph.putIfAbsent(a.name, new ArrayList<>());

            for (Node b : nodes) {
                if (!a.name.equals(b.name)) {
                    double dist = heuristic(a, b);

                    if (dist < 0.2) { // nearby nodes
                        graph.get(a.name).add(new Edge(b.name, dist));
                    }
                }
            }
        }

        String start = "Clock Tower";
        String goal = "Mussoorie";

        Map<String, Double> gScore = new HashMap<>();
        Map<String, Double> fScore = new HashMap<>();

        for (Node n : nodes) {
            gScore.put(n.name, Double.MAX_VALUE);
            fScore.put(n.name, Double.MAX_VALUE);
        }

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(nodeMap.get(start), nodeMap.get(goal)));

        PriorityQueue<Edge> open = new PriorityQueue<>(Comparator.comparingDouble(e -> e.cost));
        open.add(new Edge(start, fScore.get(start)));

        Map<String, String> cameFrom = new HashMap<>();

        while (!open.isEmpty()) {
            String current = open.poll().node;

            if (current.equals(goal)) break;

            for (Edge e : graph.get(current)) {
                double tentative = gScore.get(current) + e.cost;

                if (tentative < gScore.get(e.node)) {
                    cameFrom.put(e.node, current);
                    gScore.put(e.node, tentative);

                    double f = tentative + heuristic(nodeMap.get(e.node), nodeMap.get(goal));
                    fScore.put(e.node, f);

                    open.add(new Edge(e.node, f));
                }
            }
        }

        // Reconstruct path
        System.out.println("\n📍 A* Path from " + start + " to " + goal + ":");

        String curr = goal;
        List<String> path = new ArrayList<>();

        while (curr != null) {
            path.add(curr);
            curr = cameFrom.get(curr);
        }

        Collections.reverse(path);

        for (String p : path) {
            System.out.print(p + " → ");
        }
        System.out.println("END");
    }
}
