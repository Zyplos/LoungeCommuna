package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class SetHome implements CommandExecutor {
    private final LoungeCommuna plugin;

    public SetHome(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY();
            int z = player.getLocation().getBlockZ();

            plugin.hikariPool.playerDAO.updatePlayerHome(
                player.getUniqueId().toString(),
                x,
                y,
                z,
                player.getWorld().getUID().toString()
            );

            final Component tcHome = plugin.utils.prefixedMessage()
                .append(Component.text("Home coordinates set to"))
                .append(Component.text(" | ", TextColor.color(plugin.utils.colors.get("muted"))))
                .append(Component.text("X: "))
                .append(Component.text(x, TextColor.color(plugin.utils.colors.get("highlight"))))
                .append(Component.text(" Y: "))
                .append(Component.text(y, TextColor.color(plugin.utils.colors.get("highlight"))))
                .append(Component.text(" Z: "))
                .append(Component.text(z, TextColor.color(plugin.utils.colors.get("highlight"))));
            final Component tcPublicWarning =
                plugin.utils.prefixedMessage().decorate(TextDecoration.ITALIC)
                    .append(Component.text("Heads up! ", TextColor.color(plugin.utils.colors.get("warning"))))
                    .append(Component.text("Your home location is PUBLIC. Do "))
                    .append(Component.text("/hidehome", NamedTextColor.GREEN))
                    .append(Component.text(" to hide it from showing on your profile."));
            player.sendMessage(tcHome.append(Component.newline()).append(tcPublicWarning));
        }
        return true;
    }
}
