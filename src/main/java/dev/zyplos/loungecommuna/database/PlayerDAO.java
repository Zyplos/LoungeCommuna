package dev.zyplos.loungecommuna.database;

import dev.zyplos.loungecommuna.database.POJOs.Player;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.List;

public class PlayerDAO {
    private final Sql2o dao;

    public PlayerDAO(DataSource ds) {
        System.out.println("[PlayerDAO] Connecting to database " + ds.toString());
        dao = new Sql2o(ds);
    }

    public List<Player> fetchAll() {
        String sql = "SELECT BIN_TO_UUID(player_id) AS player_id, name, joined FROM players";
        try (Connection conn = dao.open()) {
            return conn.createQuery(sql).executeAndFetch(Player.class);
        }
    }

    public List<Player> fetchByName(String username) {
        String sql = " SELECT BIN_TO_UUID(player_id) AS player_id, name, joined FROM players WHERE name=:username";
        try (Connection conn = dao.open()) {
            return conn.createQuery(sql)
                .addParameter("username", username)
                .executeAndFetch(Player.class);
        }
    }

    public void insert(Player player) {
        String sql = "INSERT IGNORE INTO players(player_id, name, joined) VALUES ( UUID_TO_BIN(:player_id), :name, " +
            ":joined )";
        try (Connection conn = dao.open()) {
            conn.createQuery(sql).bind(player).executeUpdate();
        }
    }
}