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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player senderPlayer = (Player) sender;

            plugin.hikariPool.playerDAO.fetchByName("Zyplos", result -> {
                dev.zyplos.loungecommuna.database.POJOs.Player playerResult = result.get(0);

                senderPlayer.sendMessage("RESULT: " + playerResult.getPlayer_id());
                senderPlayer.sendMessage("RESULT: " + playerResult.getName());
                senderPlayer.sendMessage("RESULT: " + playerResult.getJoined());
            });
        }
        return true;
    }
}
