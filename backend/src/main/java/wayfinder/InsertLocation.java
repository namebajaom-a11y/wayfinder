package wayfinder;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class InsertLocation {

    public static void seedLocations() {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> locations = db.getCollection("locations");

        List<Document> sampleLocations = Arrays.asList(
                new Document("name", "Clock Tower").append("category", "Landmark").append("lat", 30.3250).append("lng", 78.0419),
                new Document("name", "ISBT").append("category", "Transport").append("lat", 30.2886).append("lng", 78.0131),
                new Document("name", "Max Super Speciality Hospital").append("category", "Hospital").append("lat", 30.3256).append("lng", 78.0437),
                new Document("name", "Synergy Institute of Medical Sciences").append("category", "Hospital").append("lat", 30.3120).append("lng", 78.0215),
                new Document("name", "CMI Hospital").append("category", "Hospital").append("lat", 30.3540).append("lng", 78.0470),
                new Document("name", "Velmed Hospital").append("category", "Hospital").append("lat", 30.3200).append("lng", 78.0400),
                new Document("name", "SBI ATM").append("category", "ATM").append("lat", 30.3162).append("lng", 78.0322),
                new Document("name", "HDFC ATM").append("category", "ATM").append("lat", 30.3245).append("lng", 78.0415),
                new Document("name", "ICICI ATM").append("category", "ATM").append("lat", 30.3302).append("lng", 78.0440),
                new Document("name", "Indian Oil Petrol Pump").append("category", "Petrol Pump").append("lat", 30.3015).append("lng", 78.0185),
                new Document("name", "HP Petrol Pump").append("category", "Petrol Pump").append("lat", 30.3188).append("lng", 78.0299),
                new Document("name", "Bharat Petroleum Pump").append("category", "Petrol Pump").append("lat", 30.3362).append("lng", 78.0361),
                new Document("name", "Town Table Restaurant").append("category", "Restaurant").append("lat", 30.3671).append("lng", 78.0712),
                new Document("name", "Black Pepper Restaurant").append("category", "Restaurant").append("lat", 30.3240).append("lng", 78.0420),
                new Document("name", "Punjab Grill Restaurant").append("category", "Restaurant").append("lat", 30.3664).append("lng", 78.0707)
        );

        UpdateOptions upsert = new UpdateOptions().upsert(true);
        for (Document location : sampleLocations) {
            locations.updateOne(
                    new Document("name", location.getString("name")),
                    new Document("$set", location),
                    upsert
            );
        }
    }
}
