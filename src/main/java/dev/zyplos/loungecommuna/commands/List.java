package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;


public class List implements CommandExecutor {
    private final LoungeCommuna plugin;

    public List(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

            TextComponent.Builder tcOutput = Component.text()
                .append(plugin.utils.prefixedMessage())
                .append(Component.text("Current players online: "));
            ArrayList<Component> tcPlayers = new ArrayList<>();
            for (Player currentPlayer : onlinePlayers) {
                tcPlayers.add(
                    Component
                        .text(currentPlayer.getName(), TextColor.color(plugin.utils.colors.get("highlight")))
                        .clickEvent(ClickEvent.runCommand("/profile " + currentPlayer.getName()))
                        .hoverEvent(
                            HoverEvent.showText(Component.text("Show " + currentPlayer.getName() + "'s profile"))
                        )
                );
            }
            tcOutput.append(
                Component.join(Component.text(", "), tcPlayers)
            );
            player.sendMessage(tcOutput);
        }
        return true;
    }
}
