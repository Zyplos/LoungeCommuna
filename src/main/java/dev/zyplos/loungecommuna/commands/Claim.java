package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Claim implements @Nullable CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Chunk chunk = player.getLocation().getChunk();
            int chunkX = chunk.getX();
            int chunkZ = chunk.getZ();

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
        }
        return true;
    }
}
