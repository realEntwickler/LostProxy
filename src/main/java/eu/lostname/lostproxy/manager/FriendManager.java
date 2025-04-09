/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 02.01.2021 @ 23:03:05
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * FriendManager.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import eu.lostname.lostproxy.database.LostProxyDatabase;
import eu.lostname.lostproxy.interfaces.IFriendData;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.HashMap;
import java.util.UUID;

public class FriendManager {

    private final LostProxyDatabase database;
    private final Gson gson;

    public FriendManager(LostProxyDatabase database, Gson gson) {
        this.database = database;
        this.gson = gson;
    }

    public IFriendData getFriendData(UUID uniqueId) {
        Document d = database.getMongoDatabase().getCollection(MongoCollection.FRIEND_DATA).find(Filters.eq("_id", uniqueId.toString())).first();

        if (d == null) {
            d = gson.fromJson(gson.toJson(new IFriendData(uniqueId.toString(), System.currentTimeMillis(), new HashMap<>(), new HashMap<>(), true, true, true, true, true, System.currentTimeMillis())), Document.class);

            database.getMongoDatabase().getCollection(MongoCollection.FRIEND_DATA).insertOne(d);
        }

        return gson.fromJson(d.toJson(), IFriendData.class);
    }
}
