package wayfinder;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Unified AuthService that handles both Signup and Login
 * Database: wayfinderDB
 * Collection: users
 * 
 * Usage:
 *   java wayfinder.AuthService signup <phone> <password>
 *   java wayfinder.AuthService login <phone> <password>
 *   java wayfinder.AuthService (interactive mode)
 */
public class AuthService {

    public static void main(String[] args) {
        if (args.length >= 2) {
            // Command-line mode
            String command = args[0].toLowerCase();
            String phone = args[1];
            String password = args.length >= 3 ? args[2] : "";
            
            if (command.equals("signup")) {
                signupWithPhone(phone, password);
            } else if (command.equals("login")) {
                loginWithPhone(phone, password);
            } else {
                showUsage();
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("menu")) {
            showMainMenu();
        } else if (args.length == 0) {
            // Interactive mode - show menu
            showMainMenu();
        } else {
            showUsage();
        }
    }

    private static void showUsage() {
        System.out.println("Usage:");
        System.out.println("  java wayfinder.AuthService signup <phone> <password>");
        System.out.println("  java wayfinder.AuthService login <phone> <password>");
        System.out.println("  java wayfinder.AuthService (interactive menu)");
    }

    public static void showMainMenu() {
        while (true) {
            System.out.println("\n========== WAYFINDER AUTH ==========");
            System.out.println("1. Signup (Create new account)");
            System.out.println("2. Login (Existing user)");
            System.out.println("3. Exit");
            System.out.print("Choose option (1-3): ");

            String choice = InputHelper.scanner().nextLine().trim();

            switch (choice) {
                case "1" -> signup();
                case "2" -> login();
                case "3" -> {
                    System.out.println("👋 Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    /**
     * Signup - Create a new user account (command-line mode)
     */
    public static void signupWithPhone(String phone, String password) {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> users = db.getCollection("users");

        System.out.println("\n--- SIGNUP (PHONE) ---");

        if (phone == null || phone.isEmpty()) {
            System.out.print("Enter Phone Number: ");
            phone = InputHelper.scanner().nextLine().trim();
        }

        if (password == null || password.isEmpty()) {
            System.out.print("Enter Password: ");
            password = InputHelper.scanner().nextLine().trim();
        }

        if (phone.isEmpty() || password.isEmpty()) {
            System.out.println("❌ Phone and password cannot be empty.");
            return;
        }

        // Check if user already exists
        Document existing = users.find(new Document("phone", phone)).first();

        if (existing != null) {
            System.out.println("❌ Phone already registered! Please login.");
            return;
        }

        // Create new user document
        Document newUser = new Document("phone", phone)
                .append("password", password)
                .append("createdAt", new java.util.Date());

        users.insertOne(newUser);

        System.out.println("✅ Signup successful! You can now login.");
        System.out.println("📱 Phone: " + phone);
    }

    /**
     * Signup - Interactive mode
     */
    public static void signup() {
        System.out.print("Enter Phone Number: ");
        String phone = InputHelper.scanner().nextLine().trim();

        System.out.print("Enter Password: ");
        String password = InputHelper.scanner().nextLine().trim();

        signupWithPhone(phone, password);
    }

    /**
     * Login - Authenticate an existing user (command-line mode)
     */
    public static void loginWithPhone(String phone, String password) {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> users = db.getCollection("users");

        System.out.println("\n--- LOGIN (PHONE) ---");

        if (phone == null || phone.isEmpty()) {
            System.out.print("Enter Phone Number: ");
            phone = InputHelper.scanner().nextLine().trim();
        }

        if (password == null || password.isEmpty()) {
            System.out.print("Enter Password: ");
            password = InputHelper.scanner().nextLine().trim();
        }

        if (phone.isEmpty() || password.isEmpty()) {
            System.out.println("❌ Phone and password cannot be empty.");
            return;
        }

        // Find user in MongoDB
        Document user = users.find(new Document("phone", phone)).first();

        if (user == null) {
            System.out.println("❌ No user found for phone: " + phone);
            System.out.println("   Please signup first.");
            return;
        }

        String dbPassword = user.getString("password");

        if (dbPassword != null && dbPassword.equals(password)) {
            System.out.println("✅ Login successful! Welcome back!");
            System.out.println("📱 Phone: " + phone);
        } else {
            System.out.println("❌ Invalid phone or password");
        }
    }

    /**
     * Login - Interactive mode
     */
    public static void login() {
        System.out.print("Enter Phone Number: ");
        String phone = InputHelper.scanner().nextLine().trim();

        System.out.print("Enter Password: ");
        String password = InputHelper.scanner().nextLine().trim();

        loginWithPhone(phone, password);
    }
}