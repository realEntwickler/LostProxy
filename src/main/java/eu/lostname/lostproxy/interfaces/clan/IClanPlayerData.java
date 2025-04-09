package eu.lostname.lostproxy.interfaces.clan;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.enums.EClanRole;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.UUID;

public class IClanPlayerData {

    private final String _id;
    private final long timestamp;
    private String clanUid;
    private EClanRole eClanRole;

    public IClanPlayerData(String _id, String clanUid, EClanRole eClanRole, long timestamp) {
        this._id = _id;
        this.clanUid = clanUid;
        this.eClanRole = eClanRole;
        this.timestamp = timestamp;
    }

    public void save() {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_PLAYER_DATA).replaceOne(Filters.eq("_id", _id), LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(this), Document.class), new ReplaceOptions().upsert(true));
    }

    public void delete() {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_PLAYER_DATA).deleteOne(Filters.eq("_id", _id));
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
    }

    public String getClanUid() {
        return clanUid;
    }

    public void setClanUid(String clanUid) {
        this.clanUid = clanUid;
    }

    public EClanRole getClanRole() {
        return eClanRole;
    }

    public void setClanRole(EClanRole eClanRole) {
        this.eClanRole = eClanRole;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
