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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyundaimobil.jadwalkajian._adapter.RecyclerTouchListener;
import com.hyundaimobil.jadwalkajian._adapter.TaskAdapter;
import com.hyundaimobil.jadwalkajian._model.ModalTask;
import com.hyundaimobil.jadwalkajian.config.Config;
import com.hyundaimobil.jadwalkajian.config.RequestHandler;
import com.hyundaimobil.jadwalkajian.config.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputDaftarWilayah extends AppCompatActivity {
    private List<ModalTask> modalTaskList = new ArrayList<>();
    RecyclerView recyclerView;
    private TaskAdapter mAdapter;
    private String JSON_STRING, formInputWilayah, versi;
    ImageView offline;
    private TextView txtDate, etOffline, etServerNotFound;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText formWilayah;

    SessionManager session;
    String kdUsers, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_daftar_wilayah);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(R.mipmap.mainlogo3);
        //actionBar.setHomeAsUpIndicator(R.mipmap.mainlogo4);

        session = new SessionManager(InputDaftarWilayah.this);
        String statusLogin = String.valueOf(session.isLoggedIn());
        if(statusLogin.equals("false")){
            onBackPressed();
            Intent intent = new Intent(InputDaftarWilayah.this, MainActivity.class);
            startActivity(intent);
        }
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        kdUsers = user.get(SessionManager.KEY_USERCODE);
        nama    = user.get(SessionManager.KEY_NAMAUSER);

        recyclerView        = (RecyclerView) findViewById(R.id.recycler_view);
        offline             = (ImageView) findViewById(R.id.offline);
        etOffline           = (TextView) findViewById(R.id.etOffline);
        etServerNotFound    = (TextView) findViewById(R.id.etServerNotFound);
        txtDate             = (TextView) findViewById(R.id.tanggal);

        versi = Config.VALUE_VERSI;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if(Config.CEK_KONEKSI(InputDaftarWilayah.this)) {
                    ModalTask modalTask = modalTaskList.get(0);
                    if (!modalTask.getKode().equals("0")) {
                        //Intent intent = new Intent(InputDaftarWilayah.this, InputDaftarWilayahForm.class);
                        //startActivity(intent);
                        btn_form_wilayah(view, "0", "");
                    }else{
                        Snackbar.make(view, modalTask.getTitle(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }else{
                    Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        mAdapter = new TaskAdapter(modalTaskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(InputDaftarWilayah.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(InputDaftarWilayah.this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(Config.CEK_KONEKSI(InputDaftarWilayah.this)) {
                    ModalTask modalTask = modalTaskList.get(position);
                    if (!modalTask.getKode().equals("0")) {
                        Intent intent = new Intent(InputDaftarWilayah.this, InputDaftarMasjid.class);
                        intent.putExtra(Config.DISP_KD_WILAYAH, modalTask.getKode());
                        intent.putExtra(Config.DISP_WILAYAH, modalTask.getTitle());
                        startActivity(intent);
                    }else{
                        Snackbar.make(view, modalTask.getTitle(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }else{
                    Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onLongClick(final View view, int position) {
                if(Config.CEK_KONEKSI(InputDaftarWilayah.this)) {
                    ModalTask modalTask = modalTaskList.get(position);
                    if (!modalTask.getKode().equals("0")) {
                        final String kodeWilayah = modalTask.getKode();
                        final String namaWilayah = modalTask.getTitle();

                        final CharSequence[] dialogitem   = {"- Edit Wilayah", "- Delete Wilayah"};
                        dialog = new AlertDialog.Builder(InputDaftarWilayah.this);
                        dialog.setCancelable(true);
                        dialog.setTitle("Wilayah");
                        dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                switch (which) {
                                    case 0:
                                        if(Config.CEK_KONEKSI(InputDaftarWilayah.this)) {
                                            btn_form_wilayah(view, kodeWilayah, namaWilayah);
                                        }else{
                                            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                        break;
                                    case 1:
                                        if(Config.CEK_KONEKSI(InputDaftarWilayah.this)) {
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InputDaftarWilayah.this);
                                            alertDialogBuilder.setMessage("Are you sure to delete "+namaWilayah+"?");
                                            alertDialogBuilder.setCancelable(false);
                                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    //Back to Login.class
                                                    if(Config.CEK_KONEKSI(InputDaftarWilayah.this)) {
                                                        btnSave("", kodeWilayah, "delete");
                                                    }else{
                                                        Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                    }
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Config.CEK_KONEKSI(InputDaftarWilayah.this)) {
                    modalTaskList.clear();
                    recyclerView.setVisibility(View.VISIBLE);
                    offline.setVisibility(View.GONE);
                    etOffline.setVisibility(View.GONE);
                    etServerNotFound.setVisibility(View.GONE);
                    String stringOfDate = "";
                    String kdMasjid = "";
                    getViewTanggalKajian(stringOfDate, kdMasjid);
                    getJSON_showWilayah();
                    mSwipeRefreshLayout.setRefreshing(false);
                }else{
                    modalTaskList.clear();
                    recyclerView.setVisibility(View.GONE);
                    offline.setVisibility(View.VISIBLE);
                    etOffline.setVisibility(View.VISIBLE);
                    etServerNotFound.setVisibility(View.GONE);
                    //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                    Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        if(Config.CEK_KONEKSI(InputDaftarWilayah.this)) {
            recyclerView.setVisibility(View.VISIBLE);
            offline.setVisibility(View.GONE);
            etOffline.setVisibility(View.GONE);
            etServerNotFound.setVisibility(View.GONE);
            String stringOfDate = "";
            String kdMasjid = "";
            getViewTanggalKajian(stringOfDate, kdMasjid);
            getJSON_showWilayah();
        } else {
            recyclerView.setVisibility(View.GONE);
            offline.setVisibility(View.VISIBLE);
            etOffline.setVisibility(View.VISIBLE);
            etServerNotFound.setVisibility(View.GONE);
            //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    //Get ID tombol & Event Tombol
    public void btn_form_wilayah(View view, String kodeWilayah, String namaWilayah) {
        if(Config.CEK_KONEKSI(InputDaftarWilayah.this)) {
            dialogFormInputWilayah(kodeWilayah, namaWilayah);
        }else{
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void dialogFormInputWilayah(final String kodeWilayah, String namaWilayah) {
        dialog      = new AlertDialog.Builder(InputDaftarWilayah.this);
        inflater    = getLayoutInflater();
        dialogView  = inflater.inflate(R.layout.activity_input_daftar_wilayah_form, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_add_black_24dp);
        dialog.setTitle(Config.TITLE_DISP_WILAYAH);

        formWilayah    = (EditText) dialogView.findViewById(R.id.editTextFormWilayah);
        if(kodeWilayah.equals("0")){
            formWilayah.setText("");
        }else{
            formWilayah.setText(namaWilayah);
        }

        dialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Config.CEK_KONEKSI(InputDaftarWilayah.this)) {
                    formInputWilayah          = formWilayah.getText().toString().trim();
                    if (formInputWilayah.length() > 0) {
                        btnSave(formInputWilayah, kodeWilayah, "");
                    } else {
                        Snackbar.make(recyclerView, Config.HARUS_DIISI, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }else{
                    Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        /*
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        */

        dialog.show();
    }

    private void getJSON_showWilayah(){
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InputDaftarWilayah.this, Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                showDataWilayah();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_VERSI, versi);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_ALL_WILAYAH, params);
                return res;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDataWilayah(){
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i=0; i<result.length(); i++){
                JSONObject jo       = result.getJSONObject(i);
                String nomor        = jo.getString(Config.DISP_NOMOR);
                String wilayah      = jo.getString(Config.DISP_WILAYAH);
                String kd_wilayah   = jo.getString(Config.DISP_KD_WILAYAH);
                String count        = jo.getString(Config.DISP_COUNT);

                int id = getResources().getIdentifier("ic_flag_red_24dp", "drawable", InputDaftarWilayah.this.getPackageName());
                ModalTask modalTask = new ModalTask(nomor, wilayah, kd_wilayah, count);
                modalTaskList.add(modalTask);

            }

        } catch (JSONException e){
            e.printStackTrace();
            recyclerView.setVisibility(View.GONE);
            offline.setVisibility(View.VISIBLE);
            etOffline.setVisibility(View.GONE);
            etServerNotFound.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();

    }

    private void btnSave(final String namaWilayah, final String kodeWilayah, final String kodeTombol) {
        class AddWilayah extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InputDaftarWilayah.this, Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(InputDaftarWilayah.this, s, Toast.LENGTH_LONG).show();

                modalTaskList.clear();
                getJSON_showWilayah();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                //from form input
                params.put(Config.DISP_WILAYAH, namaWilayah);
                params.put(Config.DISP_KD_WILAYAH, kodeWilayah);
                params.put(Config.DISP_KD_TOMBOL, kodeTombol);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_WILAYAH, params);
                return res;
            }
        }

        AddWilayah aw = new AddWilayah();
        aw.execute();

        //Toast.makeText(InputActivity.this, "Salesman Activity Berhasil Diinput", Toast.LENGTH_SHORT).show();
        //onBackPressed();


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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InputDaftarWilayah.this);
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
                Toast.makeText(InputDaftarWilayah.this, "Welcome "+nama, Toast.LENGTH_SHORT).show();
            }
        });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void getViewTanggalKajian(final String pilihTanggal, final String kdMasjid){
        class GetSalesmanActivity extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(InputDaftarWilayah.this, Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
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

            txtDate.setText(tanggal);

        } catch (JSONException e){
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
