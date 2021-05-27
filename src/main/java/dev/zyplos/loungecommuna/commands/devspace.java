package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class devspace implements CommandExecutor {
    private final LoungeCommuna plugin;

    public devspace(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.sendMessage(Math.random() > 0.5 ? "heads" : "tails");
        }
        return true;
    }
}
