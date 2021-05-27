package dev.zyplos.loungecommuna.Events;

import dev.zyplos.loungecommuna.LoungeCommuna;
import dev.zyplos.loungecommuna.database.POJOs.Player;
import dev.zyplos.loungecommuna.database.PlayerDAO;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;

public class PlayerJoined implements Listener {
    private final LoungeCommuna plugin;

    public PlayerJoined(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerDAO playerDAO = new PlayerDAO(plugin.hikariPool.getDataSource());

        Player player = new Player();
        player.setPlayer_id(event.getPlayer().getUniqueId().toString());
        player.setName(event.getPlayer().getName());

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        player.setJoined(timestamp);

        playerDAO.insert(player);
    }
}
