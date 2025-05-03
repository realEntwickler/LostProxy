package eu.lostname.lostproxy.manager;

import com.google.gson.Gson;
import eu.lostname.lostproxy.LostProxy;
import eu.lostname.lostproxy.interfaces.report.IReport;
import eu.lostname.lostproxy.interfaces.report.IReportReason;
import eu.lostname.lostproxy.utils.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class ReportManager {

    private final Gson gson;
    private final Collection<IReportReason> reportReasons;
    private final Collection<IReport> reports;

    public ReportManager(Gson gson) {
        this.gson = gson;
        this.reportReasons = new ArrayList<>();
        this.reports = new ArrayList<>();

        loadReportReasons();
    }

    public IReportReason getReportReasonByID(int id) {
        return reportReasons.stream().filter(iReportReason -> iReportReason.getID() == id).findFirst().orElse(null);
    }

    public void loadReportReasons() {
        reportReasons.clear();
        LostProxy.getInstance().getDatabase().getMongoDatabase().getCollection(MongoCollection.REPORT_REASONS).find().forEach((Consumer<? super Document>) document -> reportReasons.add(gson.fromJson(document.toJson(), IReportReason.class)));
    }

    public Collection<IReportReason> getReportReasons() {
        return reportReasons;
    }

    public Collection<IReport> getReports() {
        return reports;
    }
}
