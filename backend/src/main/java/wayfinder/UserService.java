package wayfinder;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

public class UserService {

    public static Document signup(String phone, String password) {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> users = db.getCollection("users");

        phone = phone == null ? "" : phone.trim();
        password = password == null ? "" : password.trim();

        if (phone.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Phone and password cannot be empty.");
        }

        Document existing = users.find(new Document("phone", phone)).first();
        if (existing != null) {
            throw new IllegalStateException("Phone already registered!");
        }

        Document user = new Document("phone", phone)
                .append("password", password)
                .append("createdAt", new Date());
        users.insertOne(user);
        return user;
    }

    public static Document login(String phone, String password) {
        MongoDatabase db = DBConnection.connect();
        MongoCollection<Document> users = db.getCollection("users");

        phone = phone == null ? "" : phone.trim();
        password = password == null ? "" : password.trim();

        if (phone.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Phone and password cannot be empty.");
        }

        return users.find(new Document("phone", phone).append("password", password)).first();
    }
}
