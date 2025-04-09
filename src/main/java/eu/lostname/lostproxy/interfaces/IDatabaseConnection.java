/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:39:13
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IDatabaseConnection.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class IDatabaseConnection {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public IDatabaseConnection(String host, String port, String username, String password, String database) {
        this.mongoClient = new MongoClient(new ServerAddress(host, Integer.parseInt(port)), MongoCredential.createCredential(username, database, password.toCharArray()), MongoClientOptions.builder().build());
        mongoDatabase = mongoClient.getDatabase(database);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
