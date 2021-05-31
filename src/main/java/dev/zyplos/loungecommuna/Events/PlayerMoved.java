package dev.zyplos.loungecommuna.Events;

import dev.zyplos.loungecommuna.LoungeCommuna;
import dev.zyplos.loungecommuna.database.POJOs.Chunk;
import dev.zyplos.loungecommuna.database.POJOs.LogEntry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.Timestamp;

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
            int chunkX = event.getTo().getChunk().getX();
            int chunkZ = event.getTo().getChunk().getZ();
            String chunkDimension = event.getTo().getWorld().getUID().toString();
            String playerUUID = event.getPlayer().getUniqueId().toString();

            plugin.hikariPool.chunkDAO.fetchByCoords(chunkX,
                chunkZ, chunkDimension, chunkOwnerInfo -> {
                    if (!chunkOwnerInfo.isEmpty()) { // this chunk is owned by someone

                        // don't show this messages to players who own the chunk
                        // that would be annoying if people spend most of there time there
                        Chunk ownedChunk = chunkOwnerInfo.get(0);
                        // current player owns current chunk
                        if (ownedChunk.getPlayer_id().equals(playerUUID)) {
                            return;
                        } else {
                            // current player doesnt own current chunk
                            LogEntry logEntry = new LogEntry();
                            logEntry.setX(chunkX);
                            logEntry.setZ(chunkZ);
                            logEntry.setPlayer_id(playerUUID);
                            logEntry.setDimension(chunkDimension);
                            logEntry.setEntered_time(new Timestamp(System.currentTimeMillis()));
                            plugin.hikariPool.logEntryDAO.insert(logEntry);
                        }

                        event.getPlayer().sendActionBar(
                            Component.text().color(TextColor.color(0xffffff))
                                .append(Component.text(chunkX + " , " + chunkZ))
                                .append(Component.text(" | This chunk is owned by "))
                                .append(Component.text(ownedChunk.getName(), TextColor.color(0xffa631)))
                                .append(Component.text("."))
                        );
                    }
                });
        }
    }
}