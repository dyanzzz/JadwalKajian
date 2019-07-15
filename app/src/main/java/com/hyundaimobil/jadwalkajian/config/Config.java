package com.hyundaimobil.jadwalkajian.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by User HMI on 9/16/2017.
 */

@SuppressLint("Registered")
public class Config extends AppCompatActivity {

    //JSON
    public static final String TAG_JSON_ARRAY   = "result";

    //server
    //public static final String Server           = "http://192.168.1.244/jadwal_kajian/";
    //public static final String Server           = "http://192.168.43.238/jadwal_kajian/";
    //public static final String Server           = "http://185.176.43.64/";
    public static final String Server           = "http://jadwalkajian.mywebcommunity.org/";
    //https://cp1.awardspace.net/beta/database-manager/#mysql-databases


    //url
    public static final String LOGIN_URL                    = Server + "login.php";
    public static final String URL_GET_ALL_WILAYAH          = Server + "daftarWilayah.php";
    public static final String URL_GET_ALL_MASJID           = Server + "daftarMasjid.php";
    public static final String URL_GET_ALL_KAJIAN           = Server + "daftarKajian.php";
    public static final String URL_ADD_WILAYAH              = Server + "addWilayah.php";
    public static final String URL_ADD_MASJID               = Server + "addMasjid.php";
    public static final String URL_ADD_KAJIAN               = Server + "addKajian.php";
    public static final String URL_UPLOAD_IMG               = Server + "uploadImg.php";
    public static final String URL_GET_ALL_NEWS             = Server + "daftarNews.php";
    public static final String URL_GET_ALL_NEWS_DETAIL      = Server + "daftarNewsDetail.php";


    public static final String KEY_USERNAME                 = "username";
    public static final String KEY_PASSWORD                 = "password";
    public static final String KEY_FLAG_APPS                = "flag_apps";
    public static final String KEY_IP                       = "ip";
    public static final String KEY_VERSI                    = "versi";
    public static final String VALUE_FLAG_APPS              = "1";
    public static final String VALUE_VERSI                  = "1.0.1";

    //validasi allert
    public static final String ALERT_USERNAME               = "* Please Input Username";
    public static final String ALERT_PASSWORD               = "* Please Input Password";
    public static final String ALERT_PASSWORD_LAMA          = "* Please Input Current Password";
    public static final String ALERT_PASSWORD_BARU          = "* Please Input New Password and must be more than 3 characters in length";
    public static final String ALERT_PASSWORD_BARU2         = "* Please Input Re-Type New Password and must be more than 3 characters in length";
    public static final String ALERT_PASSWORD_BARU_SAMA     = "* Password must match";
    public static final String ALERT_TITLE_CONN_ERROR       = "Connection Error";
    public static final String ALERT_MESSAGE_CONN_ERROR     = "Unable to connect with the server. Check your internet connection and try again.";
    public static final String ALERT_MESSAGE_NO_CONN        = "No connection";
    public static final String ALERT_MESSAGE_SRV_NOT_FOUND  = "Server not found";
    public static final String ALERT_OK_BUTTON              = "Ok";
    public static final String ALERT_NOT_FOUND              = "Data Not Found";
    public static final String ALERT_LOADING                = "Loading";
    public static final String ALERT_PLEASE_WAIT            = "Please Wait";

    public static final String ALERT_NAMA_MASJID            = "* Mohon Input Nama Masjid";
    public static final String ALERT_ALAMAT                 = "* Mohon Input Alamat Masjid";
    public static final String ALERT_INFORMASI              = "* Mohon Input Informasi Masjid";
    public static final String ALERT_LINK_MAPS              = "* Mohon Input Link Maps";
    public static final String ALERT_LATITUDE               = "* Mohon Input Lokasi Latitude";
    public static final String ALERT_LONGTITUDE             = "* Mohon Input Lokasi Longtitude";
    public static final String ALERT_TANGGAL_KAJIAN         = "* Mohon Input Tanggal Kajian";
    public static final String ALERT_KETERANGAN_KAJIAN      = "* Mohon Input Keterangan";

    public static final String NOTIF_LOGOUT                 = "Are you sure want to Logout?";
    public static final String NOTIF_CONTACT_ADMIN          = "Please Contact Administrator.";
    public static final String HARUS_DIISI                  = "Form Input Cannot be Empty!!!";
    public static final String DISP_NOMOR                   = "nomor";

    //untuk wilayah
    public static final String DISP_KD_WILAYAH              = "kdWilayah";
    public static final String DISP_WILAYAH                 = "namaWilayah";
    public static final String DISP_COUNT                   = "count";

    //masjid
    public static final String DISP_KD_MASJID               = "kdMasjid";
    public static final String DISP_NAMA_MASJID             = "namaMasjid";
    public static final String DISP_ALAMAT_MASJID           = "alamatMasjid";
    public static final String DISP_INFORMASI               = "informasi";
    public static final String DISP_LINK_MAPS               = "linkMaps";
    public static final String DISP_LAT                     = "lat";
    public static final String DISP_LNG                     = "lng";
    public static final String DISP_TANDAI_FORM_INPUT       = "formInput";

    //kajian
    public static final String DISP_KD_KAJIAN               = "kdKajian";
    public static final String DISP_TANGGAL                 = "tanggal";
    public static final String DISP_PEMATERI                = "pemateri";
    public static final String DISP_TEMA                    = "tema";
    public static final String DISP_WAKTU                   = "waktu";
    public static final String DISP_KETERANGAN              = "keterangan";
    public static final String DISP_IMAGE                   = "image";

    //news
    public static final String DISP_KD_NEWS                 = "kdNews";
    public static final String DISP_TGL_NEWS                = "tglNews";
    public static final String DISP_JUDUL_NEWS              = "judulNews";
    public static final String DISP_ISI_NEWS                = "isiNews";
    public static final String DISP_GAMBAR_NEWS             = "gambarNews";

    public static final String DISP_KD_TOMBOL               = "kdTombol";

    public static final String TITLE_DISP_WILAYAH           = "Form Input Wilayah";
    public static final String TITLE_DISP_MASJID            = "Form Input Masjid";
    public static final String TITLE_DISP_KAJIAN            = "Form Input Kajian";


    public static final boolean CEK_KONEKSI(Context cek){
        ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if(info != null && info.isConnected()){
            return true;
        }else{
            return false;
        }
    }
}
