package eu.lostname.lostproxy.interfaces.clan;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.UUID;

public class IClanInvitation {

    private final String _id;
    private final String clanUid;

    public IClanInvitation(String _id, String clanUid) {
        this._id = _id;
        this.clanUid = clanUid;
    }

    public void save() {
        Gson gson = LostProxy.getInstance().getGson();
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_INVITATIONS).replaceOne(Filters.eq("_id", _id), gson.fromJson(gson.toJson(this), Document.class), new ReplaceOptions().upsert(true));
    }

    public void delete() {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_INVITATIONS).deleteOne(Filters.eq("_id", _id));
    }

    public UUID getUniqueId() {
        return UUID.fromString(_id);
    }

    public String getClanUid() {
        return clanUid;
    }
}
