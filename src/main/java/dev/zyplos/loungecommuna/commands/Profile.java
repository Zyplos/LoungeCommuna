package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.Utils;
import dev.zyplos.loungecommuna.database.ChunkDAO;
import dev.zyplos.loungecommuna.database.Hikari;
import dev.zyplos.loungecommuna.database.PlayerDAO;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class Profile implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player senderPlayer = (Player) sender;

            String searchingPlayerString = senderPlayer.getName();

            if (args.length > 0) {
                searchingPlayerString = args[0];
            }

            Player onlinePlayer = Bukkit.getPlayer(searchingPlayerString);
            OfflinePlayer offlinePlayer = null;
            String parsedName = searchingPlayerString;

            if (onlinePlayer == null) {
                PlayerDAO playerDAO = new PlayerDAO(Hikari.getDataSource());
                List<dev.zyplos.loungecommuna.database.POJOs.Player> playerFromDb = playerDAO.fetchByName(searchingPlayerString);

                if (playerFromDb.isEmpty()) {
                    senderPlayer.sendMessage(
                        Utils.prefixedMessage()
                            .append(
                                Component.text(parsedName, TextColor.color(0xffa631))
                            )
                            .append(Utils.formatErrorMessage(" has never joined this server."))
                    );
                    return true;
                }

                UUID uid = UUID.fromString(playerFromDb.get(0).getPlayer_id());
                offlinePlayer = Bukkit.getServer().getOfflinePlayer(uid);
                parsedName = playerFromDb.get(0).getName();
            } else {
                parsedName = onlinePlayer.getName();
            }

            String parsedUUID =
                onlinePlayer != null ? onlinePlayer.getUniqueId().toString() : offlinePlayer.getUniqueId().toString();

            Component tcName = Component.text(parsedName, TextColor.color(0xffffff));

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

            ChunkDAO chunkDAO = new ChunkDAO(Hikari.getDataSource());
            int numChunks = chunkDAO.fetchCountByUUID(parsedUUID);

            Component tcChunkAmount = Component.text(
                "⧈ " + numChunks + " " + (numChunks == 1 ? "chunk" : "chunks") + " claimed",
                TextColor.color(0xc194fb));

            final String playerUrl = "https://lounge.haus/mc/profile/" + parsedUUID;
            Component tcUrlPage = Component.text(
                "⬈ View more details online", TextColor.color(0xa9c8fb))
                .clickEvent(ClickEvent.openUrl(playerUrl))
                .hoverEvent(HoverEvent.showText(Component.text("Open URL")));

            final String pixelSpacer = "    ";
            try {
                URL url = new URL("https://crafatar.com/avatars/" + parsedUUID + ".png?size=8&overlay");
                BufferedImage image = ImageIO.read(url);

                TextComponent.Builder tcPixels = Component.text().append(Component.newline());

                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int pixel = image.getRGB(x, y);
                        //Creating a Color object from pixel value
                        Color color = new Color(pixel, false);
                        //Retrieving the R G B values
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
            } catch (IOException e) {
                TextComponent output = Component.text().append(
                    tcName,
                    Component.newline(),
                    tcUrlPage,
                    Component.newline(),
                    tcChunkAmount,
                    Component.newline(),
                    tcOnlineStatus
                ).build();
                senderPlayer.sendMessage(output);
            }
        }
        return true;
    }
}