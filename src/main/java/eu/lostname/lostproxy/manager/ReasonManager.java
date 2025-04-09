/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 02.01.2021 @ 23:03:05
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ReasonManager.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Sorts;
import eu.lostname.lostproxy.database.LostProxyDatabase;
import eu.lostname.lostproxy.interfaces.bkms.IBanReason;
import eu.lostname.lostproxy.interfaces.bkms.IMuteReason;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ReasonManager {

    private final Gson gson;
    private final LostProxyDatabase database;

    private final ArrayList<IBanReason> registedBanReasons;
    private final ArrayList<IMuteReason> registedMuteReasons;

    private final ArrayList<String> banReasonCommandProcess;
    private final ArrayList<String> muteReasonCommandProcess;

    public ReasonManager(Gson gson, LostProxyDatabase database) {
        this.gson = gson;
        this.database = database;
        this.registedBanReasons = new ArrayList<>();
        this.registedMuteReasons = new ArrayList<>();
        this.banReasonCommandProcess = new ArrayList<>();
        this.muteReasonCommandProcess = new ArrayList<>();

        loadBanReasons();
        loadMuteReasons();
    }

    public void loadBanReasons() {
        database.getMongoDatabase().getCollection(MongoCollection.BAN_REASONS).find().sort(Sorts.ascending("_id")).forEach((Consumer<? super Document>) document -> this.registedBanReasons.add(gson.fromJson(document.toJson(), IBanReason.class)));
    }

    public void loadMuteReasons() {
        database.getMongoDatabase().getCollection(MongoCollection.MUTE_REASONS).find().sort(Sorts.ascending("_id")).forEach((Consumer<? super Document>) document -> this.registedMuteReasons.add(gson.fromJson(document.toJson(), IMuteReason.class)));
    }

    public void reloadBanReasons() {
        registedBanReasons.clear();
        loadBanReasons();
    }

    public void reloadMuteReasons() {
        registedMuteReasons.clear();
        database.getMongoDatabase().getCollection(MongoCollection.MUTE_REASONS).find().sort(Sorts.ascending("_id")).forEach((Consumer<? super Document>) document -> this.registedMuteReasons.add(gson.fromJson(document.toJson(), IMuteReason.class)));
    }

    public void saveBanReason(IBanReason iBanReason) {
        database.getMongoDatabase().getCollection(MongoCollection.BAN_REASONS).replaceOne(Filters.eq("_id", iBanReason.getId()), gson.fromJson(gson.toJson(iBanReason), Document.class), new ReplaceOptions().upsert(true));
    }

    public void saveMuteReason(IMuteReason iMuteReason) {
        database.getMongoDatabase().getCollection(MongoCollection.MUTE_REASONS).replaceOne(Filters.eq("_id", iMuteReason.getId()), gson.fromJson(gson.toJson(iMuteReason), Document.class), new ReplaceOptions().upsert(true));
    }

    public void deleteBanReason(IBanReason iBanReason) {
        database.getMongoDatabase().getCollection(MongoCollection.BAN_REASONS).deleteOne(Filters.eq("_id", iBanReason.getId()));
    }

    public void deleteMuteReason(IMuteReason iMuteReason) {
        database.getMongoDatabase().getCollection(MongoCollection.MUTE_REASONS).deleteOne(Filters.eq("_id", iMuteReason.getId()));
    }

    public IBanReason getBanReasonByID(int id) {
        return registedBanReasons.stream().filter(one -> one.getId() == id).findFirst().orElse(null);
    }

    public IMuteReason getMuteReasonByID(int id) {
        return registedMuteReasons.stream().filter(one -> one.getId() == id).findFirst().orElse(null);
    }

    public ArrayList<IBanReason> getRegistedBanReasons() {
        return registedBanReasons;
    }

    public ArrayList<IMuteReason> getRegistedMuteReasons() {
        return registedMuteReasons;
    }

    public ArrayList<String> getBanReasonCommandProcess() {
        return banReasonCommandProcess;
    }

    public ArrayList<String> getMuteReasonCommandProcess() {
        return muteReasonCommandProcess;
    }
}
