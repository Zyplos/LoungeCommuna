package dev.zyplos.loungecommuna.database.POJOs;

import java.sql.Timestamp;

public class Player {
    private String player_id;
    private String name;
    private Timestamp joined;
    private int community_id;
    private int home_x;
    private int home_y;
    private int home_z;
    private String home_dimension;

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

    public int getHome_x() {
        return this.home_x;
    }

    public void getHome_x(int home_x) {
        this.home_x = home_x;
    }

    public int getHome_y() {
        return this.home_y;
    }

    public void getHome_y(int home_y) {
        this.home_y = home_y;
    }

    public int getHome_z() {
        return this.home_z;
    }

    public void getHome_z(int home_z) {
        this.home_z = home_z;
    }

    public String getHome_dimension() {
        return this.home_dimension;
    }

    public void setHome_dimension(String home_dimension) {
        this.home_dimension = home_dimension;
    }
}