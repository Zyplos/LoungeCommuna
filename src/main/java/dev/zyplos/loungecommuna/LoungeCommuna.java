package dev.zyplos.loungecommuna;

import dev.zyplos.loungecommuna.commands.Claim;
import dev.zyplos.loungecommuna.commands.devspace;
import dev.zyplos.loungecommuna.database.Hikari;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoungeCommuna extends JavaPlugin {
    @Override
    public void onEnable() {
        Hikari.init();

        this.getCommand("claim").setExecutor(new Claim());
        this.getCommand("devspace").setExecutor(new devspace());

        getLogger().info("Enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Done.");
    }
}
