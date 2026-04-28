import com.mongodb.client.*;
import org.bson.Document;
import java.util.Scanner;

public class ChatbotService {

    static Scanner sc = new Scanner(System.in);

    public static void startChatbot() {

        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> loc = db.getCollection("locations");

        System.out.println("\n🤖 Chatbot Started (type 'exit' to stop)");
        System.out.println("----------------------------------------");

        while (true) {

            System.out.print("You: ");
            String input = sc.nextLine().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("\nBot: 👋 Chatbot Closed");
                break;
            }

            if (input.contains("hospital")) {

                System.out.println("\nBot: 📍 Nearby Hospitals Found:\n");

                int i = 1;
                for (Document d : loc.find(new Document("type", "hospital"))) {

                    System.out.println(i + ". " + d.getString("name"));
                    System.out.println("   Latitude: " + d.getDouble("lat"));
                    System.out.println("   Longitude: " + d.getDouble("lng"));
                    System.out.println();

                    i++;
                }

            } else {
                System.out.println("\nBot: ❓ Try 'nearby hospital'");
            }
        }
    }
}