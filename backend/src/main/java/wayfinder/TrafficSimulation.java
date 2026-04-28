package wayfinder;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;
import java.util.Random;

public class TrafficSimulation {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        runTrafficSimulation();
    }

    public static void runTrafficSimulation() {
        Random rand = new Random();

        String[] routes = {
                "Clock Tower -> ISBT",
                "Rajpur Road -> Mussoorie",
                "Ballupur -> Prem Nagar",
                "ISBT -> Airport"
        };

        String selectedRoute = routes[rand.nextInt(routes.length)];
        int baseDistance = 8 + rand.nextInt(10);
        int trafficDelay = rand.nextInt(20);
        double fuelRate = 0.08;
        double fuelUsed = baseDistance * fuelRate;
        double totalCost = baseDistance + (trafficDelay * 0.5);

        String trafficLevel;
        String suggestion;
        String trafficLevelPlain;

        if (trafficDelay < 5) {
            trafficLevel = GREEN + "LOW TRAFFIC" + RESET;
            trafficLevelPlain = "LOW TRAFFIC";
            suggestion = GREEN + "Smooth journey expected" + RESET;
        } else if (trafficDelay < 12) {
            trafficLevel = YELLOW + "MEDIUM TRAFFIC" + RESET;
            trafficLevelPlain = "MEDIUM TRAFFIC";
            suggestion = YELLOW + "Drive carefully" + RESET;
        } else {
            trafficLevel = RED + "HEAVY TRAFFIC" + RESET;
            trafficLevelPlain = "HEAVY TRAFFIC";
            suggestion = RED + "Consider alternate route!" + RESET;
        }

        saveTrafficReport(selectedRoute, baseDistance, trafficDelay, fuelUsed, totalCost, trafficLevelPlain);

        System.out.println(CYAN + "\n===================================" + RESET);
        System.out.println(CYAN + "SMART TRAFFIC ANALYSIS REPORT" + RESET);
        System.out.println(CYAN + "===================================" + RESET);
        System.out.println("Selected Route: " + PURPLE + selectedRoute + RESET);
        System.out.println("Base Distance: " + baseDistance + " km");
        System.out.println();
        System.out.println("Traffic Delay: " + trafficDelay + " minutes");
        System.out.println("Traffic Level: " + trafficLevel);
        System.out.println("-----------------------------------");
        System.out.println("Estimated Fuel Usage: " + String.format("%.2f L", fuelUsed));
        System.out.println("Total Travel Cost: " + String.format("%.2f", totalCost));
        System.out.println();
        System.out.println("Suggestion: " + suggestion);
        System.out.println("Saved in MongoDB collection: traffic_reports");
        System.out.println(CYAN + "===================================\n" + RESET);
    }

    public static void simulateTraffic() {
        runTrafficSimulation();
    }

    private static void saveTrafficReport(String route, int baseDistance, int trafficDelay,
                                          double fuelUsed, double totalCost, String trafficLevel) {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> trafficReports = db.getCollection("traffic_reports");

        Document report = new Document("route", route)
                .append("baseDistanceKm", baseDistance)
                .append("trafficDelayMinutes", trafficDelay)
                .append("fuelUsedLiters", fuelUsed)
                .append("totalTravelCost", totalCost)
                .append("trafficLevel", trafficLevel)
                .append("createdAt", new Date());

        trafficReports.insertOne(report);
    }
}
