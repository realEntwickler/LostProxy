/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 16.01.2021 @ 22:29:30
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * BanManager.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.bkms.IBan;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class BanManager {


    /**
     * Returns a ban when the given player is banned
     *
     * @param uniqueId the uniqueId of the player who has to be checked
     * @return the ban of the given uniqueId
     */
    public IBan getBan(UUID uniqueId) {
        Document d = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).find(eq("_id", uniqueId.toString())).first();

        return d != null ? LostProxy.getInstance().getGson().fromJson(d.toJson(), IBan.class) : null;
    }

    /**
     * Saves the given ban in the database
     *
     * @param iBan the ban which has to be saved
     */
    public void saveBan(IBan iBan) {
        Gson gson = LostProxy.getInstance().getGson();
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).replaceOne(eq("_id", iBan.getUniqueId().toString()), gson.fromJson(gson.toJson(iBan), Document.class), new ReplaceOptions().upsert(true));
    }

    /**
     * Inserts the given ban in the database
     *
     * @param iBan that is gonna be inserted into the database
     */
    public void insertBan(IBan iBan) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).insertOne(LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iBan), Document.class));
    }

    /**
     * Deletes the given ban in the database
     *
     * @param iBan the ban which has to be deleted
     */
    public void deleteBan(IBan iBan) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_BANS).deleteOne(eq("_id", iBan.getUniqueId().toString()));
    }

    /**
     * Returns a string which displays the remaining time to a given end
     *
     * @param end the end time
     * @return a string with the display
     */

    @SuppressWarnings("deprecation")
    public String calculateRemainingTime(long end) {
        long millis = end - System.currentTimeMillis();
        int seconds = 0, minutes = 0, hours = 0, days = 0;

        while (millis >= 1000) {
            millis -= 1000;
            seconds++;
        }

        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }

        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }

        while (hours >= 24) {
            hours -= 24;
            days++;
        }

        String estimatedTime = "";

        if (days == 1) {
            estimatedTime += "ein §7Tag§8, ";
        } else if (days > 1) {
            estimatedTime += days + " §7Tage§8, ";
        }

        if (hours == 1) {
            estimatedTime += "§ceine §7Stunde§8, ";
        } else if (hours > 1) {
            estimatedTime += "§c" + hours + " §7Stunden§8, ";
        }

        if (minutes == 1) {
            estimatedTime += "§ceine §7Minute und ";
        } else if (minutes > 1) {
            estimatedTime += "§c" + minutes + " §7Minuten und ";
        }

        if (seconds == 1) {
            estimatedTime += "§ceine §7Sekunde";
        } else if (seconds > 1) {
            estimatedTime += "§c" + seconds + " §7Sekunden";
        }

        return estimatedTime;
    }
}