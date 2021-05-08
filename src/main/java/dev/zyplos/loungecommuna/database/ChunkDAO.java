package dev.zyplos.loungecommuna.database;

import dev.zyplos.loungecommuna.database.POJOs.Chunk;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.List;

public class ChunkDAO {
    private final Sql2o dao;

    public ChunkDAO(DataSource ds) {
        System.out.println("[ChunkDAO] Connecting to database " + ds.toString());
        dao = new Sql2o(ds);
    }


    public List<Chunk> fetchAll() {
        String sql = "SELECT chunk_id, BIN_TO_UUID(player_id) AS player_id, name, claimed_on, x, z FROM chunks JOIN players USING (player_id)";
        try (Connection conn = dao.open()) {
            return conn.createQuery(sql).executeAndFetch(Chunk.class);
        }
    }

    /*

    INSERT INTO players(player_id, name, joined) VALUES
            ( UUID_TO_BIN('***REMOVED***'), 'Zyp', '2021-03-25 20:19:19' );

     */
        /*

    CREATE TABLE chunks
(
    chunk_id INT unsigned AUTO_INCREMENT PRIMARY KEY,
    player_id BINARY(16),
    claimed_on TIMESTAMP NOT NULL,
    x MEDIUMINT NOT NULL,
    z MEDIUMINT NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);

     */
    public void insert(Chunk chunk) {
        String sql = "INSERT INTO chunks(player_id, claimed_on, x, z) VALUES ( UUID_TO_BIN(:player_id), :claimed_on, :x, :z)";
        try (Connection conn = dao.open()) {
            conn.createQuery(sql).bind(chunk).executeUpdate();
        }
    }

}

