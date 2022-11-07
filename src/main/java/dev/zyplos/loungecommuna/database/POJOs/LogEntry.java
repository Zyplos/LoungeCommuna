package dev.zyplos.loungecommuna.database.POJOs;

import java.sql.Timestamp;
import java.util.UUID;

public class LogEntry {
    private int x;
    private int z;
    private UUID dimension;
    private UUID player_id;
    private String name;
    private Timestamp entered_time;

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

    public UUID getDimension() {
        return this.dimension;
    }

    public void setDimension(UUID dimension) {
        this.dimension = dimension;
    }

    public UUID getPlayer_id() {
        return this.player_id;
    }

    public void setPlayer_id(UUID player_id) {
        this.player_id = player_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getEntered_time() {
        return this.entered_time;
    }

    public void setEntered_time(Timestamp entered_time) {
        this.entered_time = entered_time;
    }
}
