package eu.lostname.lostproxy.interfaces;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class IMongoConnection {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public IMongoConnection(String dbName, String username, String password) {
        this.mongoClient = MongoClients.create(MongoClientSettings.builder().applyConnectionString(new ConnectionString("mongodb://172.16.3.1:27017")).credential(MongoCredential.createCredential(username, dbName, password.toCharArray())).build());
        this.mongoDatabase = mongoClient.getDatabase(dbName);
    }

    public MongoCollection<Document> getCollection (String collection) {
        return mongoDatabase.getCollection(collection);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
