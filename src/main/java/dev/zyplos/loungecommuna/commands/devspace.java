package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class devspace implements CommandExecutor {
    private final LoungeCommuna plugin;

    public devspace(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

//            plugin.hikariPool.playerDAO.fetchByName(args[0], result -> {
//                dev.zyplos.loungecommuna.database.POJOs.Player playerResult = result.get(0);
//
//                player.sendMessage("RESULT: " + playerResult.getPlayer_id());
//                player.sendMessage("RESULT: " + playerResult.getName());
//                player.sendMessage("RESULT: " + playerResult.getJoined());
//                player.sendMessage("RESULT: " + playerResult.getCommunity_id());
//                player.sendMessage("RESULT: " + playerResult.getHome_x());
//                player.sendMessage("RESULT: " + playerResult.getHome_y());
//                player.sendMessage("RESULT: " + playerResult.getHome_z());
//                player.sendMessage("RESULT: " + playerResult.getHome_dimension());
//            });

            StringBuilder output = new StringBuilder();
            List<World> worlds = Bukkit.getWorlds();
            for (World world : worlds) {
                output.append(world.getName()).append("-").append(world.getUID().toString()).append("\n");
            }

            player.sendMessage(output.toString());
        }
        return true;
    }
}
