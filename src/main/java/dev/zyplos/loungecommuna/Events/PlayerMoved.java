package dev.zyplos.loungecommuna.Events;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoved implements Listener {
    private final LoungeCommuna plugin;

    public PlayerMoved(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMoved(PlayerMoveEvent event) {

        // detects if player changed chunk
        if (
            !event.getFrom().getChunk().equals(event.getTo().getChunk())
        ) {
            plugin.hikariPool.chunkDAO.fetchByCoords(event.getTo().getChunk().getX(),
                event.getTo().getChunk().getZ(), chunkOwnerInfo -> {
                    if (!chunkOwnerInfo.isEmpty()) {
                        event.getPlayer().sendActionBar(
                            Component.text().color(TextColor.color(0xffffff))
                                .append(Component.text(event.getTo().getChunk().getX() + " , " + event.getTo().getChunk().getZ()))
                                .append(Component.text(" | This chunk is owned by "))
                                .append(Component.text(chunkOwnerInfo.get(0).getName(), TextColor.color(0xffa631)))
                                .append(Component.text("."))
                        );
                    }
                });
        }
    }
}