package wayfinder;

import java.util.Scanner;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.BsonObjectId;
import org.bson.Document;

public class SignupService {

    static Scanner sc = InputHelper.scanner();

    public static void main(String[] args) {
        signupWithPhone();
    }

    public static void signupWithPhone() {

        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> users = db.getCollection("users");

        System.out.println("\n--- SIGNUP (PHONE) ---");

        System.out.print("Enter Phone Number: ");
        if (!sc.hasNextLine()) {
            System.out.println("No input received. Closing signup.");
            return;
        }
        String phone = sc.nextLine();

        System.out.print("Enter Password: ");
        if (!sc.hasNextLine()) {
            System.out.println("No input received. Closing signup.");
            return;
        }
        String password = sc.nextLine();

        phone = phone.trim();
        password = password.trim();

        if (phone.isEmpty() || password.isEmpty()) {
            System.out.println("Phone and password cannot be empty.");
            return;
        }

        Document existing = users.find(new Document("phone", phone)).first();

        if (existing != null) {
            System.out.println("Phone already registered!");
            return;
        }

        Document user = new Document("phone", phone)
                .append("password", password)
                .append("createdAt", new java.util.Date());

        InsertOneResult result = users.insertOne(user);

        System.out.println("Signup successful. Phone stored in MongoDB.");
        System.out.println("Saved in collection: users");

        org.bson.BsonValue insertedIdValue = result.getInsertedId();
        if (insertedIdValue instanceof BsonObjectId insertedId) {
            Document savedUser = users.find(new Document("_id", insertedId.getValue())).first();
            System.out.println("Inserted user id: " + insertedId.getValue());
            System.out.println("Users count now: " + users.countDocuments());
            if (savedUser != null) {
                System.out.println("Stored phone: " + savedUser.getString("phone"));
            }
        }
    }
}
