package dev.zyplos.loungecommuna.Events;

import dev.zyplos.loungecommuna.LoungeCommuna;
import dev.zyplos.loungecommuna.database.POJOs.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;

public class PlayerJoined implements Listener {
    private final LoungeCommuna plugin;

    public PlayerJoined(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();

        Player player = new Player();
        player.setPlayer_id(event.getPlayer().getUniqueId().toString());
        player.setName(playerName);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        player.setJoined(timestamp);

        plugin.hikariPool.playerDAO.insert(player);

        
        event.joinMessage(
            Component.text("").color(TextColor.color(NamedTextColor.YELLOW))
                .append(Component.text("[", TextColor.color(0x848484)))
                .append(Component.text("+", TextColor.color(0x50fc3d)))
                .append(Component.text("] ", TextColor.color(0x848484)))
                .append(
                    Component
                        .text(playerName, TextColor.color(plugin.utils.colors.get("highlight")))
                        .clickEvent(ClickEvent.runCommand("/profile " + playerName))
                        .hoverEvent(
                            HoverEvent.showText(Component.text("Show " + playerName + "'s profile"))
                        )
                )
                .append(Component.text(" joined the game"))
        );
    }
}
