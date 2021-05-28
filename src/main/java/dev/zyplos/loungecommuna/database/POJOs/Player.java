package dev.zyplos.loungecommuna.database.POJOs;

import java.sql.Timestamp;

public class Player {
    private String player_id;
    private String name;
    private Timestamp joined;
    private int community_id;

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

    public int getCommunity_id() {
        return this.community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }
}