package dev.zyplos.loungecommuna;

import dev.zyplos.loungecommuna.commands.Claim;
import io.github.cdimascio.dotenv.Dotenv;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoungeCommuna extends JavaPlugin {
    final Dotenv dotenv = Dotenv.load();
    final String dbUsername = dotenv.get("DB_USERNAME");
    final String dbPassword = dotenv.get("DB_PASSWORD");
    final String dbUrl = dotenv.get("DB_URL");

    @Override
    public void onEnable() {
        getLogger().info("Enabled.");

        this.getCommand("claim").setExecutor(new Claim());
    }

    @Override
    public void onDisable() {
        getLogger().info("Done.");
    }
}
