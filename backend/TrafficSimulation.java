import java.util.Random;

public class TrafficSimulation {

    // ANSI Colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";

    public static void simulateTraffic() {

        Random rand = new Random();

        // Base route info
        int baseDistance = 10; // km
        int trafficDelay = rand.nextInt(15); // delay minutes

        int totalCost = baseDistance + trafficDelay;

        String trafficLevel;

        // Determine traffic level
        if (trafficDelay < 5) {
            trafficLevel = GREEN + "LOW TRAFFIC" + RESET;
        } else if (trafficDelay < 10) {
            trafficLevel = YELLOW + "MEDIUM TRAFFIC" + RESET;
        } else {
            trafficLevel = RED + "HEAVY TRAFFIC" + RESET;
        }

        // Display output
        System.out.println(CYAN + "\n============================" + RESET);
        System.out.println(CYAN + "🚦 TRAFFIC SIMULATION REPORT" + RESET);
        System.out.println(CYAN + "============================" + RESET);

        System.out.println("📍 Route: Clock Tower → ISBT");
        System.out.println("🛣️ Base Distance: " + baseDistance + " km");

        System.out.println("⏱️ Traffic Delay: " + trafficDelay + " min");
        System.out.println("🚗 Traffic Level: " + trafficLevel);

        System.out.println("--------------------------------");

        System.out.println("📊 Total Travel Cost: " + totalCost);

        if (trafficDelay > 10) {
            System.out.println(RED + "⚠️ Suggestion: Take alternate route!" + RESET);
        } else {
            System.out.println(GREEN + "✅ Route is optimal" + RESET);
        }

        System.out.println(CYAN + "============================\n" + RESET);
    }
}