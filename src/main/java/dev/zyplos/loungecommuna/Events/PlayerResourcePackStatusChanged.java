package dev.zyplos.loungecommuna.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class PlayerResourcePackStatusChanged implements Listener {
    // zy was here
    @EventHandler
    public void onPlayerResourcePackStatusChange(PlayerResourcePackStatusEvent event) {
        PlayerResourcePackStatusEvent.Status status = event.getStatus();

        if (status == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            event.getPlayer().setPlayerListHeader("\uEff1\n");
        }
    }
}