package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.Utils;
import dev.zyplos.loungecommuna.database.ChunkDAO;
import dev.zyplos.loungecommuna.database.Hikari;
import dev.zyplos.loungecommuna.database.POJOs.Chunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class devspace implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            final Component tcList = Utils.prefixedMessage()
                .append(Component.text(" Listing chunks in database: "));
            player.sendMessage(tcList);

            ChunkDAO chunkDao = new ChunkDAO(Hikari.getDataSource());
            List<Chunk> list = chunkDao.fetchAll();
            list.forEach(
                item ->
                    player.sendMessage(
                        Component.text(item.getChunk_id() + " | " +
                            item.getName() + " (" + item.getPlayer_id() + ")" +
                            " | X: " + item.getX() + " Z: " + item.getZ(), TextColor.color(0xc2d9ff)
                        )
                    )
            );

            player.sendMessage(player.getWorld().getUID().toString());
        }
        return true;
    }
}
