package wayfinder;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class LocationService {

    public static List<Document> getAllLocations() {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> locations = db.getCollection("locations");
        List<Document> results = new ArrayList<>();
        for (Document doc : locations.find()) {
            results.add(sanitize(doc));
        }
        return results;
    }

    public static List<Document> getLocationsByCategory(String category, int limit) {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> locations = db.getCollection("locations");
        FindIterable<Document> matches = locations.find(new Document("category", category)).limit(limit);
        List<Document> results = new ArrayList<>();
        for (Document doc : matches) {
            results.add(sanitize(doc));
        }
        return results;
    }

    private static Document sanitize(Document doc) {
        Document copy = new Document(doc);
        copy.remove("_id");
        return copy;
    }
}
