package com.hyundaimobil.jadwalkajian._model;

/**
 * Created by User HMI on 9/17/2017.
 */

public class ModalTaskKajian {
    private String number, keterangan, kode, image;

    public ModalTaskKajian() {
    }

    public ModalTaskKajian(String number, String keterangan, String kode, String image) {
        this.number     = number;
        //this.pemateri   = pemateri;
        //this.tema       = tema;
        //this.waktu      = waktu;
        this.keterangan = keterangan;
        this.kode       = kode;
        this.image      = image;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    /*
        public String getPemateri() {
            return pemateri;
        }
        public void setPemateri(String pemateri) {
            this.pemateri = pemateri;
        }

        public String getTema() {
            return tema;
        }
        public void setTema(String tema) {
            this.tema = tema;
        }

        public String getWaktu() {
            return waktu;
        }
        public void setWaktu(String waktu) {
            this.waktu = waktu;
        }
    */
    public String getKeterangan() {
        return keterangan;
    }
    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getKode() {
        return kode;
    }
    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}
