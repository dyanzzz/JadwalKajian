package com.hyundaimobil.jadwalkajian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyundaimobil.jadwalkajian.config.Config;
import com.hyundaimobil.jadwalkajian.config.JSONParser;
import com.hyundaimobil.jadwalkajian.config.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginFragment extends Fragment {
    SessionManager session;
    Button login;
    EditText password;
    String success, message, linkUpdate, pass, versi;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        session = new SessionManager(getActivity());
        String statusLogin = String.valueOf(session.isLoggedIn());
        password        = (EditText) rootView.findViewById(R.id.etPassword);
        login           = (Button) rootView.findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.CEK_KONEKSI(getActivity())) {
                    pass        = password.getText().toString().trim();
                    versi       = Config.VALUE_VERSI;

                    if (pass.length() > 0) {
                        new PostAsync().execute(pass, versi);
                    } else {
                        //Toast.makeText(getApplicationContext(), Config.ALERT_PASSWORD, Toast.LENGTH_LONG).show();
                        //password.setError(Config.ALERT_PASSWORD);
                        Snackbar.make(v, Config.ALERT_PASSWORD, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(v, Config.ALERT_MESSAGE_NO_CONN, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        return rootView;
    }




    //method POST
    private class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle(Config.ALERT_LOADING);
            pDialog.setMessage(Config.ALERT_PLEASE_WAIT);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_PASSWORD, args[0]);
                params.put(Config.KEY_VERSI, args[1]);

                Log.d("request", "starting");
                JSONObject json = jsonParser.makeHttpRequest(Config.LOGIN_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());
                    return json;
                }

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            success = "0";
            message = "";
            //linkUpdate = "";

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (json != null) {
                try {
                    success = json.getString("success");
                    message = json.getString("message");
                    //linkUpdate = json.getString("linkUpdate");
                    JSONArray hasil = json.getJSONArray("login");
                    if (success.equals("1")){
                        //Toast.makeText(getActivity(), json.toString(), Toast.LENGTH_LONG).show();

                        for (int i = 0; i < hasil.length(); i++){
                            JSONObject c = hasil.getJSONObject(i);
                            String kdUsers  = c.getString("kdUsers").trim();
                            String nama     = c.getString("nama").trim();
                            session.createLoginSession(kdUsers, nama);
                            Log.e("ok", " ambil data");
                        }

                    //} else if(success.equals("3")){
                        //Update Apps
                        //Log.e("error", "tidak bisa ambil data 1");
                        //buttonUpdate.setVisibility(View.VISIBLE);
                    } else {
                        //untuk display json from server
                        //Toast.makeText(Login.this, json.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error", "tidak bisa ambil data 1");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (success.equals("1")) {
                Log.d("Success!", message);
                Intent intent = new Intent(getActivity(), InputDaftarMasjid.class);
                startActivity(intent);
                //Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                //finish();
            }else{
                Log.d("Failure", message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
