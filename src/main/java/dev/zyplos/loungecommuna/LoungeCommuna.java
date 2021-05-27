package dev.zyplos.loungecommuna;

import dev.zyplos.loungecommuna.Events.PlayerJoined;
import dev.zyplos.loungecommuna.Events.PlayerMoved;
import dev.zyplos.loungecommuna.Events.PlayerResourcePackStatusChanged;
import dev.zyplos.loungecommuna.commands.ChunkInfo;
import dev.zyplos.loungecommuna.commands.Claim;
import dev.zyplos.loungecommuna.commands.Profile;
import dev.zyplos.loungecommuna.commands.devspace;
import dev.zyplos.loungecommuna.database.Hikari;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoungeCommuna extends JavaPlugin {
    public Hikari hikariPool;
    public Utils utils;

    @Override
    public void onEnable() {
        hikariPool = new Hikari();
        utils = new Utils();

        this.getCommand("claim").setExecutor(new Claim(this));
        this.getCommand("devspace").setExecutor(new devspace(this));
        this.getCommand("profile").setExecutor(new Profile(this));
        this.getCommand("chunkinfo").setExecutor(new ChunkInfo(this));

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
