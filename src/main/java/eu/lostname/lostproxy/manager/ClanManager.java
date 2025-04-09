package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.clan.IClan;
import eu.lostname.lostproxy.interfaces.clan.IClanInvitation;
import eu.lostname.lostproxy.interfaces.clan.IClanPlayerData;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ClanManager {

    public IClanPlayerData getClanPlayerData(UUID uniqueId) {
        Gson gson = LostProxy.getInstance().getGson();
        Document document = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_PLAYER_DATA).find(Filters.eq("_id", uniqueId.toString())).first();

        return document != null ? gson.fromJson(document.toJson(), IClanPlayerData.class) : null;
    }

    public IClan getClanByUniqueId(String uniqueId) {
        Gson gson = LostProxy.getInstance().getGson();
        Document document = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_DATA).find(Filters.eq("_id", uniqueId)).first();

        return document != null ? gson.fromJson(document.toJson(), IClan.class) : null;
    }

    public IClan getClanByName(String name) {
        Gson gson = LostProxy.getInstance().getGson();
        Document document = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_DATA).find(Filters.eq("name", name)).first();

        return document != null ? gson.fromJson(document.toJson(), IClan.class) : null;
    }

    public IClan getClanByTag(String tag) {
        Gson gson = LostProxy.getInstance().getGson();
        Document document = LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_DATA).find(Filters.eq("tag", tag)).first();

        return document != null ? gson.fromJson(document.toJson(), IClan.class) : null;
    }

    public List<IClanPlayerData> getClanPlayerDatasFromClan(String clanUniqueId) {
        Gson gson = LostProxy.getInstance().getGson();
        List<IClanPlayerData> playerDatas = new ArrayList<>();

        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_PLAYER_DATA).find(Filters.eq("clanUid", clanUniqueId)).forEach((Consumer<? super Document>) one -> playerDatas.add(gson.fromJson(one.toJson(), IClanPlayerData.class)));

        return playerDatas;
    }

    public List<IClanInvitation> getClanInvitationsByPlayer(UUID uniqueId) {
        List<IClanInvitation> invitations = new ArrayList<>();
        Gson gson = LostProxy.getInstance().getGson();
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_INVITATIONS).find(Filters.eq("_id", uniqueId.toString())).forEach((Consumer<? super Document>) one -> invitations.add(gson.fromJson(one.toJson(), IClanInvitation.class)));

        return invitations;
    }
}
