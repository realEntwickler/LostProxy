/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 15.01.2021 @ 00:03:35
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * LostProxy.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package eu.lostname.lostproxy;

import com.google.gson.Gson;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.modules.syncproxy.SyncProxyManagement;
import eu.lostname.lostproxy.commands.*;
import eu.lostname.lostproxy.database.LostProxyDatabase;
import eu.lostname.lostproxy.listener.*;
import eu.lostname.lostproxy.manager.*;
import eu.lostname.lostproxy.utils.CloudServices;
import eu.lostname.lostproxy.utils.Property;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class LostProxy extends Plugin {

    private static LostProxy instance;

    private final LostProxyDatabase database;
    private final Gson gson;
    //private LinkageManager linkageManager;
    private final PlayerManager playerManager;
    private final HistoryManager historyManager;
    private final TeamManager teamManager;
    private final BanManager banManager;
    private final MuteManager muteManager;
    private final ReasonManager reasonManager;
    private final FriendManager friendManager;
    private final PartyManager partyManager;
    private final ClanManager clanManager;
    private final SettingsManager settingsManager;
    private final ReportManager reportManager;
    private final Property property;
    private ScheduledTask restartTask;

    public LostProxy () {
        instance = this;

        property = new Property();
        property.setDefaultProps();

        this.gson = new Gson();
        this.database = new LostProxyDatabase(property.get("cfg", "db.database"), property.get("cfg", "db.username"), property.get("cfg", "db.password"));
        //this.linkageManager = new LinkageManager(gson);
        this.playerManager = new PlayerManager();
        this.historyManager = new HistoryManager();
       // this.teamSpeakManager = new TeamSpeakManager();
        this.teamManager = new TeamManager();
        this.banManager = new BanManager();
        this.muteManager = new MuteManager();
        this.reasonManager = new ReasonManager(gson, database);
        this.friendManager = new FriendManager(database, gson);
        this.partyManager = new PartyManager();
        this.clanManager = new ClanManager();
        this.settingsManager = new SettingsManager();
        this.reportManager = new ReportManager(gson);

        //getProxy().getPluginManager().registerCommand(this, new TSCommand("ts", "lostproxy.command.ts"));
        getProxy().getPluginManager().registerCommand(this, new PingCommand("ping", "lostproxy.command.ping"));
        getProxy().getPluginManager().registerCommand(this, new KickCommand("kick", "lostproxy.command.kick"));
        getProxy().getPluginManager().registerCommand(this, new NotifyCommand("notify", "lostproxy.command.notify", "benachrichtigung"));
        getProxy().getPluginManager().registerCommand(this, new KickHistoryClearCommand("kickhistoryclear", "lostproxy.command.kickhistoryclear", "khc", "khclear"));
        getProxy().getPluginManager().registerCommand(this, new KickHistoryCommand("kickhistory", "lostproxy.command.kickhistory", "kh"));
        getProxy().getPluginManager().registerCommand(this, new TCCommand("tc", "lostproxy.command.tc", "teamchat"));
        getProxy().getPluginManager().registerCommand(this, new TeamCommand("team", "lostproxy.command.team"));
        getProxy().getPluginManager().registerCommand(this, new BanReasonsCommand("banreasons", "lostproxy.command.banreasons", "br"));
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand("unban", "lostproxy.command.unban", "ub"));
        getProxy().getPluginManager().registerCommand(this, new BanHistoryCommand("banhistory", "lostproxy.command.banhistory", "bh"));
        getProxy().getPluginManager().registerCommand(this, new BanHistoryClearCommand("banhistoryclear", "lostproxy.command.banhistoryclear", "bhc", "bhclear"));
        getProxy().getPluginManager().registerCommand(this, new BanCommand("ban", "lostproxy.command.ban", "b"));
        getProxy().getPluginManager().registerCommand(this, new EACommand("ea", "lostproxy.command.ea"));
        getProxy().getPluginManager().registerCommand(this, new UnmuteCommand("unmute", "lostproxy.command.unmute"));
        getProxy().getPluginManager().registerCommand(this, new MuteCommand("mute", "lostproxy.command.mute"));
        getProxy().getPluginManager().registerCommand(this, new MuteReasonsCommand("mutereasons", "lostproxy.command.mutereasons", "mr"));
        getProxy().getPluginManager().registerCommand(this, new MuteHistoryCommand("mutehistory", "lostproxy.command.mutehistory", "mh"));
        getProxy().getPluginManager().registerCommand(this, new MuteHistoryClearCommand("mutehistoryclear", "lostproxy.command.mutehistoryclear", "mhclear", "mhc"));
        getProxy().getPluginManager().registerCommand(this, new FriendCommand("friend", "", "friends"));
        getProxy().getPluginManager().registerCommand(this, new HelpCommand("help"));
        getProxy().getPluginManager().registerCommand(this, new BroadcastCommand("broadcast", "lostproxy.command.broadcast", "bc"));
        getProxy().getPluginManager().registerCommand(this, new PartyCommand("party"));
        getProxy().getPluginManager().registerCommand(this, new PartyChatCommand("partychat", "", "pc"));
        getProxy().getPluginManager().registerCommand(this, new ClanCommand("clan", "", "clans"));
        getProxy().getPluginManager().registerCommand(this, new ClanChatCommand("clanchat", "", "cc"));
        getProxy().getPluginManager().registerCommand(this, new RestartCommand("restart", "lostproxy.command.restart", ""));
        getProxy().getPluginManager().registerCommand(this, new ReportCommand("report"));
        getProxy().getPluginManager().registerCommand(this, new ReportsCommand("reports", "lostproxy.command.reports", ""));

        getProxy().getPluginManager().registerListener(this, new PostLoginListener());
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener());
        getProxy().getPluginManager().registerListener(this, new PreLoginListener());
        getProxy().getPluginManager().registerListener(this, new ChatListener());
        getProxy().getPluginManager().registerListener(this, new ServerSwitchListener());

        this.restartTask = null;
    }

    @Override
    public void onEnable()
    {
        CloudServices.LUCKPERMS = LuckPermsProvider.get();
        CloudServices.SYNCPROXY_MANAGEMENT = InjectionLayer.ext().instance(ServiceRegistry.class).firstProvider(SyncProxyManagement.class);
        CloudServices.PLAYER_MANAGER = InjectionLayer.ext().instance(ServiceRegistry.class).firstProvider(eu.cloudnetservice.modules.bridge.player.PlayerManager.class);
    }

    @Override
    public void onDisable() {
        database.getMongoClient().close();
        //getTeamSpeakManager().getTs3Query().exit();
    }

    public String formatArrayToString(int startIndex, String[] strings) {
        StringBuilder msg = new StringBuilder();
        for (int i = startIndex; i < strings.length; i++) {
            if (i == (strings.length - 1)) {
                msg.append(strings[i]);
            } else
                msg.append(strings[i]).append(" ");
        }
        return msg.toString();
    }

    public BanManager getBanManager() {
        return banManager;
    }


//    public TeamSpeakManager getTeamSpeakManager() {
//        return teamSpeakManager;
//    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Gson getGson() {
        return gson;
    }

    public LostProxyDatabase getDatabase() {
        return database;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public Property getProperty() {
        return property;
    }

    public ReasonManager getReasonManager() {
        return reasonManager;
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public ScheduledTask getRestartTask() {
        return restartTask;
    }

    public void setRestartTask(ScheduledTask restartTask) {
        this.restartTask = restartTask;
    }

    public static LostProxy getInstance() {
        return instance;
    }
}
