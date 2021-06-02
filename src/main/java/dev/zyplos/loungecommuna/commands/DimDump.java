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


public class DimDump implements CommandExecutor {
    private final LoungeCommuna plugin;

    public DimDump(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            StringBuilder output = new StringBuilder();
            List<World> worlds = Bukkit.getWorlds();
            for (World world : worlds) {
                output.append(world.getName()).append(" | ").append(world.getUID().toString()).append("\n");
            }

            player.sendMessage(output.toString());
        }
        return true;
    }
}
