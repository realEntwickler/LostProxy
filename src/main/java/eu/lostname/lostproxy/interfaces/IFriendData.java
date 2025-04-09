/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 01.01.2021 @ 23:39:20
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * IFriendData.java is part of the lostproxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy.interfaces;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.HashMap;
import java.util.UUID;

public class IFriendData {

    private final String _id;
    private final long timestamp;
    private HashMap<String, Long> friends;
    private HashMap<String, Long> requests;
    private boolean notifyMessagesEnabled, friendRequestsAllowed, friendJumpAllowed, friendsSeeOnlineStatusAllowed, sentMessagesAllowed;
    private long lastLogoutTimestamp;

    public IFriendData(String _id, long timestamp, HashMap<String, Long> friends, HashMap<String, Long> requests, boolean notifyMessagesEnabled, boolean friendRequestsAllowed, boolean friendJumpAllowed, boolean friendsSeeOnlineStatusAllowed, boolean sentMessagesAllowed, long lastLogoutTimestamp) {
        this._id = _id;
        this.timestamp = timestamp;
        this.friends = friends;
        this.requests = requests;
        this.notifyMessagesEnabled = notifyMessagesEnabled;
        this.friendRequestsAllowed = friendRequestsAllowed;
        this.friendJumpAllowed = friendJumpAllowed;
        this.friendsSeeOnlineStatusAllowed = friendsSeeOnlineStatusAllowed;
        this.sentMessagesAllowed = sentMessagesAllowed;
        this.lastLogoutTimestamp = lastLogoutTimestamp;
    }

    public void save() {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.FRIEND_DATA).replaceOne(Filters.eq("_id", _id), LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(this), Document.class), new ReplaceOptions().upsert(true));
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public HashMap<String, Long> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, Long> friends) {
        this.friends = friends;
    }

    public void addFriend(UUID friendUUID) {
        friends.put(friendUUID.toString(), System.currentTimeMillis());
    }

    public void removeFriend(UUID friendUUID) {
        friends.remove(friendUUID.toString());
    }

    public boolean isFriend(UUID friendUUID) {
        return friends.containsKey(friendUUID.toString());
    }

    public HashMap<String, Long> getRequests() {
        return requests;
    }

    public void setRequests(HashMap<String, Long> requests) {
        this.requests = requests;
    }

    public void addRequest(UUID requestUUID) {
        requests.put(requestUUID.toString(), System.currentTimeMillis());
    }

    public void removeRequest(UUID requestUUID) {
        requests.remove(requestUUID.toString());
    }

    public boolean haveRequest(UUID requestUUID) {
        return requests.containsKey(requestUUID.toString());
    }

    public boolean areNotifyMessagesEnabled() {
        return notifyMessagesEnabled;
    }

    public void setNotifyMessagesEnabled(boolean notifyMessagesEnabled) {
        this.notifyMessagesEnabled = notifyMessagesEnabled;
    }

    public boolean areFriendRequestsAllowed() {
        return friendRequestsAllowed;
    }

    public void setFriendRequestsAllowed(boolean friendRequestsAllowed) {
        this.friendRequestsAllowed = friendRequestsAllowed;
    }

    public boolean isFriendJumpAllowed() {
        return friendJumpAllowed;
    }

    public void setFriendJumpAllowed(boolean friendJumpAllowed) {
        this.friendJumpAllowed = friendJumpAllowed;
    }

    public boolean canFriendsSeeOnlineStatusAllowed() {
        return friendsSeeOnlineStatusAllowed;
    }

    public void setFriendsSeeOnlineStatusAllowed(boolean friendsSeeOnlineStatusAllowed) {
        this.friendsSeeOnlineStatusAllowed = friendsSeeOnlineStatusAllowed;
    }

    public boolean canFriendsSentMessages() {
        return sentMessagesAllowed;
    }

    public void setSentMessagesAllowed(boolean sentMessagesAllowed) {
        this.sentMessagesAllowed = sentMessagesAllowed;
    }

    public long getLastLogoutTimestamp() {
        return lastLogoutTimestamp;
    }

    public void setLastLogoutTimestamp(long lastLogoutTimestamp) {
        this.lastLogoutTimestamp = lastLogoutTimestamp;
    }
}