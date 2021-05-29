package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import dev.zyplos.loungecommuna.database.POJOs.Chunk;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class Unclaim implements CommandExecutor {
    private final LoungeCommuna plugin;

    public Unclaim(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            plugin.hikariPool.chunkDAO.fetchByCoords(player.getLocation().getChunk().getX(),
                player.getLocation().getChunk().getZ(), chunkOwnerInfo -> {
                    if (chunkOwnerInfo.isEmpty()) {
                        player.sendMessage(
                            plugin.utils.prefixedMessage().append(
                                plugin.utils.formatErrorMessage("You don't own this chunk.")
                            )
                        );
                        return;
                    }

                    Chunk resultChunk = chunkOwnerInfo.get(0);

                    if (
                        resultChunk.getPlayer_id().equals(player.getUniqueId().toString()) &&
                            resultChunk.getDimension().equals(player.getWorld().getUID().toString())
                    ) {
                        plugin.hikariPool.chunkDAO.deleteChunk(
                            player.getLocation().getChunk().getX(),
                            player.getLocation().getChunk().getZ(),
                            player.getWorld().getUID().toString(),
                            player.getUniqueId().toString()
                        );
                        // player owns chunk and chunk is in the same dimension
                        player.sendMessage(
                            plugin.utils.prefixedMessage().append(
                                Component.text("You have unclaimed this chunk. Others can claim this chunk now.")
                            )
                        );
                    } else {
                        player.sendMessage(
                            plugin.utils.prefixedMessage().append(
                                plugin.utils.formatErrorMessage("You don't own this chunk.")
                            )
                        );
                    }
                });
        }
        return true;
    }
}
