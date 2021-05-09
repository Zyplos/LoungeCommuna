package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.Utils;
import dev.zyplos.loungecommuna.database.ChunkDAO;
import dev.zyplos.loungecommuna.database.Hikari;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

public class Claim implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ChunkDAO chunkDao = new ChunkDAO(Hikari.getDataSource());

            org.bukkit.Chunk chunk = player.getLocation().getChunk();
            int chunkX = chunk.getX();
            int chunkZ = chunk.getZ();

            dev.zyplos.loungecommuna.database.POJOs.Chunk newClaim = new dev.zyplos.loungecommuna.database.POJOs.Chunk();
            newClaim.setChunk_id(137);
            newClaim.setPlayer_id(player.getUniqueId().toString());
            newClaim.setClaimed_on(new Timestamp(System.currentTimeMillis()));
            newClaim.setX(chunkX);
            newClaim.setZ(chunkZ);
            newClaim.setDimension(player.getWorld().getUID().toString());

            try {
                chunkDao.insert(newClaim);

                final Component tcChunk = Utils.prefixedMessage()
                    .append(Component.text("Claimed chunk"))
                    .append(Component.text(" | ", TextColor.color(0xbababa)))
                    .append(Component.text("X: "))
                    .append(Component.text(chunkX, TextColor.color(0xffa631)))
                    .append(Component.text(" Z: "))
                    .append(Component.text(chunkZ, TextColor.color(0xffa631)));
                player.sendMessage(tcChunk);

                final Component tcAdminChunk = Utils.prefixedMessage()
                    .append(Component.text(player.getName(), NamedTextColor.WHITE))
                    .append(Component.text(" claimed chunk ("))
                    .append(Component.text(chunkX, TextColor.color(0xffa631)))
                    .append(Component.text(","))
                    .append(Component.text(chunkZ, TextColor.color(0xffa631)))
                    .append(Component.text(")."));
                Bukkit.broadcast(tcAdminChunk, "communa.admin");
            } catch (Exception e) {
                player.sendMessage(Utils.prefixedMessage().append(Utils.formatErrorMessage(e.toString())));
            }


        }
        return true;
    }
}
