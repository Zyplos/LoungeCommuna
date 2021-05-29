package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class SetCommunity implements CommandExecutor {
    private final LoungeCommuna plugin;

    public SetCommunity(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 2) {
                player.sendMessage(
                    plugin.utils.prefixedMessage()
                        .append(
                            Component.text("/setcommunity <player> <id>")
                        )
                        .append(Component.newline())
                        .append(Component.text(plugin.utils.communityNameMap.toString()))
                );
                return true;
            }

            Player selectedPlayer = Bukkit.getPlayer(args[0]);

            if (selectedPlayer == null) {
                player.sendMessage(plugin.utils.prefixedMessage().append(Component.text(args[0] + " isn't online")));
                return true;
            }

            int communityId = Integer.parseInt(args[1]);
            String communityName = plugin.utils.getCommunityName(communityId);
            String communityColor = plugin.utils.getCommunityBrandColor(communityId);

            if (communityName == null || communityColor == null) {
                player.sendMessage(
                    plugin.utils.prefixedMessage()
                        .append(
                            Component.text(communityId + " doesn't seem to be a valid community id")
                        )
                        .append(Component.newline())
                        .append(Component.text(plugin.utils.communityNameMap.toString()))
                );
                return true;
            }

            plugin.hikariPool.playerDAO.updatePlayerCommunity(
                selectedPlayer.getUniqueId().toString(),
                communityId
            );

            final Component tcSuccess = plugin.utils.prefixedMessage()
                .append(Component.text("Set "))
                .append(Component.text(selectedPlayer.getName(), TextColor.color(plugin.utils.colors.get("highlight"))))
                .append(Component.text("'s community to"))
                .append(
                    Component.text(" ○ " + communityName,
                        TextColor.fromCSSHexString(communityColor))
                );
            player.sendMessage(tcSuccess);
        }
        return true;
    }
}
