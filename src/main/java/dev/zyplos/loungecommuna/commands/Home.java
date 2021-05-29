package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class Home implements CommandExecutor {
    private final LoungeCommuna plugin;

    public Home(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            plugin.hikariPool.playerDAO.fetchByName(player.getName(), resultPlayerList -> {
                dev.zyplos.loungecommuna.database.POJOs.Player resultPlayer = resultPlayerList.get(0);

                int x = resultPlayer.getHome_x();
                int y = resultPlayer.getHome_y();
                int z = resultPlayer.getHome_z();

                final Component tcHome = plugin.utils.prefixedMessage()
                    .append(Component.text("Home"))
                    .append(Component.text(" | ", TextColor.color(plugin.utils.colors.get("muted"))))
                    .append(Component.text("X: "))
                    .append(Component.text(x, TextColor.color(plugin.utils.colors.get("highlight"))))
                    .append(Component.text(" Y: "))
                    .append(Component.text(y, TextColor.color(plugin.utils.colors.get("highlight"))))
                    .append(Component.text(" Z: "))
                    .append(Component.text(z, TextColor.color(plugin.utils.colors.get("highlight"))));
                player.sendMessage(tcHome);
            });
        }
        return true;
    }
}
