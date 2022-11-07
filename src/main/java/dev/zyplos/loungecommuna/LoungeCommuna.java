package dev.zyplos.loungecommuna;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import dev.zyplos.loungecommuna.Events.PlayerJoined;
import dev.zyplos.loungecommuna.Events.PlayerLeft;
import dev.zyplos.loungecommuna.Events.PlayerMoved;
import dev.zyplos.loungecommuna.Events.PlayerResourcePackStatusChanged;
import dev.zyplos.loungecommuna.commands.*;
import dev.zyplos.loungecommuna.database.Hikari;
import dev.zyplos.loungecommuna.internals.ScrunchTaskManager;
import dev.zyplos.loungecommuna.internals.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class LoungeCommuna extends JavaPlugin {
    public Hikari hikariPool;
    public Utils utils;
    public ScrunchTaskManager taskManager;
    public MarkerAPI markerApi;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        hikariPool = new Hikari(this);
        utils = new Utils(this);
        taskManager = new ScrunchTaskManager(this);
        BlueMapAPI.onEnable(api -> {
            try {
                markerApi = api.getMarkerAPI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        this.getCommand("claim").setExecutor(new Claim(this));
        this.getCommand("devspace").setExecutor(new devspace(this));
        this.getCommand("profile").setExecutor(new Profile(this));
        this.getCommand("chunkinfo").setExecutor(new ChunkInfo(this));
        this.getCommand("sethome").setExecutor(new SetHome(this));
        this.getCommand("home").setExecutor(new Home(this));
        this.getCommand("setcommunity").setExecutor(new SetCommunity(this));
        this.getCommand("unclaim").setExecutor(new Unclaim(this));
        this.getCommand("list").setExecutor(new List(this));
        this.getCommand("help").setExecutor(new Help(this));
        this.getCommand("visitorslog").setExecutor(new VisitorsLog(this));
        this.getCommand("dimdump").setExecutor(new DimDump(this));
        this.getCommand("hidehome").setExecutor(new HideHome(this));
        this.getCommand("showhome").setExecutor(new ShowHome(this));

        getServer().getPluginManager().registerEvents(new PlayerJoined(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoved(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeft(this), this);
        getServer().getPluginManager().registerEvents(new PlayerResourcePackStatusChanged(), this);
        getLogger().info("Enabled.");
    }

    @Override
    public void onDisable() {
        hikariPool.closeConnection();
        getLogger().info("Disabled.");
    }
}
