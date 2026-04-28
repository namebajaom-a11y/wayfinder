package wayfinder;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Scanner;

public class LoginService {

    static Scanner sc = InputHelper.scanner();

    public static void main(String[] args) {
        loginWithPhone();
    }

    public static void loginWithPhone() {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> users = db.getCollection("users");

        System.out.println("\n--- LOGIN (PHONE) ---");

        System.out.print("Enter Phone Number: ");
        if (!sc.hasNextLine()) {
            System.out.println("No input received. Closing login.");
            return;
        }
        String phone = sc.nextLine().trim();

        System.out.print("Enter Password: ");
        if (!sc.hasNextLine()) {
            System.out.println("No input received. Closing login.");
            return;
        }
        String password = sc.nextLine().trim();

        if (phone.isEmpty() || password.isEmpty()) {
            System.out.println("Phone and password cannot be empty.");
            return;
        }

        Document query = new Document("phone", phone)
                .append("password", password);

        Document user = users.find(query).first();

        if (user != null) {
            System.out.println("Login successful!");
            System.out.println("User found in MongoDB users collection.");
        } else {
            System.out.println("Invalid phone or password");
            Document phoneMatch = users.find(new Document("phone", phone)).first();
            if (phoneMatch == null) {
                System.out.println("No user found for phone: " + phone);
            }
        }
    }
}
