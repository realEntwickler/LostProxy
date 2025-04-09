package eu.lostname.lostproxy.interfaces.report;

import eu.lostname.lostproxy.enums.EReportState;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class IReport {

    private final ProxiedPlayer invokerProxiedPlayer, targetProxiedPlayer;
    private final IReportReason iReportReason;
    private final ServerInfo reportServerInfo;
    private final long timestamp;
    private ProxiedPlayer responsibleProxiedPlayer;
    private EReportState eReportState;

    public IReport(ProxiedPlayer invokerProxiedPlayer, ProxiedPlayer targetProxiedPlayer, IReportReason iReportReason) {
        this.invokerProxiedPlayer = invokerProxiedPlayer;
        this.targetProxiedPlayer = targetProxiedPlayer;
        this.reportServerInfo = targetProxiedPlayer.getServer().getInfo();
        this.iReportReason = iReportReason;
        this.timestamp = System.currentTimeMillis();
        this.responsibleProxiedPlayer = null;
        this.eReportState = EReportState.OPEN;
    }

    public ProxiedPlayer getInvokerProxiedPlayer() {
        return invokerProxiedPlayer;
    }

    public ProxiedPlayer getTargetProxiedPlayer() {
        return targetProxiedPlayer;
    }

    public ProxiedPlayer getResponsibleProxiedPlayer() {
        return responsibleProxiedPlayer;
    }

    public void setResponsibleProxiedPlayer(ProxiedPlayer responsibleProxiedPlayer) {
        this.responsibleProxiedPlayer = responsibleProxiedPlayer;
    }

    public IReportReason getReportReason() {
        return iReportReason;
    }

    public ServerInfo getReportServerInfo() {
        return reportServerInfo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public EReportState getReportState() {
        return eReportState;
    }

    public void setReportState(EReportState eReportState) {
        this.eReportState = eReportState;
    }
}
