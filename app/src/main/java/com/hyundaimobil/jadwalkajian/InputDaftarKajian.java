package com.hyundaimobil.jadwalkajian;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.hyundaimobil.jadwalkajian._adapter.RecyclerTouchListener;
import com.hyundaimobil.jadwalkajian._adapter.TaskAdapterKajian;
import com.hyundaimobil.jadwalkajian._model.ModalTaskKajian;
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

public class InputDaftarKajian extends AppCompatActivity {

    private List<ModalTaskKajian> modalTaskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapterKajian mAdapter;
    private String JSON_STRING, kd_masjid, kd_wilayah, wilayah;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView txtDate, txtMasjid, txtAlamat;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_daftar_kajian);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setIcon(R.mipmap.mainlogo3);
        //actionBar.setHomeAsUpIndicator(R.mipmap.mainlogo4);

        Intent intent   = getIntent();
        kd_masjid       = intent.getStringExtra(Config.DISP_KD_MASJID);
        kd_wilayah      = intent.getStringExtra(Config.DISP_KD_WILAYAH);
        wilayah         = intent.getStringExtra(Config.DISP_WILAYAH);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Config.CEK_KONEKSI(InputDaftarKajian.this)) {
                    //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Intent intent = new Intent(InputDaftarKajian.this, InputDaftarKajianForm.class);
                    intent.putExtra(Config.DISP_KD_MASJID, kd_masjid);
                    intent.putExtra(Config.DISP_KD_KAJIAN, "0");
                    //intent.putExtra(Config.DISP_KD_TOMBOL, "");
                    startActivity(intent);
                }else{
                    Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txtDate     = (TextView) findViewById(R.id.tanggal);
        txtMasjid   = (TextView) findViewById(R.id.masjid);
        txtAlamat   = (TextView) findViewById(R.id.alamat);

        mAdapter = new TaskAdapterKajian(modalTaskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(InputDaftarKajian.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.destroyDrawingCache();
        imagePopup.clearFocus();
        imagePopup.setBackgroundColor(Color.WHITE);
//        imagePopup.setWindowHeight(800);
//        imagePopup.setWindowWidth(800);
        imagePopup.setHideCloseIcon(true);
        imagePopup.setImageOnClickClose(true);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(InputDaftarKajian.this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(Config.CEK_KONEKSI(InputDaftarKajian.this)) {
                    ModalTaskKajian modalTask = modalTaskList.get(position);
                    if(!modalTask.getKode().equals("0")) {
                        final String photoUrl = modalTask.getImage();
                        imagePopup.initiatePopupWithPicasso(photoUrl);
                        imagePopup.viewPopup();
                    }else{
                        Snackbar.make(view, modalTask.getKeterangan(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        //Toast.makeText(DaftarKajian.this, modalTask.getPemateri(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //recyclerView.setBackgroundResource(R.drawable.ic_offline_black_24dp);
                    //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if(Config.CEK_KONEKSI(InputDaftarKajian.this)) {
                    ModalTaskKajian modalTask = modalTaskList.get(position);
                    if (!modalTask.getKode().equals("0")) {
                        final String kodeKajian = modalTask.getKode();
                        final String keterangan = modalTask.getKeterangan();

                        final CharSequence[] dialogitem   = {"- Edit Kajian", "- Delete Kajian", "- Input Brosur"};
                        dialog = new AlertDialog.Builder(InputDaftarKajian.this);
                        dialog.setCancelable(true);
                        dialog.setTitle("Kajian");
                        dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                switch (which) {
                                    case 0:
                                        if(Config.CEK_KONEKSI(InputDaftarKajian.this)) {
                                            //btn_form_masjid(view, kodeWilayah, namaWilayah);
                                            Intent intent = new Intent(InputDaftarKajian.this, InputDaftarKajianForm.class);
                                            intent.putExtra(Config.DISP_KD_MASJID, kd_masjid);
                                            intent.putExtra(Config.DISP_KD_KAJIAN, kodeKajian);
                                            //intent.putExtra(Config.DISP_KETERANGAN, keterangan);
                                            startActivity(intent);
                                        }else{
                                            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                        break;
                                    case 1:
                                        if(Config.CEK_KONEKSI(InputDaftarKajian.this)) {
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InputDaftarKajian.this);
                                            alertDialogBuilder.setMessage("Are you sure to delete?");
                                            alertDialogBuilder.setCancelable(false);
                                            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    //Back to Login.class
                                                    btnDelete(kodeKajian, "delete");
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
                                    case 2:
                                        if(Config.CEK_KONEKSI(InputDaftarKajian.this)) {
                                            Intent intent = new Intent(InputDaftarKajian.this, InputDaftarKajianFormImg.class);
                                            intent.putExtra(Config.DISP_KD_KAJIAN, kodeKajian);
                                            startActivity(intent);
                                        }else{
                                            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                        break;
                                }
                            }
                        }).show();

                    } else {
                        //Toast.makeText(getActivity(), modalTask.getTitle(), Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, modalTask.getKeterangan(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
                if(Config.CEK_KONEKSI(InputDaftarKajian.this)) {
                    modalTaskList.clear();
                    String stringOfDate = "";
                    getJSON_showKajian(stringOfDate);
                    getJSON_showMasjid();
                    mSwipeRefreshLayout.setRefreshing(false);
                }else{
                    modalTaskList.clear();
                    Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        if(Config.CEK_KONEKSI(InputDaftarKajian.this)) {
            String stringOfDate = "";
            getJSON_showKajian(stringOfDate);
            getJSON_showMasjid();
        } else {
            //onCreateDialog(tampil_error);
            //recyclerView.setBackgroundResource(R.drawable.ic_offline_black_24dp);
            //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void btnDelete(final String kodeKajian, final String kodeTombol) {
        class AddKajian extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InputDaftarKajian.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(InputDaftarKajian.this, s, Toast.LENGTH_LONG).show();

                modalTaskList.clear();
                String stringOfDate = "";
                getJSON_showKajian(stringOfDate);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                //from form input
                params.put(Config.DISP_KD_KAJIAN, kodeKajian);
                params.put(Config.DISP_KD_TOMBOL, kodeTombol);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_KAJIAN, params);
                return res;
            }
        }

        AddKajian aw = new AddKajian();
        aw.execute();


    }

    private void getJSON_showMasjid(){
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InputDaftarKajian.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(DaftarKajian.this, s, Toast.LENGTH_SHORT).show();
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
            JSONObject jsonObject   = new JSONObject(json);
            JSONArray result        = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c            = result.getJSONObject(0);
            String kd_masjid        = c.getString(Config.DISP_KD_MASJID);
            String nama             = c.getString(Config.DISP_NAMA_MASJID);
            String alamat           = c.getString(Config.DISP_ALAMAT_MASJID);

            txtMasjid.setText(nama);
            txtAlamat.setText(alamat);

        } catch (JSONException e){
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    public void getJSON_showKajian(final String stringOfDate){
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(InputDaftarKajian.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                //Toast.makeText(DaftarKajian.this, s, Toast.LENGTH_SHORT).show();
                showDataKajian();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_KD_MASJID, kd_masjid);
                params.put(Config.DISP_TANGGAL, stringOfDate);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_ALL_KAJIAN, params);
                return res;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDataKajian(){
        JSONObject jsonObject = null;
        //ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try{
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            modalTaskList.clear();

            for(int i=0; i<result.length(); i++){
                JSONObject jo       = result.getJSONObject(i);
                String nomor        = jo.getString(Config.DISP_NOMOR);
                String kd_kajian    = jo.getString(Config.DISP_KD_KAJIAN);
                //String pemateri     = jo.getString(Config.DISP_PEMATERI);
                //String tema         = "Tema. " + jo.getString(Config.DISP_TEMA);
                //String waktu        = "Waktu. " + jo.getString(Config.DISP_WAKTU);
                String keterangan   = jo.getString(Config.DISP_KETERANGAN);
                String tanggal      = jo.getString(Config.DISP_TANGGAL);
                String image        = jo.getString(Config.DISP_IMAGE);

                txtDate.setText(tanggal + " ( " + wilayah + " )");

                //int id = getResources().getIdentifier("ic_flag_red_24dp", "drawable", getContext().getPackageName());
                //ModalTaskKajian modalTask = new ModalTaskKajian(nomor, pemateri, tema, waktu, keterangan, kd_kajian, image);
                ModalTaskKajian modalTask = new ModalTaskKajian(nomor, keterangan, kd_kajian, image);
                modalTaskList.add(modalTask);

            }

        } catch (JSONException e){
            e.printStackTrace();
            Snackbar.make(recyclerView, Config.ALERT_MESSAGE_SRV_NOT_FOUND, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        mAdapter.notifyDataSetChanged();

    }




    private void showDatePicker() {
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

            if(Config.CEK_KONEKSI(InputDaftarKajian.this)) {
                getJSON_showKajian(stringOfDate);
            } else {
                //onCreateDialog(tampil_error);
                //recyclerView.setBackgroundResource(R.drawable.ic_offline_black_24dp);
                //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                Snackbar.make(recyclerView, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }


            //Toast.makeText(DaftarKajian.this, stringOfDate, Toast.LENGTH_SHORT).show();
        }
    };


    //controll tombol toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.nav_calender:
                showDatePicker();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    //controll memanggil item toolbar untuk menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.navtoolbar, menu);
        return true;
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
