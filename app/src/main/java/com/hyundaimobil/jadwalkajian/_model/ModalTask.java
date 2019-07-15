package com.hyundaimobil.jadwalkajian._model;

/**
 * Created by User HMI on 9/16/2017.
 */
public class ModalTask {
    private String number, title, kode, count;

    public ModalTask() {
    }

    public ModalTask(String number, String title, String kode, String count) {
        this.number = number;
        this.title = title;
        this.kode = kode;
        this.count = count;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String name) {
        this.title = name;
    }

    public String getKode() {
        return kode;
    }
    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }

}
