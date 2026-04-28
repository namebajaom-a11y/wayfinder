import java.util.Random;

public class NetworkService {

    // ANSI Colors
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";

    public static void checkNetwork() {

        Random rand = new Random();

        // Simulate signal strength (0–100)
        int signal = rand.nextInt(101);

        String status;
        boolean lowNetwork = false;

        if (signal > 70) {
            status = GREEN + "🟢 STRONG SIGNAL" + RESET;
        } else if (signal > 40) {
            status = YELLOW + "🟡 MODERATE SIGNAL" + RESET;
            lowNetwork = true;
        } else {
            status = RED + "🔴 LOW SIGNAL" + RESET;
            lowNetwork = true;
        }

        // OUTPUT
        System.out.println(CYAN + "\n============================" + RESET);
        System.out.println(CYAN + "📡 NETWORK STATUS REPORT" + RESET);
        System.out.println(CYAN + "============================" + RESET);

        System.out.println("📶 Signal Strength: " + signal + "%");
        System.out.println("📊 Network Status: " + status);

        System.out.println("--------------------------------");

        if (lowNetwork) {
            System.out.println(RED + "⚠️ Low Network Mode ACTIVATED" + RESET);
            System.out.println("📦 Using stored data from MongoDB");
            System.out.println("🚫 Live updates disabled (traffic/weather)");
        } else {
            System.out.println(GREEN + "✅ Online Mode ACTIVE" + RESET);
            System.out.println("🌐 Fetching live data (maps, traffic, weather)");
        }

        System.out.println(CYAN + "============================\n" + RESET);
    }
}