package dev.zyplos.loungecommuna.database.POJOs;

import java.sql.Timestamp;

public class LogEntry {
    private int x;
    private int z;
    private String dimension;
    private String player_id;
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

    public String getDimension() {
        return this.dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
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

    public Timestamp getEntered_time() {
        return this.entered_time;
    }

    public void setEntered_time(Timestamp entered_time) {
        this.entered_time = entered_time;
    }
}
