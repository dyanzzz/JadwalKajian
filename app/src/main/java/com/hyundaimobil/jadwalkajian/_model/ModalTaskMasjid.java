package com.hyundaimobil.jadwalkajian._model;

/**
 * Created by User HMI on 9/17/2017.
 */

public class ModalTaskMasjid {
    private String number, title, kode, address, informasi, link_maps;

    public ModalTaskMasjid() {
    }

    public ModalTaskMasjid(String number, String title, String kode, String address, String informasi, String link_maps) {
        this.number     = number;
        this.title      = title;
        this.kode       = kode;
        this.address    = address;
        this.informasi  = informasi;
        this.link_maps  = link_maps;
        //this.count      = count;
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

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getInformasi() {
        return informasi;
    }
    public void setInformasi(String informasi) {
        this.informasi = informasi;
    }

    public String getLink_maps() {
        return link_maps;
    }
    public void setLink_maps(String link_maps) {
        this.link_maps = link_maps;
    }
    /*
    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }
*/
}
