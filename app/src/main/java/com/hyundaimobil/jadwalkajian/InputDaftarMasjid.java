package com.hyundaimobil.jadwalkajian;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyundaimobil.jadwalkajian._adapter.RecyclerTouchListener;
import com.hyundaimobil.jadwalkajian._adapter.TaskAdapterMasjid;
import com.hyundaimobil.jadwalkajian._model.ModalTaskMasjid;
import com.hyundaimobil.jadwalkajian.config.Config;
import com.hyundaimobil.jadwalkajian.config.RequestHandler;
import com.hyundaimobil.jadwalkajian.config.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputDaftarMasjid extends AppCompatActivity {

    private List<ModalTaskMasjid> modalTaskList = new ArrayList<>();
    RecyclerView recyclerView;
    private TaskAdapterMasjid mAdapter;
    private String JSON_STRING, kd_wilayah, wilayah;
    private TextView txtDate;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AlertDialog.Builder dialog;

    SessionManager session;
    String kdUsers, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_daftar_masjid);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(R.mipmap.mainlogo3);
        //actionBar.setHomeAsUpIndicator(R.mipmap.mainlogo4);

        session = new SessionManager(InputDaftarMasjid.this);
        String statusLogin = String.valueOf(session.isLoggedIn());
        if(statusLogin.equals("false")){
            onBackPressed();
            Intent intent = new Intent(InputDaftarMasjid.this, MainActivity.class);
            startActivity(intent);
        }
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        kdUsers = user.get(SessionManager.KEY_USERCODE);
        nama    = user.get(SessionManager.KEY_NAMAUSER);

        Intent intent   = getIntent();
        //kd_wilayah      = intent.getStringExtra(Config.DISP_KD_WILAYAH);
        //wilayah         = intent.getStringExtra(Config.DISP_WILAYAH);
        kd_wilayah      = "17";
        wilayah         = "Banten";

        recyclerView    = findViewById(R.id.recycler_view);
        txtDate         = findViewById(R.id.tanggal);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if(Config.CEK_KONEKSI(InputDaftarMasjid.this)) {
                    Intent intent = new Intent(InputDaftarMasjid.this, InputDaftarMasjidForm.class);
                    intent.putExtra(Config.DISP_KD_WILAYAH, kd_wilayah);
                    intent.putExtra(Config.DISP_KD_MASJID, "0");
                    intent.putExtra(Config.DISP_NAMA_MASJID, "");
                    startActivity(intent);
                }else{
                    Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        mAdapter = new TaskAdapterMasjid(modalTaskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(InputDaftarMasjid.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(InputDaftarMasjid.this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(Config.CEK_KONEKSI(InputDaftarMasjid.this)) {
                    ModalTaskMasjid modalTask = modalTaskList.get(position);
                    if (!modalTask.getKode().equals("0")) {
                        Intent intent = new Intent(InputDaftarMasjid.this, InputDaftarKajian.class);
                        intent.putExtra(Config.DISP_KD_MASJID, modalTask.getKode());
                        intent.putExtra(Config.DISP_KD_WILAYAH, kd_wilayah);
                        intent.putExtra(Config.DISP_WILAYAH, wilayah);
                        startActivity(intent);
                    } else {
                        Snackbar.make(view, modalTask.getTitle(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }else {
                    Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onLongClick(final View view, int position) {
                if(Config.CEK_KONEKSI(InputDaftarMasjid.this)) {
                    ModalTaskMasjid modalTask = modalTaskList.get(position);
                    if (!modalTask.getKode().equals("0")) {
                        final String kodeMasjid = modalTask.getKode();
                        final String namaMasjid = modalTask.getTitle();

                        final CharSequence[] dialogitem   = {"- Edit Masjid", "- Delete Masjid"};
                        dialog = new AlertDialog.Builder(InputDaftarMasjid.this);
                        dialog.setCancelable(true);
                        dialog.setTitle("Masjid");
                        dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                switch (which) {
                                    case 0:
                                        if(Config.CEK_KONEKSI(InputDaftarMasjid.this)) {
                                            //btn_form_masjid(view, kodeWilayah, namaWilayah);
                                            Intent intent = new Intent(InputDaftarMasjid.this, InputDaftarMasjidForm.class);
                                            intent.putExtra(Config.DISP_KD_WILAYAH, kd_wilayah);
                                            intent.putExtra(Config.DISP_KD_MASJID, kodeMasjid);
                                            intent.putExtra(Config.DISP_NAMA_MASJID, namaMasjid);
                                            startActivity(intent);
                                        }else{
                                            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                        break;
                                    case 1:
                                        if(Config.CEK_KONEKSI(InputDaftarMasjid.this)) {
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InputDaftarMasjid.this);
                                            alertDialogBuilder.setMessage("Are you sure to delete "+namaMasjid+"?");
                                            alertDialogBuilder.setCancelable(false);
                                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    //Back to Login.class
                                                    btnDelete(kodeMasjid, "delete");
                                                }
                                            });

                                            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {}
                                            });

                                            //Showing the alert dialog
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();

                                            //Snackbar.make(view, kodeWilayah, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            //ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                            //ClipData clip = ClipData.newPlainText(Config.TAG_PHONE, phone_number_cust);
                                            //clipboard.setPrimaryClip(clip);
                                            //Toast.makeText(getApplicationContext(), "Phone Number Copied to Clipboard", Toast.LENGTH_LONG).show();
                                        }else{
                                            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                        break;
                                }
                            }
                        }).show();

                    } else {
                        //Toast.makeText(getActivity(), modalTask.getTitle(), Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, modalTask.getTitle(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }else{
                    //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        }));

        //untuk refresh
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Config.CEK_KONEKSI(InputDaftarMasjid.this)) {
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

        if(Config.CEK_KONEKSI(InputDaftarMasjid.this)) {
            //recyclerView.setVisibility(View.VISIBLE);
            String stringOfDate = "";
            String kdMasjid = "";
            getViewTanggalKajian(stringOfDate, kdMasjid);
            getJSON_showMasjid();
        } else {
            //recyclerView.setVisibility(View.GONE);
            //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void btnDelete(final String kodeMasjid, final String kodeTombol) {
        class AddWilayah extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InputDaftarMasjid.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(InputDaftarMasjid.this, s, Toast.LENGTH_LONG).show();

                modalTaskList.clear();
                getJSON_showMasjid();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                //from form input
                params.put(Config.DISP_KD_MASJID, kodeMasjid);
                params.put(Config.DISP_KD_TOMBOL, kodeTombol);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_MASJID, params);
                return res;
            }
        }

        AddWilayah aw = new AddWilayah();
        aw.execute();


    }

    private void getViewTanggalKajian(final String pilihTanggal, final String kdMasjid){
        class GetSalesmanActivity extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(InputDaftarMasjid.this, "", Config.ALERT_PLEASE_WAIT, false, false);
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
                loading = ProgressDialog.show(InputDaftarMasjid.this, "", Config.ALERT_PLEASE_WAIT, false, false);
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
                params.put(Config.DISP_TANDAI_FORM_INPUT, "formInput");

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
    /*@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }*/

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    //controll tombol toolbar
    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        // TODO Add your menu entries here
        getMenuInflater().inflate(R.menu.navcategorytoolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                logout();
                break;
        }
        return true;

    }

    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InputDaftarMasjid.this);
        alertDialogBuilder.setMessage("Are you sure want to Logout?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Back to Login.class
                onBackPressed();
            }

        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(InputDaftarMasjid.this, "Welcome "+nama, Toast.LENGTH_SHORT).show();
            }
        });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
