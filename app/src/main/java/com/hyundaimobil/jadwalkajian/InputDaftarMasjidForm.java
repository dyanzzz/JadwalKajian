package com.hyundaimobil.jadwalkajian;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyundaimobil.jadwalkajian._adapter.TaskAdapterMasjid;
import com.hyundaimobil.jadwalkajian._model.ModalTaskMasjid;
import com.hyundaimobil.jadwalkajian.config.Config;
import com.hyundaimobil.jadwalkajian.config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputDaftarMasjidForm extends AppCompatActivity {

    private List<ModalTaskMasjid> modalTaskList = new ArrayList<>();
    RecyclerView recyclerView;
    private TaskAdapterMasjid mAdapter;
    private String JSON_STRING, kd_masjid, masjid, kd_wilayah;
    ImageView offline;
    private TextView txtDate, etOffline, etServerNotFound, titleForm;
    EditText kdWilayah, kdMasjid, namaMasjid, alamat, informasi, linkMaps, latitude, longtitude;
    Button btnSave, btnCancel;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_daftar_masjid_form);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setIcon(R.mipmap.mainlogo3);
        //actionBar.setHomeAsUpIndicator(R.mipmap.mainlogo4);

        Intent intent   = getIntent();
        kd_wilayah      = intent.getStringExtra(Config.DISP_KD_WILAYAH);
        kd_masjid       = intent.getStringExtra(Config.DISP_KD_MASJID);
        masjid          = intent.getStringExtra(Config.DISP_NAMA_MASJID);

        kdWilayah       = (EditText) findViewById(R.id.kdWilayah);
        kdMasjid        = (EditText) findViewById(R.id.kdMasjid);
        namaMasjid      = (EditText) findViewById(R.id.namaMasjid);
        alamat          = (EditText) findViewById(R.id.alamat);
        informasi       = (EditText) findViewById(R.id.informasi);
        linkMaps        = (EditText) findViewById(R.id.linkMaps);
        //latitude        = (EditText) findViewById(R.id.latitude);
        //longtitude      = (EditText) findViewById(R.id.longtitude);
        titleForm       = (TextView) findViewById(R.id.titleForm);

        btnSave         = (Button) findViewById(R.id.btnSave);
        btnCancel       = (Button) findViewById(R.id.btnCancel);

        if(Config.CEK_KONEKSI(InputDaftarMasjidForm.this)) {
            kdMasjid.setText(kd_masjid);
            kdWilayah.setText(kd_wilayah);
            if(!kd_masjid.equals("0")){
                getJSON_showMasjid();
            }
        } else {
            //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }



    private void getJSON_showMasjid(){
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InputDaftarMasjidForm.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(DaftarMasjid.this, s, Toast.LENGTH_SHORT).show();
                showDataMasjid(s);
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_KD_MASJID, kd_masjid);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_ALL_MASJID, params);
                return res;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDataMasjid(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject jo           = result.getJSONObject(0);
            String masjid           = jo.getString(Config.DISP_NAMA_MASJID);
            String address          = jo.getString(Config.DISP_ALAMAT_MASJID);
            String informasi_masjid = jo.getString(Config.DISP_INFORMASI);
            String link_maps        = jo.getString(Config.DISP_LINK_MAPS);
            //String lat              = jo.getString(Config.DISP_LAT);
            //String lng              = jo.getString(Config.DISP_LNG);

            namaMasjid.setText(masjid);
            alamat.setText(address);
            informasi.setText(informasi_masjid);
            linkMaps.setText(link_maps);
            //latitude.setText(lat);
            //longtitude.setText(lng);
            titleForm.setText("Edit Masjid");

        } catch (JSONException e){
            e.printStackTrace();
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }



    private void masjidSave(View v){
        final String kode_wilayah       = kdWilayah.getText().toString().trim();
        final String kode_masjid        = kdMasjid.getText().toString().trim();
        final String nama_masjid        = namaMasjid.getText().toString().trim();
        final String alamat_masjid      = alamat.getText().toString().trim();
        final String informasi_masjid   = informasi.getText().toString().trim();
        final String link_maps          = linkMaps.getText().toString().trim();
        //final String latitude_masjid    = latitude.getText().toString().trim();
        //final String longtitude_masjid  = longtitude.getText().toString().trim();

        if(nama_masjid.equals("")) {
            Snackbar.make(v, Config.ALERT_NAMA_MASJID, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            namaMasjid.requestFocus();

        } else if(alamat_masjid.equals("")) {
            Snackbar.make(v, Config.ALERT_ALAMAT, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            alamat.requestFocus();

        } else if(informasi_masjid.equals("")) {
            Snackbar.make(v, Config.ALERT_INFORMASI, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            informasi.requestFocus();

        } else if(link_maps.equals("")) {
            Snackbar.make(v, Config.ALERT_LINK_MAPS, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            linkMaps.requestFocus();

        } else {
            class saveMasjid extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(InputDaftarMasjidForm.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(InputDaftarMasjidForm.this, s, Toast.LENGTH_LONG).show();
                    loading.dismiss();

                    onBackPressed();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();

                    //from session
                    hashMap.put(Config.DISP_KD_MASJID, kode_masjid);
                    hashMap.put(Config.DISP_NAMA_MASJID, nama_masjid);
                    hashMap.put(Config.DISP_ALAMAT_MASJID, alamat_masjid);
                    hashMap.put(Config.DISP_INFORMASI, informasi_masjid);
                    hashMap.put(Config.DISP_LINK_MAPS, link_maps);

                    hashMap.put(Config.DISP_KD_TOMBOL, "input");
                    hashMap.put(Config.DISP_KD_WILAYAH, kode_wilayah);

                    RequestHandler rh = new RequestHandler();

                    String s = rh.sendPostRequest(Config.URL_ADD_MASJID, hashMap);
                    return s;
                }
            }

            saveMasjid usa = new saveMasjid();
            usa.execute();
        }
    }



    public void onClick(View v){
        if(Config.CEK_KONEKSI(InputDaftarMasjidForm.this)) {
            if (v == btnSave) {
                masjidSave(v);
            }else if (v == btnCancel) {
                onBackPressed();
            }
        }else{
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    //controll tombol toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
