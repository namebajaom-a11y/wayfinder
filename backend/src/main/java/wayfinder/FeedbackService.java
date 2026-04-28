package wayfinder;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FeedbackService {

    /**
     * CLI Implementation: Handles user input from the terminal.
     */
    public static void addFeedbackCLI() {
        Scanner sc = InputHelper.scanner();
        try {
            System.out.println("\n📝 FEEDBACK FORM");
            System.out.println("----------------------");

            System.out.print("Enter your name: ");
            String name = sc.nextLine();

            System.out.print("Enter your feedback: ");
            String message = sc.nextLine();

            System.out.print("Rating (1-5): ");
            int rating = Integer.parseInt(sc.nextLine());

            saveFeedback(name, rating, message);
            System.out.println("\n✅ Feedback saved to MongoDB!");

        } catch (Exception e) {
            System.out.println("❌ Error saving feedback: " + e.getMessage());
        }
    }

    /**
     * Core Logic: Saves feedback to the MongoDB collection.
     * This is used by both the CLI and the Web API.
     */
    public static Document saveFeedback(String name, int rating, String message) {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> feedback = db.getCollection("feedback");

        Document fb = new Document("name", name)
                .append("message", message)
                .append("rating", rating)
                .append("date", new Date());

        feedback.insertOne(fb);
        return fb;
    }

    /**
     * Core Logic: Retrieves all feedback records, sorted by newest first.
     */
    public static List<Document> getAllFeedbacks() {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> feedback = db.getCollection("feedback");
        List<Document> results = new ArrayList<>();

        for (Document d : feedback.find().sort(new Document("date", -1))) {
            Document copy = new Document(d);
            copy.remove("_id"); // Remove internal MongoDB ID for cleaner API response
            results.add(copy);
        }
        return results;
    }

    /**
     * CLI Implementation: Displays all feedback in the terminal.
     */
    public static void viewFeedbackCLI() {
        try {
            System.out.println("\n📋 ALL FEEDBACK");
            System.out.println("----------------------");

            for (Document d : getAllFeedbacks()) {
                System.out.println("👤 Name: " + d.getString("name"));
                System.out.println("💬 Message: " + d.getString("message"));
                System.out.println("⭐ Rating: " + d.getInteger("rating"));
                System.out.println("📅 Date: " + d.get("date"));
                System.out.println("----------------------");
            }

        } catch (Exception e) {
            System.out.println("❌ Error fetching feedback: " + e.getMessage());
        }
    }
}
