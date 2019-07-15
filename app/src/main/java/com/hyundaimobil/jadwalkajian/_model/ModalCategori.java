package com.hyundaimobil.jadwalkajian._model;

/**
 * Created by User HMI on 9/16/2017.
 */

public class ModalCategori {
    private String id, title;

    public ModalCategori() {
    }

    public ModalCategori(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

}