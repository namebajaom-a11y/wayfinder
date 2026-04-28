package wayfinder;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoGraphService {

    public static void main(String[] args) {
        showLocations();
    }

    public static void showLocations() {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> locations = db.getCollection("locations");

        System.out.println("\n----------------------------------------");
        System.out.println("LOCATIONS FROM MONGODB");
        System.out.println("----------------------------------------");

        MongoCursor<Document> cursor = locations.find().iterator();
        int count = 0;
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            count++;
            Double lat = doc.getDouble("lat");
            Double lng = doc.getDouble("lng");
            System.out.println(count + ". " + doc.getString("name")
                    + " | " + doc.getString("category")
                    + " | Lat: " + (lat != null ? lat : "N/A")
                    + " | Lng: " + (lng != null ? lng : "N/A"));
        }

        if (count == 0) {
            System.out.println("No locations found in MongoDB.");
        }

        System.out.println("----------------------------------------");
    }
}
