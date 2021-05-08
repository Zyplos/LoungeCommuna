package dev.zyplos.loungecommuna.database.POJOs;

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

import java.sql.Timestamp;

public class Chunk {
    private int chunk_id;
    private String player_id;
    private String name;
    private Timestamp claimed_on;
    int x;
    int z;
    private String dimension;

    public int getChunk_id() {
        return this.chunk_id;
    }

    public void setChunk_id(int id) {
        this.chunk_id = id;
    }

    public String getPlayer_id() {
        return this.player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getClaimed_on() {
        return this.claimed_on;
    }

    public void setClaimed_on(Timestamp claimed_on) {
        this.claimed_on = claimed_on;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getDimension() {
        return this.dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

}

