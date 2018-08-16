package com.example.emma.qrcode_iteration2;

/**
 * Created by touchzy on 2018/3/21.
 */

public class Location {
    private String id;
    private String name;
    private String description;
    private float coor_x;
    private float coor_y;
    private int floor;
    private boolean loc;
    private float angle;
    private String URL;
    private String BuildName;
    private int idInMap;
    public Location(){};

    public Location(String id, String name, String description, float coor_x, float coor_y, int floor, boolean loc, float angle, String URL, String buildName, int idInMap) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.coor_x = coor_x;
        this.coor_y = coor_y;
        this.floor = floor;
        this.loc = loc;
        this.angle = angle;
        this.URL = URL;
        BuildName = buildName;
        this.idInMap = idInMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getCoor_x() {
        return coor_x;
    }

    public void setCoor_x(float coor_x) {
        this.coor_x = coor_x;
    }

    public float getCoor_y() {
        return coor_y;
    }

    public void setCoor_y(float coor_y) {
        this.coor_y = coor_y;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public boolean getLoc() {
        return loc;
    }

    public void setLoc(boolean loc) {
        this.loc = loc;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getBuildName() {
        return BuildName;
    }

    public void setBuildName(String buildName) {
        BuildName = buildName;
    }

    public int getIdInMap() {
        return idInMap;
    }

    public void setIdInMap(int idInMap) {
        this.idInMap = idInMap;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", coor_x=" + coor_x +
                ", coor_y=" + coor_y +
                ", floor=" + floor +
                ", loc=" + loc +
                ", angle=" + angle +
                ", URL='" + URL + '\'' +
                ", BuildName='" + BuildName + '\'' +
                ", idInMap=" + idInMap +
                '}';
    }
}
