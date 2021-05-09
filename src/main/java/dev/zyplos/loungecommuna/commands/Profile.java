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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class Profile implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player senderPlayer = (Player) sender;

            if (args.length == 0) {
                senderPlayer.sendMessage(
                    Utils.prefixedMessage().append(
                        Utils.formatErrorMessage("Seems you're missing a username. Do ")
                            .append(Component.text("/profile <username>", NamedTextColor.GREEN))
                    )
                );
                return true;
            }

            Player onlinePlayer = Bukkit.getPlayer(args[0]);
            OfflinePlayer offlinePlayer = null;
            String parsedName = args[0];

            if (onlinePlayer == null) {
                PlayerDAO playerDAO = new PlayerDAO(Hikari.getDataSource());
                List<dev.zyplos.loungecommuna.database.POJOs.Player> playerFromDb = playerDAO.fetchByName(args[0]);

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

            Component tcName = Component.text(parsedName + "\n", TextColor.color(0xffffff));

            Component tcOnlineStatus;
            if (onlinePlayer != null) {
                tcOnlineStatus = Component.text("◆ Currently online.\n", TextColor.color(0x2bcd82));
            } else {
                Timestamp lastSeenTimestamp = new Timestamp(offlinePlayer.getLastSeen());
                tcOnlineStatus = Component.text("◆ Last seen: " + lastSeenTimestamp + "\n", TextColor.color(0x6c7f96));
            }

            ChunkDAO chunkDAO = new ChunkDAO(Hikari.getDataSource());
            int numChunks = chunkDAO.fetchCountByUUID(parsedUUID);

            Component tcChunkAmount = Component.text(
                "⧈ " + numChunks + " " + (numChunks == 1 ? "chunk" : "chunks") + " claimed\n",
                TextColor.color(0xc194fb));

            final String playerUrl = "https://lounge.haus/mc/profile/" + parsedUUID;
            Component tcUrlPage = Component.text(
                "⬈ View more details on the lounge site\n" + playerUrl, TextColor.color(0xa9c8fb))
                .clickEvent(ClickEvent.openUrl(playerUrl))
                .hoverEvent(HoverEvent.showText(Component.text("Open URL")));

            TextComponent output = Component.text().append(tcName, tcChunkAmount, tcOnlineStatus, tcUrlPage).build();

            senderPlayer.sendMessage(output);
        }
        return true;
    }
}