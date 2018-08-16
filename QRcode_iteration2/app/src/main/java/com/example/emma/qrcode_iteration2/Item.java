package com.example.emma.qrcode_iteration2;

public class Item {
    private String items;
    private String type;

    public Item(){
        super();
    }

    public Item(String items, String type){
        this.items = items;
        this.type = type;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{'items':'" + items + "','type':'" + type + "'}";
    }
}
