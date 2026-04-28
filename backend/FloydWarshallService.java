import java.util.*;

public class FloydWarshallService {

    public static void runFloydWarshall() {

        String[] nodes = {
            "Clock Tower", "Ballupur", "Prem Nagar", "Rajpur Road"
        };

        int n = nodes.length;
        int INF = 9999;

        int[][] dist = {
            {0, 5, INF, 7},
            {5, 0, 6, INF},
            {INF, 6, 0, INF},
            {7, INF, INF, 0}
        };

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {

                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        System.out.println("\n📍 Floyd-Warshall Matrix:");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(dist[i][j] + "\t");
            }
            System.out.println();
        }
    }
}