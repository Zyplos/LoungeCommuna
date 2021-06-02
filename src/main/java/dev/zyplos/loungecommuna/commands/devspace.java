package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
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

//            if (args.length > 0 && args[0].equals("s")) {
//                int id = plugin.taskManager.tasks.get(player.getUniqueId().toString());
//                Bukkit.getScheduler().cancelTask(id);
//                player.sendMessage("stopped " + id);
//                return true;
//            }

            StringBuilder output = new StringBuilder();
            List<BukkitTask> tasks = Bukkit.getScheduler().getPendingTasks();
            for (BukkitTask task : tasks) {
                output
                    .append(task.getTaskId())
                    .append(" | ")
                    .append(" isSync: ")
                    .append(task.isSync())
                    .append(" isCancelled: ")
                    .append(task.isCancelled())
                    .append(" getOwner: ")
                    .append(task.getOwner())
                    .append(" toString: ")
                    .append(task)
                    .append("\n");
            }

            player.sendMessage(output.toString());
        }
        return true;
    }
}
