package wayfinder;

import org.bson.Document;

import java.util.Date;
import java.util.Random;

public class TrafficService {

    public static Document generateTrafficReport() {
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
        if (trafficDelay < 5) {
            trafficLevel = "LOW TRAFFIC";
            suggestion = "Smooth journey expected";
        } else if (trafficDelay < 12) {
            trafficLevel = "MEDIUM TRAFFIC";
            suggestion = "Drive carefully";
        } else {
            trafficLevel = "HEAVY TRAFFIC";
            suggestion = "Consider alternate route!";
        }

        Document report = new Document("route", selectedRoute)
                .append("baseDistanceKm", baseDistance)
                .append("trafficDelayMinutes", trafficDelay)
                .append("fuelUsedLiters", fuelUsed)
                .append("totalTravelCost", totalCost)
                .append("trafficLevel", trafficLevel)
                .append("suggestion", suggestion)
                .append("createdAt", new Date());

        DBConnection.connect().getCollection("traffic_reports").insertOne(report);
        report.remove("_id");
        return report;
    }
}
