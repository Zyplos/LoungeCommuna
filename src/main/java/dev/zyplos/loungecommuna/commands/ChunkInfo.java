package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChunkInfo implements CommandExecutor {
    private final LoungeCommuna plugin;

    public ChunkInfo(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            plugin.hikariPool.chunkDAO.fetchByCoords(player.getChunk().getX(),
                player.getChunk().getZ(), player.getWorld().getUID(), chunkOwnerInfo -> {
                    if (!chunkOwnerInfo.isEmpty()) {
                        String claimedString = DurationFormatUtils.formatPeriod(
                            chunkOwnerInfo.get(0).getClaimed_on().getTime(),
                            System.currentTimeMillis(),
                            "d' days,' H' hrs,' m' mins, and' s' secs'"
                        );

                        player.sendMessage(
                            plugin.utils.prefixedMessage()
                                .append(Component.text("This chunk is owned by "))
                                .append(Component.text(chunkOwnerInfo.get(0).getName(), TextColor.color(0xffa631)))
                                .append(Component.text(". They claimed it "))
                                .append(Component.text(claimedString, TextColor.color(0xffa631)))
                                .append(Component.text(" ago. "))
                                .append(Component.text(
                                        "Click here", TextColor.color(0xa9c8fb))
                                    .clickEvent(ClickEvent.runCommand("/profile " + chunkOwnerInfo.get(0).getName()))
                                    .hoverEvent(HoverEvent.showText(Component.text("Show " + chunkOwnerInfo.get(0).getName() + "'s profile")))
                                )
                                .append(Component.text(" to see their profile or do "))
                                .append(Component.text("/profile " + chunkOwnerInfo.get(0).getName(), NamedTextColor.GREEN))
                                .append(Component.text("."))
                        );
                    } else {
                        player.sendMessage(
                            plugin.utils.prefixedMessage()
                                .append(Component.text("No one has claimed this chunk. Make it yours by doing "))
                                .append(Component.text("/claim", NamedTextColor.GREEN))
                                .append(Component.text("."))
                        );
                    }
                });
        }
        return true;
    }
}