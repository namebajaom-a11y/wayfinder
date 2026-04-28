import java.util.Scanner;

public class WayfinderApplication {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("\n🚀 Welcome to WAYFINDER SMART NAVIGATION SYSTEM");

        while (true) {

            System.out.println("\n==============================");
            System.out.println("        MAIN MENU");
            System.out.println("==============================");

            System.out.println("1. Signup (Phone)");
            System.out.println("2. Login");
            System.out.println("3. Show Locations (MongoDB)");
            System.out.println("4. Run Dijkstra");
            System.out.println("5. Run A* Search");
            System.out.println("6. Bellman-Ford");
            System.out.println("7. Floyd-Warshall");
            System.out.println("8. Traffic Simulation");
            System.out.println("9. Weather Simulation");
            System.out.println("10. Network Status");
            System.out.println("11. Smart Routing (All Combined)");
            System.out.println("0. Exit");

            System.out.print("\n👉 Enter your choice: ");

            int choice;

            // 🛑 Handle invalid input
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine();
            } else {
                System.out.println("❌ Invalid input! Enter a number.");
                sc.nextLine();
                continue;
            }

            switch (choice) {

                case 1:
                    SignupService.signupWithPhone();
                    break;

                case 2:
                    AuthService.login();
                    break;

                case 3:
                    LocationService.showLocations();
                    break;

                case 4:
                    RealGraphService.runRealDijkstra();
                    break;

                case 5:
                    AStarService.runAStar();
                    break;

                case 6:
                    BellmanFordService.runBellmanFord();
                    break;

                case 7:
                    FloydWarshallService.runFloydWarshall();
                    break;

                case 8:
                    TrafficSimulation.simulateTraffic();
                    break;

                case 9:
                    WeatherService.simulateWeather();
                    break;

                case 10:
                    NetworkService.checkNetwork();
                    break;

                case 11:
                    SmartRouteService.runSmartRouting();
                    break;

                case 0:
                    System.out.println("\n👋 Exiting Wayfinder... Thank You!");
                    System.exit(0);

                default:
                    System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }
}