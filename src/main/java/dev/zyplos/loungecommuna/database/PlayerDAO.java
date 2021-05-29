package dev.zyplos.loungecommuna.database;

import dev.zyplos.loungecommuna.LoungeCommuna;
import dev.zyplos.loungecommuna.database.POJOs.Player;
import dev.zyplos.loungecommuna.internals.AsyncCallback;
import org.bukkit.scheduler.BukkitRunnable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.List;

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
                String sql = " SELECT BIN_TO_UUID(player_id) AS player_id, name, joined," +
                    "community_id, home_x, home_y, home_z, BIN_TO_UUID(home_dimension) AS home_dimension " +
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
                    "VALUES ( UUID_TO_BIN(:player_id), :name, :joined ) " +
                    "ON DUPLICATE KEY UPDATE name=:name";
                try (Connection conn = dao.open()) {
                    conn.createQuery(sql).bind(player).executeUpdate();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updatePlayerHome(String playerUUID, int x, int y, int z, String dimensionUUID) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "UPDATE players " +
                    "SET home_x=:x, home_y=:y, home_z=:z, home_dimension=UUID_TO_BIN(:dimensionUUID) " +
                    "WHERE player_id=UUID_TO_BIN(:playerUUID)";
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

    public void updatePlayerCommunity(String playerUUID, int communityId) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "UPDATE players " +
                    "SET community_id=:communityId " +
                    "WHERE player_id=UUID_TO_BIN(:playerUUID)";
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