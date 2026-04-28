package wayfinder;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

public class DBConnection {

    private static final String DEFAULT_URI = "mongodb://localhost:27017";
    private static final String DEFAULT_DB_NAME = "wayfinderDB";

    private static MongoClient client;

    public static MongoDatabase connect() {
        String mongoUri = System.getenv("MONGODB_URI");
        if (mongoUri == null || mongoUri.isBlank()) {
            mongoUri = DEFAULT_URI;
        }

        String dbName = System.getenv("MONGODB_DB");
        if (dbName == null || dbName.isBlank()) {
            dbName = DEFAULT_DB_NAME;
        }

        if (client == null) {
            client = MongoClients.create(mongoUri);
        }

        MongoDatabase db = client.getDatabase(dbName);
        db.runCommand(new Document("ping", 1));
        db.getCollection("connection_logs").updateOne(
                new Document("_id", "wayfinder-app"),
                new Document("$set", new Document("status", "connected")
                        .append("database", dbName)
                        .append("uri", mongoUri)
                        .append("updatedAt", new java.util.Date())),
                new UpdateOptions().upsert(true)
        );

        System.out.println("Connected to MongoDB");
        System.out.println("URI: " + mongoUri);
        System.out.println("Database: " + dbName);
        return db;
    }
}
