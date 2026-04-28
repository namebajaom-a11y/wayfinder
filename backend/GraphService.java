import java.util.*;

public class GraphService {

    static class Edge {
        String node;
        int weight;

        Edge(String node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }

    public static void runDijkstra() {

        System.out.println("\n🚀 Running Dijkstra Algorithm...");

        Map<String, List<Edge>> graph = new HashMap<>();

        // 🧭 GRAPH CONNECTIONS (SIMPLIFIED)
        graph.put("Clock Tower", Arrays.asList(new Edge("Ballupur", 5), new Edge("Rajpur Road", 7)));
        graph.put("Ballupur", Arrays.asList(new Edge("Prem Nagar", 6)));
        graph.put("Prem Nagar", Arrays.asList(new Edge("UPES", 10)));
        graph.put("Rajpur Road", Arrays.asList(new Edge("Jakhan", 4)));
        graph.put("Jakhan", Arrays.asList(new Edge("Mussoorie", 12)));
        graph.put("ISBT", Arrays.asList(new Edge("Airport", 15)));
        graph.put("Airport", new ArrayList<>());
        graph.put("Mussoorie", new ArrayList<>());

        String start = "Clock Tower";

        Map<String, Integer> dist = new HashMap<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        // Initialize distances
        for (String node : graph.keySet()) {
            dist.put(node, Integer.MAX_VALUE);
        }

        dist.put(start, 0);
        pq.add(new Edge(start, 0));

        while (!pq.isEmpty()) {
            Edge curr = pq.poll();

            for (Edge neighbor : graph.getOrDefault(curr.node, new ArrayList<>())) {
                int newDist = dist.get(curr.node) + neighbor.weight;

                if (newDist < dist.getOrDefault(neighbor.node, Integer.MAX_VALUE)) {
                    dist.put(neighbor.node, newDist);
                    pq.add(new Edge(neighbor.node, newDist));
                }
            }
        }

        System.out.println("\n📍 Shortest Distances from " + start + ":");
        for (String node : dist.keySet()) {
            System.out.println(start + " → " + node + " = " + dist.get(node));
        }
    }
}
