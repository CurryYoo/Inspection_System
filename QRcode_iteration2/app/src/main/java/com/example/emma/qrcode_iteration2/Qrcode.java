package com.example.emma.qrcode_iteration2;

public class Qrcode {
    private int id;
    private String name;

    public Qrcode(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }

    public String toString() {
        return "id: " + id + ", name: " + name;
    }
}
