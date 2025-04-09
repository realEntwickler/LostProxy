package eu.lostname.lostproxy.interfaces.report;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.IReason;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

public class IReportReason {

    private Integer _id;
    private String name;
    private String description;
    private IReason iReason;

    public IReportReason(Integer _id, String name, String description, IReason iReason) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.iReason = iReason;
    }

    public void save() {
        Gson gson = LostProxy.getInstance().getGson();
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.REPORT_REASONS).replaceOne(Filters.eq("_id", _id), gson.fromJson(gson.toJson(this), Document.class), new ReplaceOptions().upsert(true));
    }

    public void delete() {
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.REPORT_REASONS).deleteOne(Filters.eq("_id", _id));
    }

    public Integer getID() {
        return _id;
    }

    public void setID(Integer _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IReason getiReason() {
        return iReason;
    }

    public void setiReason(IReason iReason) {
        this.iReason = iReason;
    }
}
