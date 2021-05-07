package dev.zyplos.loungecommuna;

import io.github.cdimascio.dotenv.Dotenv;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoungeCommuna extends JavaPlugin {

    @Override
    public void onEnable() {
        Dotenv dotenv = Dotenv.load();
        getLogger().info("Watching as " + dotenv.get("DB_USERNAME"));
    }

    @Override
    public void onDisable() {
        getLogger().info("Done.");
    }
}
