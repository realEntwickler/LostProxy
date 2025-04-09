/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:40:48
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * HistoryManager.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.historyandentries.ban.IBanHistory;
import eu.lostname.lostproxy.interfaces.historyandentries.kick.IKickHistory;
import eu.lostname.lostproxy.interfaces.historyandentries.mute.IMuteHistory;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class HistoryManager {

    private final ArrayList<String> kickHistoryClearCommandProcess;
    private final ArrayList<String> banHistoryClearCommandProcess;
    private final ArrayList<String> muteHistoryClearCommandProcess;

    public HistoryManager() {
        this.kickHistoryClearCommandProcess = new ArrayList<>();
        this.banHistoryClearCommandProcess = new ArrayList<>();
        this.muteHistoryClearCommandProcess = new ArrayList<>();
    }

    public IKickHistory getKickHistory(UUID uniqueId) {
        Document d = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.KICK_HISTORIES).find(eq("_id", uniqueId.toString())).first();

        Gson gson = LostProxy.getInstance().getGson();
        if (d == null) {
            IKickHistory iKickHistory = new IKickHistory(uniqueId, new ArrayList<>());
            d = gson.fromJson(gson.toJson(iKickHistory), Document.class);

            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.KICK_HISTORIES).insertOne(d);
        }
        return gson.fromJson(d.toJson(), IKickHistory.class);
    }

    public void saveKickHistory(IKickHistory iKickHistory) {
        Gson gson = LostProxy.getInstance().getGson();
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.KICK_HISTORIES).replaceOne(eq("_id", iKickHistory.getUniqueId().toString()),
                gson.fromJson(gson.toJson(iKickHistory), Document.class),
                new ReplaceOptions().upsert(true));
    }

    public IBanHistory getBanHistory(UUID uniqueId) {
        Gson gson = LostProxy.getInstance().getGson();

        Document d = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).find(eq("_id", uniqueId.toString())).first();

        if (d == null) {
            IBanHistory iBanHistory = new IBanHistory(uniqueId, new ArrayList<>());
            d = gson.fromJson(gson.toJson(iBanHistory), Document.class);
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).insertOne(d);
        }

        return gson.fromJson(d.toJson(), IBanHistory.class);
    }

    public void saveBanHistory(IBanHistory iBanHistory) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.BAN_HISTORIES).replaceOne(eq("_id", iBanHistory.getUniqueId().toString()), LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iBanHistory), Document.class), new ReplaceOptions().upsert(true));
    }

    public IMuteHistory getMuteHistory(UUID uniqueId) {
        Gson gson = LostProxy.getInstance().getGson();

        Document d = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.MUTE_HISTORIES).find(eq("_id", uniqueId.toString())).first();

        if (d == null) {
            IMuteHistory iMuteHistory = new IMuteHistory(uniqueId, new ArrayList<>());
            d = gson.fromJson(gson.toJson(iMuteHistory), Document.class);
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.MUTE_HISTORIES).insertOne(d);
        }

        return gson.fromJson(d.toJson(), IMuteHistory.class);
    }

    public void saveMuteHistory(IMuteHistory iMuteHistory) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.MUTE_HISTORIES).replaceOne(eq("_id", iMuteHistory.getUniqueId().toString()), LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iMuteHistory), Document.class), new ReplaceOptions().upsert(true));
    }

    public ArrayList<String> getKickHistoryClearCommandProcess() {
        return kickHistoryClearCommandProcess;
    }

    public ArrayList<String> getBanHistoryClearCommandProcess() {
        return banHistoryClearCommandProcess;
    }

    public ArrayList<String> getMuteHistoryClearCommandProcess() {
        return muteHistoryClearCommandProcess;
    }
}
