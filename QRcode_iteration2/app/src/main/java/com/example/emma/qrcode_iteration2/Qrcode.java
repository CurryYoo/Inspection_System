package com.example.emma.qrcode_iteration2;

public class Qrcode {
    private int id;
    private String name;

    public Qrcode(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }

    public String toString() {
        return "name: " + name;
    }
}
