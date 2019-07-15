package com.hyundaimobil.jadwalkajian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.hyundaimobil.jadwalkajian._adapter.TaskAdapterNews;
import com.hyundaimobil.jadwalkajian._model.ModalTaskNews;
import com.hyundaimobil.jadwalkajian.config.AppController;
import com.hyundaimobil.jadwalkajian.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsFragment extends Fragment {

    private List<ModalTaskNews> modalTaskList = new ArrayList<>();
    //RecyclerView recyclerView;
    private TaskAdapterNews mAdapter;
    private String JSON_STRING, versi;
    ImageView offline;
    private TextView txtDate, etOffline, etServerNotFound;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String TAG = MainActivity.class.getSimpleName();
    int no;
    private int offSet = 0;
    Handler handler;
    Runnable runnable;
    ListView list;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        //recyclerView        = rootView.findViewById(R.id.recycler_view);
        offline             = rootView.findViewById(R.id.offline);
        etOffline           = rootView.findViewById(R.id.etOffline);
        etServerNotFound    = rootView.findViewById(R.id.etServerNotFound);
        txtDate             = rootView.findViewById(R.id.tanggal);
        list                = rootView.findViewById(R.id.list_news);
        //modalTaskList.clear();

        mAdapter = new TaskAdapterNews(getActivity(), modalTaskList);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        //list.setLayoutManager(mLayoutManager);
        //list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(mAdapter);

        versi = Config.VALUE_VERSI;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), NewsDetail.class);
                intent.putExtra(Config.DISP_KD_NEWS, modalTaskList.get(position).getId());
                startActivity(intent);
                //Toast.makeText(getActivity(), modalTaskList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });

        //untuk refresh
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Config.CEK_KONEKSI(Objects.requireNonNull(getActivity()))) {
                    mAdapter.notifyDataSetChanged();
                    modalTaskList.clear();
                    //recyclerView.setVisibility(View.VISIBLE);
                    list.setVisibility(View.VISIBLE);
                    offline.setVisibility(View.GONE);
                    etOffline.setVisibility(View.GONE);
                    etServerNotFound.setVisibility(View.GONE);
                    callNews(0);
                    mSwipeRefreshLayout.setRefreshing(true);
                }else{
                    mAdapter.notifyDataSetChanged();
                    modalTaskList.clear();
                    //recyclerView.setVisibility(View.GONE);
                    list.setVisibility(View.GONE);
                    offline.setVisibility(View.VISIBLE);
                    etOffline.setVisibility(View.VISIBLE);
                    etServerNotFound.setVisibility(View.GONE);
                    //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_NO_CONN, Toast.LENGTH_SHORT).show();
                    Snackbar.make(container, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        /*
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(Config.CEK_KONEKSI(Objects.requireNonNull(getContext()))) {
                    ModalTaskNews modalTask = modalTaskList.get(position);
                    if (!modalTask.getId().equals("0")) {
                        Intent intent = new Intent(getActivity(), DaftarMasjid.class);
                        intent.putExtra(Config.DISP_KD_WILAYAH, modalTask.getId());
                        //intent.putExtra(Config.DISP_WILAYAH, modalTask.getTitle());
                        startActivity(intent);
                    } else {
                        //Toast.makeText(getActivity(), modalTask.getTitle(), Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, modalTask.getJudul(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }else{
                    //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));
        */

        if(Config.CEK_KONEKSI(Objects.requireNonNull(getContext()))) {
            mAdapter.notifyDataSetChanged();
            //recyclerView.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
            modalTaskList.clear();
            offline.setVisibility(View.GONE);
            etOffline.setVisibility(View.GONE);
            etServerNotFound.setVisibility(View.GONE);
            callNews(0);
        } else {
            mAdapter.notifyDataSetChanged();
            //recyclerView.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
            modalTaskList.clear();
            offline.setVisibility(View.VISIBLE);
            etOffline.setVisibility(View.VISIBLE);
            etServerNotFound.setVisibility(View.GONE);
            //Toast.makeText(getActivity(), Config.ALERT_MESSAGE_CONN_ERROR, Toast.LENGTH_SHORT).show();
            Snackbar.make(container, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount && this.currentScrollState == SCROLL_STATE_IDLE) {
                    final ProgressDialog loading2 = ProgressDialog.show(getContext(), "", Config.ALERT_PLEASE_WAIT, false, false);
                    handler = new Handler();

                    runnable = new Runnable() {
                        public void run() {
                            if (Config.CEK_KONEKSI(getContext())) {
                                callNews(offSet);
                                loading2.dismiss();
                            } else {
                                loading2.dismiss();
                                mSwipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getContext(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                            }

                        }
                    };

                    handler.postDelayed(runnable, 1000);
                }
            }

        });

        return rootView;
    }

    private void callNews(int page) {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "", Config.ALERT_PLEASE_WAIT, false, false);
        //swipe.setRefreshing(true);

        // Creating volley request obj
        //JsonArrayRequest arrReq = new JsonArrayRequest(Config.URL_ALL_NEWS + "/" + page,
        StringRequest arrReq = new StringRequest(Request.Method.POST, Config.URL_GET_ALL_NEWS + "?page=" + page,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);

                        if (response.length() > 0) {
                            // Parsing json
                            //for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                                JSONArray jsonArray = new JSONArray(getObject);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    ModalTaskNews news = new ModalTaskNews();

                                    no = obj.getInt(Config.DISP_NOMOR);
                                    news.setId(obj.getString(Config.DISP_KD_NEWS));
                                    news.setJudul(obj.getString(Config.DISP_JUDUL_NEWS));

                                    if (!Objects.equals(obj.getString(Config.DISP_GAMBAR_NEWS), "")) {
                                        news.setGambar(obj.getString(Config.DISP_GAMBAR_NEWS));
                                    }

                                    news.setDatetime(obj.getString(Config.DISP_TGL_NEWS));
                                    news.setIsi(obj.getString(Config.DISP_ISI_NEWS));

                                    // adding news to news array
                                    modalTaskList.add(news);

                                    if (no > offSet)
                                        offSet = no;

                                    Log.e(TAG, "offSet " + offSet + " - " + no);
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            mAdapter.notifyDataSetChanged();
                            //}
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        loading.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), Config.ALERT_TITLE_CONN_ERROR, Toast.LENGTH_SHORT).show();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
                loading.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }


}
