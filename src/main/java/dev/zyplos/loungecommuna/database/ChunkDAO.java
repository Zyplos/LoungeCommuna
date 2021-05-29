package dev.zyplos.loungecommuna.database;

import dev.zyplos.loungecommuna.LoungeCommuna;
import dev.zyplos.loungecommuna.database.POJOs.Chunk;
import dev.zyplos.loungecommuna.internals.AsyncCallback;
import org.bukkit.scheduler.BukkitRunnable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import javax.sql.DataSource;
import java.util.List;

public class ChunkDAO {
    private final Sql2o dao;
    private final LoungeCommuna plugin;

    public ChunkDAO(DataSource ds, LoungeCommuna plugin) {
        System.out.println("[ChunkDAO] Connecting to database " + ds.toString());
        this.plugin = plugin;
        dao = new Sql2o(ds);
    }

    public void fetchByCoords(int xCoord, int zCoord, AsyncCallback<List<Chunk>> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "SELECT chunk_id, BIN_TO_UUID(player_id) AS player_id, name, claimed_on, x, z," +
                    "BIN_TO_UUID(dimension) AS dimension " +
                    "FROM chunks JOIN players USING (player_id) WHERE x=:xCoord AND z=:zCoord";
                try (Connection conn = dao.open()) {
                    List<Chunk> result = conn.createQuery(sql)
                        .addParameter("xCoord", xCoord)
                        .addParameter("zCoord", zCoord)
                        .executeAndFetch(Chunk.class);

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

    public void fetchCountByUUID(String uuid, AsyncCallback<Integer> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "SELECT COUNT(*) FROM chunks WHERE player_id=UUID_TO_BIN(:uuid)";
                try (Connection conn = dao.open()) {
                    int result = conn.createQuery(sql)
                        .addParameter("uuid", uuid)
                        .executeScalar(Integer.class);

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

    public void deleteChunk(int x, int z, String dimensionUUID, String playerUUID) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "DELETE FROM chunks WHERE " +
                    "x=:x AND z=:z AND dimension=UUID_TO_BIN(:dimensionUUID)" +
                    "AND player_id=UUID_TO_BIN(:playerUUID)";
                try (Connection conn = dao.open()) {
                    conn.createQuery(sql)
                        .addParameter("x", x)
                        .addParameter("z", z)
                        .addParameter("dimensionUUID", dimensionUUID)
                        .addParameter("playerUUID", playerUUID)
                        .executeUpdate();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void insert(Chunk chunk, AsyncCallback<String> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "INSERT INTO chunks(player_id, claimed_on, x, z, dimension) VALUES ( UUID_TO_BIN(:player_id), " +
                    ":claimed_on, :x, :z, UUID_TO_BIN(:dimension))";

                try (Connection conn = dao.beginTransaction()) {
                    try {
                        conn.createQuery(sql).bind(chunk).executeUpdate();
                        conn.commit();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                callback.thenDo(null);
                            }
                        }.runTask(plugin);
                    } catch (Sql2oException e) {
                        conn.rollback();
                        String result;
                        if (e.getMessage().contains("Duplicate entry")) {
                            result = "This chunk (" + chunk.getX() + "," + chunk.getZ() + ") has already " +
                                "been claimed.";
                        } else {
                            result = e.toString();
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                callback.thenDo(result);
                            }
                        }.runTask(plugin);
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}

