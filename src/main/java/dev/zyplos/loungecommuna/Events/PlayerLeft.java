package dev.zyplos.loungecommuna.Events;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeft implements Listener {
    private final LoungeCommuna plugin;

    public PlayerLeft(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        event.quitMessage(
            Component.text("").color(TextColor.color(NamedTextColor.YELLOW))
                .append(Component.text("[", TextColor.color(0x848484)))
                .append(Component.text("-", TextColor.color(0xff3e3e)))
                .append(Component.text("] ", TextColor.color(0x848484)))
                .append(
                    Component
                        .text(playerName, TextColor.color(plugin.utils.colors.get("highlight")))
                        .clickEvent(ClickEvent.runCommand("/profile " + playerName))
                        .hoverEvent(
                            HoverEvent.showText(Component.text("Show " + playerName + "'s profile"))
                        )
                )
                .append(Component.text(" left the game"))
        );
    }
}
