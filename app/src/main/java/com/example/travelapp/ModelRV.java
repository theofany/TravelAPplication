package com.example.travelapp;

public class ModelRV {
    private String id, nama, desc, img;

    public ModelRV(){

    }

    public ModelRV(String nama, String desc, String img) {
        this.nama = nama;
        this.desc = desc;
        this.img = img;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
