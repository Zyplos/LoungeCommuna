package dev.zyplos.loungecommuna.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class Hikari {
    private HikariDataSource ds;

    public Hikari() {

        final Dotenv dotenv = Dotenv.load();
        final String dbUsername = dotenv.get("DB_USERNAME");
        final String dbPassword = dotenv.get("DB_PASSWORD");
        final String dbUrl = dotenv.get("DB_URL");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }


    public DataSource getDataSource() {
        return ds;
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
