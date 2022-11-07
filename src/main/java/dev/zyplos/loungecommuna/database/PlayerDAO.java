package dev.zyplos.loungecommuna.database;

import dev.zyplos.loungecommuna.LoungeCommuna;
import dev.zyplos.loungecommuna.database.POJOs.Player;
import dev.zyplos.loungecommuna.internals.AsyncCallback;
import org.bukkit.scheduler.BukkitRunnable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

public class PlayerDAO {
    private final Sql2o dao;
    private final LoungeCommuna plugin;

    public PlayerDAO(DataSource ds, LoungeCommuna plugin) {
        System.out.println("[PlayerDAO] Connecting to database " + ds.toString());
        this.plugin = plugin;
        dao = new Sql2o(ds);
    }

    public void fetchByName(String username, final AsyncCallback<List<Player>> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = " SELECT player_id, name, joined, " +
                    "community_id, home_x, home_y, home_z, home_dimension, " +
                    "home_hidden " +
                    "FROM players WHERE name=:username";
                try (Connection conn = dao.open()) {
                    List<Player> result = conn.createQuery(sql)
                        .addParameter("username", username)
                        .executeAndFetch(Player.class);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            callback.thenDo(result);
                        }
                    }.runTask(plugin);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void insert(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "INSERT INTO players(player_id, name, joined) " +
                    "VALUES (:player_id, :name, :joined ) " +
                    "ON CONFLICT (player_id) DO UPDATE SET name=:name";
                try (Connection conn = dao.open()) {
                    conn.createQuery(sql).bind(player).executeUpdate();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updatePlayerHome(UUID playerUUID, int x, int y, int z, UUID dimensionUUID) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "UPDATE players " +
                    "SET home_x=:x, home_y=:y, home_z=:z, home_dimension=:dimensionUUID " +
                    "WHERE player_id=:playerUUID";
                try (Connection conn = dao.open()) {
                    conn.createQuery(sql)
                        .addParameter("playerUUID", playerUUID)
                        .addParameter("x", x)
                        .addParameter("y", y)
                        .addParameter("z", z)
                        .addParameter("dimensionUUID", dimensionUUID)
                        .executeUpdate();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void hidePlayerHome(UUID playerUUID) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "UPDATE players " +
                    "SET home_hidden=true " +
                    "WHERE player_id=:playerUUID";
                try (Connection conn = dao.open()) {
                    conn.createQuery(sql)
                        .addParameter("playerUUID", playerUUID)
                        .executeUpdate();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void showPlayerHome(UUID playerUUID) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "UPDATE players " +
                    "SET home_hidden=false " +
                    "WHERE player_id=:playerUUID";
                try (Connection conn = dao.open()) {
                    conn.createQuery(sql)
                        .addParameter("playerUUID", playerUUID)
                        .executeUpdate();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updatePlayerCommunity(UUID playerUUID, int communityId) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "UPDATE players " +
                    "SET community_id=:communityId " +
                    "WHERE player_id=:playerUUID";
                try (Connection conn = dao.open()) {
                    conn.createQuery(sql)
                        .addParameter("communityId", communityId)
                        .addParameter("playerUUID", playerUUID)
                        .executeUpdate();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}