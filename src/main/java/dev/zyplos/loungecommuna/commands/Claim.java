package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
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
    private final LoungeCommuna plugin;

    public Claim(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            org.bukkit.Chunk chunk = player.getLocation().getChunk();
            int chunkX = chunk.getX();
            int chunkZ = chunk.getZ();

            dev.zyplos.loungecommuna.database.POJOs.Chunk newClaim = new dev.zyplos.loungecommuna.database.POJOs.Chunk();
            newClaim.setChunk_id(137);
            newClaim.setPlayer_id(player.getUniqueId());
            newClaim.setClaimed_on(new Timestamp(System.currentTimeMillis()));
            newClaim.setX(chunkX);
            newClaim.setZ(chunkZ);
            newClaim.setDimension(player.getWorld().getUID());

            plugin.hikariPool.chunkDAO.insert(newClaim, result -> {
                if (result == null) {
                    final Component tcChunk = plugin.utils.prefixedMessage()
                        .append(Component.text("Claimed chunk"))
                        .append(Component.text(" | ", TextColor.color(0xbababa)))
                        .append(Component.text("X: "))
                        .append(Component.text(chunkX, TextColor.color(0xffa631)))
                        .append(Component.text(" Z: "))
                        .append(Component.text(chunkZ, TextColor.color(0xffa631)));
                    player.sendMessage(tcChunk);

                    final Component tcAdminChunk = plugin.utils.prefixedMessage()
                        .append(Component.text(player.getName(), NamedTextColor.WHITE))
                        .append(Component.text(" claimed chunk ("))
                        .append(Component.text(chunkX, TextColor.color(0xffa631)))
                        .append(Component.text(","))
                        .append(Component.text(chunkZ, TextColor.color(0xffa631)))
                        .append(Component.text(")."));
                    Bukkit.broadcast(tcAdminChunk, "communa.admin");
                    return;
                }

                player.sendMessage(
                    plugin.utils.prefixedMessage().append(
                        plugin.utils.formatErrorMessage(result)
                    )
                );
            });
        }
        return true;
    }
}
