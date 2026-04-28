import com.mongodb.client.*;
import org.bson.Document;

public class InsertLocation {

    public static void main(String[] args) {

        // Connect to MongoDB
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("wayfinderDB");
        MongoCollection<Document> loc = db.getCollection("locations");

        System.out.println("✅ Connected to MongoDB");

        // Clear old data
        loc.deleteMany(new Document());

        // 📍 AREAS
        loc.insertOne(new Document("name", "Clock Tower").append("lat", 30.3256).append("lng", 78.0437));
        loc.insertOne(new Document("name", "Ballupur").append("lat", 30.3340).append("lng", 78.0330));
        loc.insertOne(new Document("name", "Prem Nagar").append("lat", 30.3400).append("lng", 78.0700));
        loc.insertOne(new Document("name", "Patel Nagar").append("lat", 30.3000).append("lng", 78.0200));
        loc.insertOne(new Document("name", "Raipur").append("lat", 30.3100).append("lng", 78.1000));
        loc.insertOne(new Document("name", "Clement Town").append("lat", 30.2830).append("lng", 78.0080));
        loc.insertOne(new Document("name", "Jakhan").append("lat", 30.3600).append("lng", 78.0700));
        loc.insertOne(new Document("name", "Nehru Colony").append("lat", 30.3105).append("lng", 78.0300));

        // 🛣️ ROADS
        loc.insertOne(new Document("name", "Rajpur Road").append("lat", 30.3500).append("lng", 78.0600));
        loc.insertOne(new Document("name", "Haridwar Bypass").append("lat", 30.2900).append("lng", 78.0100));
        loc.insertOne(new Document("name", "Mussoorie Road").append("lat", 30.3700).append("lng", 78.0900));

        // 🏔️ TOURIST
        loc.insertOne(new Document("name", "Mussoorie").append("lat", 30.4598).append("lng", 78.0664));
        loc.insertOne(new Document("name", "Sahastradhara").append("lat", 30.3872).append("lng", 78.1316));

        // 🛍️ MALLS
        loc.insertOne(new Document("name", "Pacific Mall").append("lat", 30.3530).append("lng", 78.0630));
        loc.insertOne(new Document("name", "Crossroad Mall").append("lat", 30.3250).append("lng", 78.0400));

        // 🏥 HOSPITALS
        loc.insertOne(new Document("name", "Max Hospital").append("lat", 30.3150).append("lng", 78.0320));
        loc.insertOne(new Document("name", "Synergy Hospital").append("lat", 30.3160).append("lng", 78.0000));
        loc.insertOne(new Document("name", "Graphic Era Hospital").append("lat", 30.2700).append("lng", 78.0500));

        // 🎓 COLLEGES
        loc.insertOne(new Document("name", "Graphic Era University").append("lat", 30.2680).append("lng", 78.0440));
        loc.insertOne(new Document("name", "UPES").append("lat", 30.4150).append("lng", 78.0780));
        loc.insertOne(new Document("name", "SGRR University").append("lat", 30.3160).append("lng", 78.0200));
        loc.insertOne(new Document("name", "Doon University").append("lat", 30.2760).append("lng", 78.0600));
        loc.insertOne(new Document("name", "Uttaranchal University").append("lat", 30.3770).append("lng", 78.1100));

        // 🚆 TRANSPORT
        loc.insertOne(new Document("name", "Railway Station").append("lat", 30.3165).append("lng", 78.0322));
        loc.insertOne(new Document("name", "ISBT").append("lat", 30.2880).append("lng", 78.0130));
        loc.insertOne(new Document("name", "Airport").append("lat", 30.1897).append("lng", 78.1803));

        System.out.println("✅ All locations inserted successfully!");
    }
}