package eu.lostname.lostproxy.manager;

import com.mongodb.client.model.Filters;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.utils.MongoCollection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

import java.util.HashMap;
import java.util.UUID;

public class SettingsManager {

    private final HashMap<UUID, Document> cacheData;

    public SettingsManager() {
        this.cacheData = new HashMap<>();
    }

    public boolean allowPartyInvitations(UUID uniqueId) {
        if (!cacheData.containsKey(uniqueId)) {
            setupDefaultValues(uniqueId);
        }
        return cacheData.get(uniqueId).getBoolean("partyInvitations");
    }

    public void updatePartyInvitations(ProxiedPlayer proxiedPlayer) {
        if (!cacheData.containsKey(proxiedPlayer.getUniqueId())) {
            setupDefaultValues(proxiedPlayer.getUniqueId());
        }

        cacheData.replace(proxiedPlayer.getUniqueId(), cacheData.get(proxiedPlayer.getUniqueId()).append("partyInvitations", !cacheData.get(proxiedPlayer.getUniqueId()).getBoolean("partyInvitations")));
    }

    public boolean allowClanInvitations(UUID uniqueId) {
        if (!cacheData.containsKey(uniqueId)) {
            setupDefaultValues(uniqueId);
        }

        return cacheData.get(uniqueId).getBoolean("clanInvitations");
    }

    public void updateClanInvitations(ProxiedPlayer proxiedPlayer) {
        if (!cacheData.containsKey(proxiedPlayer.getUniqueId())) {
            setupDefaultValues(proxiedPlayer.getUniqueId());
        }

        cacheData.replace(proxiedPlayer.getUniqueId(), cacheData.get(proxiedPlayer.getUniqueId()).append("clanInvitations", !cacheData.get(proxiedPlayer.getUniqueId()).getBoolean("clanInvitations")));
    }

    public Document setupDefaultValues(UUID uniqueId) {
        Document document = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.PLAYER_SETTINGS).find(Filters.eq("_id", uniqueId.toString())).first();

        if (document == null) {
            document = new Document("_id", uniqueId.toString()).append("partyInvitations", true).append("clanInvitations", true);
            LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.PLAYER_SETTINGS).insertOne(document);
        }

        cacheData.put(uniqueId, document);

        return document;
    }

    public HashMap<UUID, Document> getCacheData() {
        return cacheData;
    }
}