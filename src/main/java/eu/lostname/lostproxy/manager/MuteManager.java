/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:41:03
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MuteManager.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.bkms.IMute;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class MuteManager {


    /**
     * Returns a mute when the given player is muted
     *
     * @param uniqueId the uniqueId of the player who has to be checked
     * @return the ban of the given uniqueId
     */
    public IMute getMute(UUID uniqueId) {
        Document d = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_MUTES).find(eq("_id", uniqueId.toString())).first();

        return d != null ? LostProxy.getInstance().getGson().fromJson(d.toJson(), IMute.class) : null;
    }

    /**
     * Saves the given mute in the database
     *
     * @param iMute the mute which has to be saved
     */
    public void saveMute(IMute iMute) {
        Gson gson = LostProxy.getInstance().getGson();
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_MUTES).replaceOne(eq("_id", iMute.getUniqueId().toString()), gson.fromJson(gson.toJson(iMute), Document.class), new ReplaceOptions().upsert(true));
    }

    /**
     * Inserts the given mute in the database
     *
     * @param iMute that is gonna be inserted into the database
     */
    public void insertMute(IMute iMute) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_MUTES).insertOne(LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(iMute), Document.class));
    }

    /**
     * Deletes the given mute in the database
     *
     * @param iMute the mute which has to be deleted
     */
    public void deleteMute(IMute iMute) {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.ACTIVE_MUTES).deleteOne(eq("_id", iMute.getUniqueId().toString()));
    }

    /**
     * Returns a string which displays the remaining time to a given end
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