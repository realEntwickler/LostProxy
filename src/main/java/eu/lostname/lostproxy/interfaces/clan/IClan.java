package eu.lostname.lostproxy.interfaces.clan;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

public class IClan {

    private String _id;
    private String name;
    private String tag;
    private String tagColor;
    private int maxSize;

    public IClan(String _id, String name, String tag, String tagColor, int maxSize) {
        this._id = _id;
        this.name = name;
        this.tag = tag;
        this.tagColor = tagColor;
        this.maxSize = maxSize;
    }

    public void save() {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_DATA).replaceOne(Filters.eq("_id", _id), LostProxy.getInstance().getGson().fromJson(LostProxy.getInstance().getGson().toJson(this), Document.class), new ReplaceOptions().upsert(true));
    }

    public void delete() {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.CLAN_DATA).deleteOne(Filters.eq("_id", _id));
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
