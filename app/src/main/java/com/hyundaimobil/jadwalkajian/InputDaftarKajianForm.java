package com.hyundaimobil.jadwalkajian;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyundaimobil.jadwalkajian._adapter.TaskAdapterMasjid;
import com.hyundaimobil.jadwalkajian._model.ModalTaskMasjid;
import com.hyundaimobil.jadwalkajian.config.Config;
import com.hyundaimobil.jadwalkajian.config.DatePickerFragment;
import com.hyundaimobil.jadwalkajian.config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class InputDaftarKajianForm extends AppCompatActivity {

    private List<ModalTaskMasjid> modalTaskList = new ArrayList<>();
    RecyclerView recyclerView;
    private TaskAdapterMasjid mAdapter;
    private String JSON_STRING, kd_masjid, kd_kajian, ket;
    ImageView offline;
    private TextView etOffline, etServerNotFound, titleForm;
    EditText kdMasjid, kdKajian, tanggalKajian, keterangan;
    Button btnSave, btnCancel;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_daftar_kajian_form);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setIcon(R.mipmap.mainlogo3);
        //actionBar.setHomeAsUpIndicator(R.mipmap.mainlogo4);

        Intent intent   = getIntent();
        kd_masjid       = intent.getStringExtra(Config.DISP_KD_MASJID);
        kd_kajian       = intent.getStringExtra(Config.DISP_KD_KAJIAN);
        //ket             = intent.getStringExtra(Config.DISP_KETERANGAN);

        kdMasjid        = (EditText) findViewById(R.id.kdMasjid);
        kdKajian        = (EditText) findViewById(R.id.kdKajian);
        tanggalKajian   = (EditText) findViewById(R.id.tanggalKajian);
        keterangan      = (EditText) findViewById(R.id.keterangan);
        titleForm       = (TextView) findViewById(R.id.titleForm);

        btnSave         = (Button) findViewById(R.id.btnSave);
        btnCancel       = (Button) findViewById(R.id.btnCancel);

        if(Config.CEK_KONEKSI(InputDaftarKajianForm.this)) {
            kdKajian.setText(kd_kajian);
            kdMasjid.setText(kd_masjid);
            if(!kd_kajian.equals("0")){
                //keterangan.setText(ket);
                getJSON_showKajian();
            }
        } else {
            //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }



    private void getJSON_showKajian(){
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InputDaftarKajianForm.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(DaftarMasjid.this, s, Toast.LENGTH_SHORT).show();
                showDataKajian(s);
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_KD_MASJID, kd_masjid);
                params.put(Config.DISP_KD_KAJIAN, kd_kajian);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_ALL_KAJIAN, params);
                return res;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDataKajian(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject jo               = result.getJSONObject(0);
            String tanggal_kajian       = jo.getString(Config.DISP_TANGGAL);
            String keterangan_kajian    = jo.getString(Config.DISP_KETERANGAN);

            tanggalKajian.setText(tanggal_kajian);
            keterangan.setText(keterangan_kajian);
            titleForm.setText("Edit Kajian");

        } catch (JSONException e){
            e.printStackTrace();
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }



    private void kajianSave(View v){
        final String kode_kajian        = kdKajian.getText().toString().trim();
        final String kode_masjid        = kdMasjid.getText().toString().trim();
        final String tanggal_kajian     = tanggalKajian.getText().toString().trim();
        final String keterangan_kajian  = keterangan.getText().toString().trim();

        if(tanggal_kajian.equals("")) {
            Snackbar.make(v, Config.ALERT_TANGGAL_KAJIAN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            tanggalKajian.requestFocus();

        } else if(keterangan_kajian.equals("")) {
            Snackbar.make(v, Config.ALERT_KETERANGAN_KAJIAN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            keterangan.requestFocus();

        } else {
            class saveKajian extends AsyncTask<Void, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(InputDaftarKajianForm.this, "", Config.ALERT_PLEASE_WAIT, false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(InputDaftarKajianForm.this, s, Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> hashMap = new HashMap<>();

                    //from session
                    hashMap.put(Config.DISP_KD_MASJID, kode_masjid);
                    hashMap.put(Config.DISP_KD_KAJIAN, kode_kajian);
                    hashMap.put(Config.DISP_TANGGAL, tanggal_kajian);
                    hashMap.put(Config.DISP_KETERANGAN, keterangan_kajian);
                    hashMap.put(Config.DISP_KD_TOMBOL, "input");

                    RequestHandler rh = new RequestHandler();
                    String s = rh.sendPostRequest(Config.URL_ADD_KAJIAN, hashMap);
                    return s;
                }
            }

            saveKajian usa = new saveKajian();
            usa.execute();


        }
    }



    public void onClick(View v){
        if(Config.CEK_KONEKSI(InputDaftarKajianForm.this)) {
            if (v == btnSave) {
                kajianSave(v);
            }else if (v == btnCancel) {
                onBackPressed();
            }else if(v == tanggalKajian){
                showDatePicker();
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

    public void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "DatePicker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int bulan           = month+1;
            String day_string   = String.valueOf(day);
            String month_string = String.valueOf(bulan);

            if(day < 10){
                day_string = "0" + String.valueOf(day);
            }
            if(bulan < 10){
                month_string = "0" + String.valueOf(bulan);
            }

            String stringOfDate = year + "-" + month_string + "-" + day_string;

            if(Config.CEK_KONEKSI(InputDaftarKajianForm.this)) {

                tanggalKajian.setText(stringOfDate);
                //getJSON_showKajian(stringOfDate);
            } else {
                //onCreateDialog(tampil_error);
                //recyclerView.setBackgroundResource(R.drawable.ic_offline_black_24dp);
                //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }


            //Toast.makeText(DaftarKajian.this, stringOfDate, Toast.LENGTH_SHORT).show();
        }
    };

}
