package dev.zyplos.loungecommuna;

import dev.zyplos.loungecommuna.Events.PlayerJoined;
import dev.zyplos.loungecommuna.Events.PlayerMoved;
import dev.zyplos.loungecommuna.commands.ChunkInfo;
import dev.zyplos.loungecommuna.commands.Claim;
import dev.zyplos.loungecommuna.commands.Profile;
import dev.zyplos.loungecommuna.commands.devspace;
import dev.zyplos.loungecommuna.database.Hikari;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoungeCommuna extends JavaPlugin {
    @Override
    public void onEnable() {
        Hikari.init();

        this.getCommand("claim").setExecutor(new Claim());
        this.getCommand("devspace").setExecutor(new devspace());
        this.getCommand("profile").setExecutor(new Profile());
        this.getCommand("chunkinfo").setExecutor(new ChunkInfo());

        getServer().getPluginManager().registerEvents(new PlayerJoined(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoved(), this);
        getLogger().info("Enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Done.");
    }
}
