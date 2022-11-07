package dev.zyplos.loungecommuna.database;

import dev.zyplos.loungecommuna.LoungeCommuna;
import dev.zyplos.loungecommuna.database.POJOs.LogEntry;
import dev.zyplos.loungecommuna.internals.AsyncCallback;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

public class LogEntryDAO {
    private final Sql2o dao;
    private final LoungeCommuna plugin;

    public LogEntryDAO(DataSource ds, LoungeCommuna plugin) {
        Bukkit.getLogger().info("[LogEntryDAO] Connecting to database " + ds.toString());
        this.plugin = plugin;
        dao = new Sql2o(ds);
    }

    public void insert(LogEntry logEntry) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "INSERT INTO logentries(x, z, dimension, player_id, entered_time) " +
                    "VALUES (:x, :z, :dimension, :player_id, :entered_time)";
                try (Connection conn = dao.open()) {
                    conn.createQuery(sql).bind(logEntry).executeUpdate();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void fetchByCoords(int x, int z, UUID dimensionUUID, AsyncCallback<List<LogEntry>> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "SELECT " +
                    "x, z, dimension, player_id, name, entered_time" +
                    " FROM logentries JOIN players USING (player_id)" +
                    "WHERE x=:x AND z=:z AND dimension=:dimensionUUID " +
                    "ORDER BY id DESC LIMIT 5";
                try (Connection conn = dao.open()) {
                    List<LogEntry> result = conn.createQuery(sql)
                        .addParameter("x", x)
                        .addParameter("z", z)
                        .addParameter("dimensionUUID", dimensionUUID)
                        .executeAndFetch(LogEntry.class);

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
}
