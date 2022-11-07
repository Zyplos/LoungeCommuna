package dev.zyplos.loungecommuna.Events;

import dev.zyplos.loungecommuna.LoungeCommuna;
import dev.zyplos.loungecommuna.database.POJOs.Chunk;
import dev.zyplos.loungecommuna.database.POJOs.LogEntry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.Timestamp;
import java.util.UUID;

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
            UUID chunkDimension = event.getTo().getWorld().getUID();
            UUID playerUUID = event.getPlayer().getUniqueId();

            plugin.hikariPool.chunkDAO.fetchByCoords(chunkX,
                chunkZ, chunkDimension, chunkOwnerInfo -> {
                    if (!chunkOwnerInfo.isEmpty()) { // this chunk is owned by someone
                        plugin.taskManager.stopTask(event.getPlayer(), false);
                        // don't show this messages to players who own the chunk
                        // that would be annoying for people who spend most of their time in their own chunks
                        Chunk ownedChunk = chunkOwnerInfo.get(0);
                        // current player owns current chunk
                        if (ownedChunk.getPlayer_id().equals(playerUUID)) {
                            // player actually owns chunk but message shouldn't show
                            plugin.taskManager.stopTask(event.getPlayer(), true);
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

                        plugin.taskManager.addTask(event.getPlayer(), ownedChunk.getName());
                    } else {
                        plugin.taskManager.stopTask(event.getPlayer(), true);
                    }
                });
        }
    }
}