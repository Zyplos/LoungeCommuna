package dev.zyplos.loungecommuna.commands;

import dev.zyplos.loungecommuna.LoungeCommuna;
import dev.zyplos.loungecommuna.database.POJOs.Chunk;
import dev.zyplos.loungecommuna.database.POJOs.LogEntry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VisitorsLog implements CommandExecutor {
    private final LoungeCommuna plugin;

    public VisitorsLog(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            int chunkX = player.getChunk().getX();
            int chunkZ = player.getChunk().getZ();
            String chunkDimension = player.getWorld().getUID().toString();

            plugin.hikariPool.chunkDAO.fetchByCoords(chunkX, chunkZ, chunkDimension, chunkOwnerInfo -> {
                if (!chunkOwnerInfo.isEmpty()) {
                    Chunk ownedChunk = chunkOwnerInfo.get(0);

                    plugin.hikariPool.logEntryDAO.fetchByCoords(chunkX, chunkZ, chunkDimension, historyList -> {
                        TextComponent.Builder tcOutput = Component.text().append(plugin.utils.prefixedMessage());

                        tcOutput.append(
                            Component.text("Chunk owner: "),
                            Component.text(ownedChunk.getName(), TextColor.color(plugin.utils.colors.get("highlight"))),
                            Component.text(" | ", TextColor.color(plugin.utils.colors.get("muted"))),
                            Component.text("Last 10 visits:"),
                            Component.newline()
                        );

                        if (historyList.isEmpty()) {
                            tcOutput.append(
                                Component.text(
                                    "Seems no one has visited here yet.",
                                    TextColor.color(plugin.utils.colors.get("muted")),
                                    TextDecoration.ITALIC
                                ),
                                Component.newline()
                            );
                        } else {
                            for (LogEntry entry : historyList) {
                                tcOutput.append(
                                    Component
                                        .text(entry.getName(), TextColor.color(plugin.utils.colors.get("highlight")))
                                        .clickEvent(ClickEvent.runCommand("/profile " + entry.getName()))
                                        .hoverEvent(
                                            HoverEvent.showText(Component.text("Show " + entry.getName() + "'s profile"))
                                        ),
                                    Component.text(" - "),
                                    Component.text(String.format("%1$TD %1$Tr", entry.getEntered_time())),
                                    Component.text(" CST"),
                                    Component.newline()
                                );
                            }
                        }

                        final String chunkUrl = "https://dev.lounge.haus/mc/chunk/" + chunkX + "/" + chunkZ;
                        tcOutput.append(
                            Component.text(
                                "⬈ View all entries online", TextColor.color(0xa9c8fb))
                                .clickEvent(ClickEvent.openUrl(chunkUrl))
                                .hoverEvent(HoverEvent.showText(Component.text("Open URL")))
                        );

                        player.sendMessage(tcOutput.build());
                    });
                } else {
                    player.sendMessage(
                        plugin.utils.prefixedMessage().append(
                            plugin.utils.formatErrorMessage(
                                "No one owns this chunk. Visitor's logs only show on claimed chunks.")
                        )
                    );
                }
            });
        }
        return true;
    }
}