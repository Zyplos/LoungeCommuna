package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class HideHome implements CommandExecutor {
    private final LoungeCommuna plugin;

    public HideHome(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            final Component tcHome = plugin.utils.prefixedMessage()
                .append(Component.text("Your home coords won't show publicly anymore."));
            player.sendMessage(tcHome);

            plugin.hikariPool.playerDAO.hidePlayerHome(
                player.getUniqueId().toString()
            );
        }
        return true;
    }
}
