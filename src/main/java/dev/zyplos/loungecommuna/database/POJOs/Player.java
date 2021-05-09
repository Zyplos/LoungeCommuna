package dev.zyplos.loungecommuna.database.POJOs;

import java.sql.Timestamp;

public class Player {
    private String player_id;
    private String name;
    private Timestamp joined;

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

    public Timestamp getJoined() {
        return this.joined;
    }

    public void setJoined(Timestamp joined) {
        this.joined = joined;
    }
}