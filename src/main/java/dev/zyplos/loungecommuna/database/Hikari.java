package dev.zyplos.loungecommuna.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.zyplos.loungecommuna.LoungeCommuna;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Hikari {
    private HikariDataSource ds;
    public PlayerDAO playerDAO;
    public ChunkDAO chunkDAO;
    public LogEntryDAO logEntryDAO;

    public Hikari(LoungeCommuna plugin) {
        final String dbUsername = plugin.getConfig().getString("db.username");
        final String dbPassword = plugin.getConfig().getString("db.password");
        final String dbName = plugin.getConfig().getString("db.database");
        final String dbHostname = plugin.getConfig().getString("db.hostname");
        final String dbUrl = plugin.getConfig().getString("db.url");

        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.user", dbUsername);
        props.setProperty("dataSource.password", dbPassword);
        props.setProperty("dataSource.databaseName", dbName);
        props.setProperty("dataSource.serverName", dbHostname);

        HikariConfig config = new HikariConfig(props);

        ds = new HikariDataSource(config);

        playerDAO = new PlayerDAO(ds, plugin);
        chunkDAO = new ChunkDAO(ds, plugin);
        logEntryDAO = new LogEntryDAO(ds, plugin);
    }

    public DataSource getDataSource() {
        return ds;
    }

    public void closeConnection() {
        ds.close();
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
