package wayfinder;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ChatbotService {

    /**
     * CLI Implementation: Handles chatbot interaction in the terminal.
     */
    public static void startChatbot() {
        Scanner sc = InputHelper.scanner();
        System.out.println("\n🤖 Chatbot Started (type 'exit' to stop)");
        System.out.println("----------------------------------------");

        while (true) {
            System.out.print("You: ");
            String input = sc.nextLine().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("\nBot: 👋 Chatbot Closed");
                break;
            }

            Map<String, Object> result = processQuery(input);
            System.out.println("\nBot: " + result.get("answer"));
            
            if (result.containsKey("locations")) {
                List<Document> locs = (List<Document>) result.get("locations");
                int i = 1;
                for (Document d : locs) {
                    System.out.println(i + ". " + d.getString("name") + " [" + d.getString("cat") + "]");
                    i++;
                }
            }
            System.out.println();
        }
    }

    /**
     * Core Logic: Processes a text query and returns structured data.
     * This is used by both the CLI and the Web API.
     */
    public static Map<String, Object> processQuery(String query) {
        Map<String, Object> response = new HashMap<>();
        String input = query.toLowerCase();

        String category = null;
        if (input.contains("hospital")) category = "Hospital";
        else if (input.contains("college") || input.contains("university")) category = "College";
        else if (input.contains("mall") || input.contains("shop")) category = "Mall";
        else if (input.contains("tourist") || input.contains("place")) category = "Tourist Place";
        else if (input.contains("transport") || input.contains("bus") || input.contains("station")) category = "Transport Hub";
        else if (input.contains("market")) category = "Market";

        if (category != null) {
            List<Document> found = LocationService.getLocationsByCategory(category, 5);
            response.put("answer", "I found " + found.size() + " " + category + "(s) for you in Dehradun.");
            response.put("locations", found);
            response.put("type", "category");
        } else {
            response.put("answer", "I'm not sure about that. Try asking about 'hospitals', 'colleges', 'malls', or 'tourist spots'!");
            response.put("type", "unknown");
        }

        return response;
    }
}