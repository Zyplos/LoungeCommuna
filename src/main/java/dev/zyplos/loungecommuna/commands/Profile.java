package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.UUID;

public class Profile implements CommandExecutor {
    private final LoungeCommuna plugin;

    public Profile(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    private void sendPlayerProfile(Player senderPlayer, String parsedName,
                                   Player onlinePlayer, OfflinePlayer offlinePlayer) {

        plugin.hikariPool.playerDAO.fetchByName(parsedName, resultPlayerList -> {
            String parsedUUID =
                onlinePlayer != null ? onlinePlayer.getUniqueId().toString() : offlinePlayer.getUniqueId().toString();

            dev.zyplos.loungecommuna.database.POJOs.Player resultPlayer = resultPlayerList.get(0);

            int communityId;
            if (resultPlayer.getCommunity_id() == 0) {
                communityId = 99;
            } else {
                communityId = resultPlayer.getCommunity_id();
            }

            Component tcCommunity = Component.text(" ○ " + plugin.utils.getCommunityName(communityId),
                TextColor.fromCSSHexString(plugin.utils.getCommunityBrandColor(communityId)));
            Component tcName = Component.text(parsedName, TextColor.color(0xffffff)).append(tcCommunity);


            Component tcHome;
            if (resultPlayer.getHome_hidden()) {
                tcHome = Component.text(
                    "☗ Home hidden",
                    TextColor.color(plugin.utils.colors.get("muted"))
                );
            } else if (resultPlayer.getHome_dimension() == null) {
                tcHome = Component.text(
                    "☗ No home set",
                    TextColor.color(plugin.utils.colors.get("muted"))
                );
            } else {
                World homeDimension = Bukkit.getWorld(UUID.fromString(resultPlayer.getHome_dimension()));
                tcHome = Component.text(
                    "☗ Home at " + resultPlayer.getHome_x() + ", "
                        + resultPlayer.getHome_y() + ", "
                        + resultPlayer.getHome_z() + " in " + plugin.utils.worldFriendlyNames.get(homeDimension.getName()),
                    TextColor.color(plugin.utils.colors.get("highlight"))
                );
            }


            Component tcOnlineStatus;
            if (onlinePlayer != null) {
                tcOnlineStatus = Component.text("◆ Currently online", TextColor.color(0x2bcd82));
            } else {
                String lastSeenString = DurationFormatUtils.formatPeriod(
                    offlinePlayer.getLastSeen(),
                    System.currentTimeMillis(),
                    "d'days' H'hrs' m'mins' s'sec ago'"
                );
                tcOnlineStatus = Component.text("◆ Last on " + lastSeenString, TextColor.color(0xbababa));
            }

            plugin.hikariPool.chunkDAO.fetchCountByUUID(parsedUUID, numChunks -> {
                Component tcChunkAmount = Component.text(
                    "⧈ " + numChunks + " " + (numChunks == 1 ? "chunk" : "chunks") + " claimed",
                    TextColor.color(0xc194fb));

                final String playerUrl = "https://lounge.haus/mc/player/" + parsedName;
                Component tcUrlPage = Component.text(
                    "⬈ View more details online", TextColor.color(0xa9c8fb))
                    .clickEvent(ClickEvent.openUrl(playerUrl))
                    .hoverEvent(HoverEvent.showText(Component.text("Open URL")));

                final String pixelSpacer = "    ";
                plugin.utils.getPlayerHeadImage(parsedUUID, image -> {
                    if (image == null) {
                        TextComponent output = Component.text().append(
                            tcName,
                            Component.newline(),
                            tcHome,
                            Component.newline(),
                            tcUrlPage,
                            Component.newline(),
                            tcChunkAmount,
                            Component.newline(),
                            tcOnlineStatus
                        ).build();
                        senderPlayer.sendMessage(output);
                        return;
                    }

                    TextComponent.Builder tcPixels = Component.text().append(Component.newline());

                    for (int y = 0; y < image.getHeight(); y++) {
                        for (int x = 0; x < image.getWidth(); x++) {
                            int pixel = image.getRGB(x, y);

                            Color color = new Color(pixel, false);

                            int red = color.getRed();
                            int green = color.getGreen();
                            int blue = color.getBlue();
                            tcPixels.append(Component.text("█", TextColor.color(red, green, blue)));
                        }

                        if (y == 1) {
                            tcPixels
                                .append(Component.text(pixelSpacer))
                                .append(tcName);
                        }
                        if (y == 2) {
                            tcPixels
                                .append(Component.text(pixelSpacer))
                                .append(tcHome);
                        }
                        if (y == 3) {
                            tcPixels
                                .append(Component.text(pixelSpacer))
                                .append(tcUrlPage);
                        }
                        if (y == 5) {
                            tcPixels
                                .append(Component.text(pixelSpacer))
                                .append(tcChunkAmount);
                        }
                        if (y == 6) {
                            tcPixels
                                .append(Component.text(pixelSpacer))
                                .append(tcOnlineStatus);
                        }

                        tcPixels.append(Component.newline());
                    }

                    senderPlayer.sendMessage(tcPixels.build());
                });
            });
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player senderPlayer = (Player) sender;

            final String searchingPlayerString = args.length > 0 ? args[0] : senderPlayer.getName();

            Player onlinePlayer = Bukkit.getPlayer(searchingPlayerString);

            if (onlinePlayer == null) {
                plugin.hikariPool.playerDAO.fetchByName(searchingPlayerString, playerFromDb -> {
                    if (playerFromDb.isEmpty()) {
                        senderPlayer.sendMessage(
                            plugin.utils.prefixedMessage()
                                .append(
                                    Component.text(searchingPlayerString, TextColor.color(0xffa631))
                                )
                                .append(plugin.utils.formatErrorMessage(" has never joined this server."))
                        );
                        return;
                    }

                    UUID uid = UUID.fromString(playerFromDb.get(0).getPlayer_id());
                    OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(uid);
                    String parsedName = playerFromDb.get(0).getName();
                    sendPlayerProfile(senderPlayer, parsedName, null, offlinePlayer);
                });
            } else {
                sendPlayerProfile(senderPlayer, onlinePlayer.getName(), onlinePlayer, null);
            }
        }
        return true;
    }
}