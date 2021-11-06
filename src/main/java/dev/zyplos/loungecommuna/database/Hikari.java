package dev.zyplos.loungecommuna.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.zyplos.loungecommuna.LoungeCommuna;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Hikari {
    private HikariDataSource ds;
    public PlayerDAO playerDAO;
    public ChunkDAO chunkDAO;
    public LogEntryDAO logEntryDAO;

    public Hikari(LoungeCommuna plugin) {
        final String dbUsername = plugin.getConfig().getString("db.username");
        final String dbPassword = plugin.getConfig().getString("db.password");
        final String dbUrl = plugin.getConfig().getString("db.url");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);

        playerDAO = new PlayerDAO(ds, plugin);
        chunkDAO = new ChunkDAO(ds, plugin);
        logEntryDAO = new LogEntryDAO(ds, plugin);
    }

    public DataSource getDataSource() {
        return ds;
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
