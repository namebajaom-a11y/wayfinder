package wayfinder;

import java.util.Scanner;

public class WayfinderApplication {

    static Scanner sc = InputHelper.scanner();

    public static void main(String[] args) {
        try {
            DBConnection.connect();
            InsertLocation.seedLocations();
        } catch (Exception e) {
            System.out.println("MongoDB connection failed: " + e.getMessage());
            System.out.println("Make sure MongoDB is running and Compass is connected to the same URI.");
            return;
        }

        while (true) {
            printMenu();
            System.out.print("\nEnter choice: ");

            if (!sc.hasNextLine()) {
                System.out.println("\nNo input received. Exiting Wayfinder System...");
                System.out.println("====================================");
                return;
            }

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    SignupService.signupWithPhone();
                    break;
                case 2:
                    LoginService.loginWithPhone();
                    break;
                case 3:
                    MongoGraphService.showLocations();
                    break;
                case 4:
                    GraphService.runDijkstra();
                    break;
                case 5:
                    AStarService.runAStar();
                    break;
                case 6:
                    GraphService.runBellmanFord();
                    break;
                case 7:
                    FloydWarshallService.runFloydWarshall();
                    break;
                case 8:
                    TrafficSimulation.runTrafficSimulation();
                    break;
                case 9:
                    WeatherService.runWeatherSimulation();
                    break;
                case 10:
                    NetworkService.showNetworkStatus();
                    break;
                case 11:
                    RealGraphService.runSmartRouting();
                    break;
                case 12:
                    ChatbotService.startChatbot();
                    break;
                case 13:
                    FeedbackService.addFeedbackCLI();
                    break;
                case 0:
                    System.out.println("\nExiting Wayfinder System...");
                    System.out.println("====================================");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nWAYFINDER SMART NAVIGATION SYSTEM");
        System.out.println("====================================");
        System.out.println("1. Signup (Phone)");
        System.out.println("2. Login");
        System.out.println("3. Show Locations (MongoDB)");
        System.out.println("4. Run Dijkstra");
        System.out.println("5. Run A*");
        System.out.println("6. Bellman-Ford (Advanced Routing)");
        System.out.println("7. Floyd-Warshall");
        System.out.println("8. Traffic Simulation");
        System.out.println("9. Weather Simulation");
        System.out.println("10. Network Status");
        System.out.println("11. Smart Routing");
        System.out.println("12. Chatbot (Location Assistant)");
        System.out.println("13. Submit Feedback");
        System.out.println("0. Exit");
    }
}
