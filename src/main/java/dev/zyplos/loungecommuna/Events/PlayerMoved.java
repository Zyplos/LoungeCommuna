package dev.zyplos.loungecommuna.Events;

import dev.zyplos.loungecommuna.Utils;
import dev.zyplos.loungecommuna.database.ChunkDAO;
import dev.zyplos.loungecommuna.database.Hikari;
import dev.zyplos.loungecommuna.database.POJOs.Chunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class PlayerMoved implements Listener {

    @EventHandler
    public void onPlayerMoved(PlayerMoveEvent event) {
        if (
            !event.getFrom().getChunk().equals(event.getTo().getChunk())
        ) {
            ChunkDAO chunkDao = new ChunkDAO(Hikari.getDataSource());
            List<Chunk> chunkOwnerInfo = chunkDao.fetchByCoords(event.getTo().getChunk().getX(),
                event.getTo().getChunk().getZ());
            if (!chunkOwnerInfo.isEmpty()) {
                event.getPlayer().sendActionBar(
                    Component.text().color(TextColor.color(0xffffff))
                        .append(Component.text("This chunk is owned by "))
                        .append(Component.text(chunkOwnerInfo.get(0).getName(), TextColor.color(0xffa631)))
                        .append(Component.text("."))
                );
            }
        }
    }
}