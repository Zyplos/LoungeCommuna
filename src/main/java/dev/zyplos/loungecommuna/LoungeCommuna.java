package dev.zyplos.loungecommuna;

import dev.zyplos.loungecommuna.Events.PlayerJoined;
import dev.zyplos.loungecommuna.Events.PlayerMoved;
import dev.zyplos.loungecommuna.Events.PlayerResourcePackStatusChanged;
import dev.zyplos.loungecommuna.commands.*;
import dev.zyplos.loungecommuna.database.Hikari;
import dev.zyplos.loungecommuna.internals.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoungeCommuna extends JavaPlugin {
    public Hikari hikariPool;
    public Utils utils;

    @Override
    public void onEnable() {
        hikariPool = new Hikari(this);
        utils = new Utils(this);

        this.getCommand("claim").setExecutor(new Claim(this));
        this.getCommand("devspace").setExecutor(new devspace(this));
        this.getCommand("profile").setExecutor(new Profile(this));
        this.getCommand("chunkinfo").setExecutor(new ChunkInfo(this));
        this.getCommand("sethome").setExecutor(new SetHome(this));
        this.getCommand("home").setExecutor(new Home(this));
        this.getCommand("setcommunity").setExecutor(new SetCommunity(this));
        this.getCommand("unclaim").setExecutor(new Unclaim(this));

        getServer().getPluginManager().registerEvents(new PlayerJoined(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoved(this), this);
        getServer().getPluginManager().registerEvents(new PlayerResourcePackStatusChanged(), this);
        getLogger().info("Enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Done.");
    }
}
