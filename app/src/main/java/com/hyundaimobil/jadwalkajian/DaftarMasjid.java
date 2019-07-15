package com.hyundaimobil.jadwalkajian;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hyundaimobil.jadwalkajian._adapter.RecyclerTouchListener;
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

public class DaftarMasjid extends AppCompatActivity {

    private List<ModalTaskMasjid> modalTaskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapterMasjid mAdapter;
    private String JSON_STRING, kd_wilayah, wilayah;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView txtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_masjid);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(R.mipmap.mainlogo3);
        //actionBar.setHomeAsUpIndicator(R.mipmap.mainlogo4);

        Intent intent   = getIntent();
        //kd_wilayah      = intent.getStringExtra(Config.DISP_KD_WILAYAH);
        //wilayah         = intent.getStringExtra(Config.DISP_WILAYAH);
        kd_wilayah      = "17";
        wilayah         = "Serang";

        //View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView    = findViewById(R.id.recycler_view);
        txtDate         = findViewById(R.id.tanggal);

        mAdapter = new TaskAdapterMasjid(modalTaskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DaftarMasjid.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(DaftarMasjid.this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(Config.CEK_KONEKSI(DaftarMasjid.this)) {
                    ModalTaskMasjid modalTask = modalTaskList.get(position);

                    if (!modalTask.getKode().equals("0")) {
                        Intent intent = new Intent(DaftarMasjid.this, DaftarKajian.class);
                        intent.putExtra(Config.DISP_KD_MASJID, modalTask.getKode());
                        intent.putExtra(Config.DISP_LINK_MAPS, modalTask.getLink_maps());
                        intent.putExtra(Config.DISP_KD_WILAYAH, kd_wilayah);
                        intent.putExtra(Config.DISP_WILAYAH, wilayah);
                        startActivity(intent);
                    } else {
                        //Toast.makeText(DaftarMasjid.this, modalTask.getTitle(), Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, modalTask.getTitle(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }else{
                    //recyclerView.setBackgroundResource(R.drawable.ic_offline_black_24dp);
                    //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //untuk refresh
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Config.CEK_KONEKSI(DaftarMasjid.this)) {
                    modalTaskList.clear();
                    String stringOfDate = "";
                    String kdMasjid = "";
                    getViewTanggalKajian(stringOfDate, kdMasjid);
                    getJSON_showMasjid();
                    mSwipeRefreshLayout.setRefreshing(false);
                }else{
                    modalTaskList.clear();
                    Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        if(Config.CEK_KONEKSI(DaftarMasjid.this)) {
            String stringOfDate = "";
            String kdMasjid = "";
            getViewTanggalKajian(stringOfDate, kdMasjid);
            getJSON_showMasjid();
        } else {
            //onCreateDialog(tampil_error);
            //recyclerView.setBackgroundResource(R.drawable.ic_offline_black_24dp);
            //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void getViewTanggalKajian(final String pilihTanggal, final String kdMasjid){
        @SuppressLint("StaticFieldLeak")
        class GetSalesmanActivity extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(DaftarMasjid.this, Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                showTanggalKajian(s);
                loading.dismiss();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_TANGGAL, pilihTanggal);
                params.put(Config.DISP_KD_MASJID, kdMasjid);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_ALL_KAJIAN, params);
                return res;
            }
        }
        GetSalesmanActivity gsa = new GetSalesmanActivity();
        gsa.execute();
    }

    private void showTanggalKajian(String json){
        try{
            JSONObject jsonObject   = new JSONObject(json);
            JSONArray result        = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c            = result.getJSONObject(0);
            String tanggal          = c.getString(Config.DISP_TANGGAL);

            txtDate.setText(tanggal + " ( " + wilayah + " )");

        } catch (JSONException e){
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void getJSON_showMasjid(){
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DaftarMasjid.this, Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                //Toast.makeText(DaftarMasjid.this, s, Toast.LENGTH_SHORT).show();
                showDataMasjid();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_KD_WILAYAH, kd_wilayah);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_ALL_MASJID, params);
                return res;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDataMasjid(){
        JSONObject jsonObject = null;
        //ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try{
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i=0; i<result.length(); i++){
                JSONObject jo       = result.getJSONObject(i);
                String nomor        = jo.getString(Config.DISP_NOMOR);
                String masjid       = jo.getString(Config.DISP_NAMA_MASJID);
                String kd_masjid    = jo.getString(Config.DISP_KD_MASJID);
                String address      = jo.getString(Config.DISP_ALAMAT_MASJID);
                String informasi    = jo.getString(Config.DISP_INFORMASI);
                String link_maps    = jo.getString(Config.DISP_LINK_MAPS);
                //String count        = jo.getString(Config.DISP_COUNT);

                //int id = getResources().getIdentifier("ic_flag_red_24dp", "drawable", getContext().getPackageName());
                ModalTaskMasjid modalTask = new ModalTaskMasjid(nomor, masjid, kd_masjid, address, informasi, link_maps);
                modalTaskList.add(modalTask);

            }

        } catch (JSONException e){
            e.printStackTrace();
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        mAdapter.notifyDataSetChanged();

    }


    //controll tombol toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
/*
            case R.id.nav_location:
                Intent intent = new Intent(DaftarMasjid.this, DaftarMasjidMaps.class);
                intent.putExtra(Config.DISP_KD_WILAYAH, kd_wilayah);
                intent.putExtra(Config.DISP_WILAYAH, wilayah);
                startActivity(intent);
                return true;
*/
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }



}
