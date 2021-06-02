package dev.zyplos.loungecommuna.internals;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

// temp fix for chunk messages people keep pointing out

public class ScrunchTaskManager {
    private final LoungeCommuna plugin;
    private Map<String, Integer> tasks;

    public ScrunchTaskManager(LoungeCommuna plugin) {
        this.tasks = new HashMap<>();
        this.plugin = plugin;
    }

    public void addTask(Player player, String ownerName) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendActionBar(
                    Component.text().color(TextColor.color(0xffffff))
                        .append(Component.text("Currently in "))
                        .append(Component.text(ownerName, TextColor.color(0xffa631)))
                        .append(Component.text("'s territory"))
                );
                tasks.put(player.getUniqueId().toString(), this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, 20 * 2);
    }

    public void stopTask(Player player, boolean unownedChunk) {
        if (unownedChunk) player.sendActionBar("   ");
        String playerUUID = player.getUniqueId().toString();

        if (!tasks.containsKey(playerUUID)) {
            return;
        }

        int id = tasks.get(playerUUID);
        Bukkit.getScheduler().cancelTask(id);
        tasks.remove(playerUUID);
    }

}
