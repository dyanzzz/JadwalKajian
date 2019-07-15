package com.hyundaimobil.jadwalkajian;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Objects;

public class HomeFragmentDaftarMasjid extends Fragment {
    private List<ModalTaskMasjid> modalTaskList = new ArrayList<>();
    RecyclerView recyclerView;
    private TaskAdapterMasjid mAdapter;
    private String JSON_STRING, versi, kd_wilayah, wilayah;
    ImageView offline;
    private TextView txtDate, etOffline, etServerNotFound;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public HomeFragmentDaftarMasjid() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView        = rootView.findViewById(R.id.recycler_view);
        offline             = rootView.findViewById(R.id.offline);
        etOffline           = rootView.findViewById(R.id.etOffline);
        etServerNotFound    = rootView.findViewById(R.id.etServerNotFound);
        txtDate             = rootView.findViewById(R.id.tanggal);

        kd_wilayah      = "17";
        wilayah         = "Banten";

        mAdapter = new TaskAdapterMasjid(modalTaskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        versi = Config.VALUE_VERSI;

        //untuk refresh
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Config.CEK_KONEKSI(Objects.requireNonNull(getActivity()))) {
                    modalTaskList.clear();
                    recyclerView.setVisibility(View.VISIBLE);
                    offline.setVisibility(View.GONE);
                    etOffline.setVisibility(View.GONE);
                    etServerNotFound.setVisibility(View.GONE);
                    String stringOfDate = "";
                    String kdMasjid = "";
                    getViewTanggalKajian(stringOfDate, kdMasjid);
                    getJSON_showMasjid();
                    //getJSON_showWilayah();
                    mSwipeRefreshLayout.setRefreshing(false);
                }else{
                    modalTaskList.clear();
                    recyclerView.setVisibility(View.GONE);
                    offline.setVisibility(View.VISIBLE);
                    etOffline.setVisibility(View.VISIBLE);
                    etServerNotFound.setVisibility(View.GONE);
                    //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                    Snackbar.make(container, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(Config.CEK_KONEKSI(Objects.requireNonNull(getContext()))) {
                    ModalTaskMasjid modalTask = modalTaskList.get(position);
                    if (!modalTask.getKode().equals("0")) {
                        //Intent intent = new Intent(getActivity(), DaftarMasjid.class);
                        //intent.putExtra(Config.DISP_KD_WILAYAH, modalTask.getKode());
                        //intent.putExtra(Config.DISP_WILAYAH, modalTask.getTitle());
                        //startActivity(intent);

                        Intent intent = new Intent(getActivity(), DaftarKajian.class);
                        intent.putExtra(Config.DISP_KD_MASJID, modalTask.getKode());
                        intent.putExtra(Config.DISP_LINK_MAPS, modalTask.getLink_maps());
                        intent.putExtra(Config.DISP_KD_WILAYAH, kd_wilayah);
                        intent.putExtra(Config.DISP_WILAYAH, wilayah);
                        startActivity(intent);
                    } else {
                        //Toast.makeText(getActivity(), modalTask.getTitle(), Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, modalTask.getTitle(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }else{
                    //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));

        if(Config.CEK_KONEKSI(Objects.requireNonNull(getContext()))) {
            recyclerView.setVisibility(View.VISIBLE);
            offline.setVisibility(View.GONE);
            etOffline.setVisibility(View.GONE);
            etServerNotFound.setVisibility(View.GONE);
            String stringOfDate = "";
            String kdMasjid = "";
            getViewTanggalKajian(stringOfDate, kdMasjid);
            getJSON_showMasjid();
            //getJSON_showWilayah();
        } else {
            recyclerView.setVisibility(View.GONE);
            offline.setVisibility(View.VISIBLE);
            etOffline.setVisibility(View.VISIBLE);
            etServerNotFound.setVisibility(View.GONE);
            //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            Snackbar.make(container, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        return rootView;
    }



    private void getJSON_showWilayah(){
        @SuppressLint("StaticFieldLeak")
        class GetJSON extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
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
                return rh.sendPostRequest(Config.URL_GET_ALL_WILAYAH, params);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showDataWilayah(){
        JSONObject jsonObject;
        try{
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i=0; i<result.length(); i++){
                JSONObject jo       = result.getJSONObject(i);
                String nomor        = jo.getString(Config.DISP_NOMOR);
                String wilayah      = jo.getString(Config.DISP_WILAYAH);
                String kd_wilayah   = jo.getString(Config.DISP_KD_WILAYAH);
                String count        = jo.getString(Config.DISP_COUNT);

                int id = getResources().getIdentifier("ic_flag_red_24dp", "drawable", getContext().getPackageName());
                //ModalTask modalTask = new ModalTask(nomor, wilayah, kd_wilayah, count);
                //modalTaskList.add(modalTask);

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

    private void getViewTanggalKajian(final String pilihTanggal, final String kdMasjid){
        @SuppressLint("StaticFieldLeak")
        class GetSalesmanActivity extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                //loading = ProgressDialog.show(getActivity(), Config.ALERT_LOADING, Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                showTanggalKajian(s);
                //loading.dismiss();
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
                loading = ProgressDialog.show(getActivity(), "", Config.ALERT_PLEASE_WAIT, false, false);
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
}
