package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class Help implements CommandExecutor {
    private final LoungeCommuna plugin;

    public Help(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    private Component createCmdComponent(String command, String helpText) {
        return Component.newline()
            .append(
                Component.text(command, TextColor.color(NamedTextColor.GREEN))
                    .clickEvent(ClickEvent.suggestCommand(command))
                    .hoverEvent(
                        HoverEvent.showText(Component.text("Quick Type"))
                    )
            )
            .append(Component.text(" - " + helpText));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            final Component tcHelp = plugin.utils.prefixedMessage()
                .append(Component.text(
                    "Welcome to the lounge SMP server! " +
                        "We have a few commands that help connect people in the server."
                ))
                .append(createCmdComponent("/claim", "Grants ownership of the chunk you're in. " +
                    "Note: this is public information! Don't claim a chunk you want to keep secret."))
                .append(createCmdComponent("/unclaim", "Unclaim the chunk you're in for others to claim."))
                .append(createCmdComponent("/profile <username>", "Bring up some info on a player."))
                .append(createCmdComponent("/sethome", "Sets your home coordinates for you to refer to later. " +
                    "Note: this is public information so people can visit you."))
                .append(createCmdComponent("/home", "Tells you your home coordinates. " +
                    "Note: this does not teleport you."))
                .append(createCmdComponent("/chunkinfo", "Show info about the chunk you're in."));
            
            player.sendMessage(tcHelp);
        }
        return true;
    }
}
