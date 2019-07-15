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
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyundaimobil.jadwalkajian.config.Config;
import com.hyundaimobil.jadwalkajian.config.RequestHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewsDetail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    String id_news;
    TextView judul, tgl, isi;
    ImageView thumb_image;
    SwipeRefreshLayout swipe;

    private static final String TAG = NewsDetail.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent   = getIntent();
        id_news         = intent.getStringExtra(Config.DISP_KD_NEWS);

        thumb_image = findViewById(R.id.gambar_news);
        judul       = findViewById(R.id.judul_news);
        tgl         = findViewById(R.id.tgl_news);
        isi         = findViewById(R.id.isi_news);

        swipe       = findViewById(R.id.swipe_refresh_layout);
        swipe.setColorSchemeResources(R.color.colorAccent);
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                if (Config.CEK_KONEKSI(NewsDetail.this)) {
                    callDetailNews(id_news);
                    swipe.setRefreshing(false);
                } else {
                    swipe.setRefreshing(false);
                    Snackbar.make(swipe, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //Toast.makeText(PromotionsDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        if (Config.CEK_KONEKSI(NewsDetail.this)) {
            callDetailNews(id_news);
            swipe.setRefreshing(false);
        } else {
            swipe.setRefreshing(false);
            //showDialog(Config.TAMPIL_ERROR);
            Snackbar.make(swipe, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            //Toast.makeText(PromotionsDetail.this, Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_LONG).show();
        }
    }


    private void callDetailNews(final String id){
        @SuppressLint("StaticFieldLeak")
        class GetViewRun extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(NewsDetail.this, "", Config.ALERT_PLEASE_WAIT, false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                showNewsSelected(s);
                loading.dismiss();
            }

            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.DISP_KD_NEWS, id);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(Config.URL_GET_ALL_NEWS, params);
            }
        }
        GetViewRun jalankan = new GetViewRun();
        jalankan.execute();
    }

    private void showNewsSelected(String json){
        try{
            JSONObject jsonObject       = new JSONObject(json);
            JSONArray result            = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c                = result.getJSONObject(0);

            String Judul    = c.getString(Config.DISP_JUDUL_NEWS);
            String Gambar   = c.getString(Config.DISP_GAMBAR_NEWS);
            String Tgl      = c.getString(Config.DISP_TGL_NEWS);
            String Isi      = c.getString(Config.DISP_ISI_NEWS);

            Picasso.with(this).load(Gambar).into(thumb_image);
            judul.setText(Judul);
            tgl.setText(Tgl);
            isi.setText(Html.fromHtml(Isi));


        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

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
