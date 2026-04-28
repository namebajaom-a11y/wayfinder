import java.util.Random;

public class WeatherService {

    // ANSI Colors
    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";

    public static void simulateWeather() {

        Random rand = new Random();

        double baseDistance = 10.0;

        // Random weather type
        int w = rand.nextInt(4);

        String weatherType = "";
        double weatherFactor = 1.0;

        if (w == 0) {
            weatherType = GREEN + "☀️ Sunny" + RESET;
            weatherFactor = 1.0;
        } else if (w == 1) {
            weatherType = YELLOW + "☁️ Cloudy" + RESET;
            weatherFactor = 1.1;
        } else if (w == 2) {
            weatherType = BLUE + "🌧️ Rainy" + RESET;
            weatherFactor = 1.3;
        } else {
            weatherType = RED + "⛈️ Stormy" + RESET;
            weatherFactor = 1.6;
        }

        double finalDistance = baseDistance * weatherFactor;

        // OUTPUT
        System.out.println(CYAN + "\n============================" + RESET);
        System.out.println(CYAN + "🌦️ WEATHER SIMULATION REPORT" + RESET);
        System.out.println(CYAN + "============================" + RESET);

        System.out.println("📍 Route: Clock Tower → Mussoorie");
        System.out.println("🛣️ Base Distance: " + baseDistance + " km");

        System.out.println("🌤️ Weather Condition: " + weatherType);
        System.out.println("⚙️ Weather Factor: " + weatherFactor);

        System.out.println("--------------------------------");

        System.out.println("📊 Adjusted Travel Cost: " + String.format("%.2f km", finalDistance));

        // Suggestion
        if (weatherFactor > 1.4) {
            System.out.println(RED + "⚠️ Warning: Avoid travel / choose safer route!" + RESET);
        } else if (weatherFactor > 1.1) {
            System.out.println(YELLOW + "⚠️ Moderate impact: Drive carefully" + RESET);
        } else {
            System.out.println(GREEN + "✅ Weather is clear, route is safe" + RESET);
        }

        System.out.println(CYAN + "============================\n" + RESET);
    }
}