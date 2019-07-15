package com.hyundaimobil.jadwalkajian;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.hyundaimobil.jadwalkajian._adapter.CustomGrid;
import com.hyundaimobil.jadwalkajian.config.SessionManager;

import java.util.HashMap;

public class CategoryFragment extends Fragment {

    private GridView grid;

    public CategoryFragment() {
        // Required empty public constructor
    }

    private String[] name = {
            "Wilayah",
            "Masjid",
            "Kajian"
    };

    private int[] imageId = {
            R.drawable.ic_add_black_24dp,
            R.drawable.ic_categories_black_24dp,
            R.drawable.ic_home_black_24dp
    };


    SessionManager session;
    String kdUsers, nama;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        session = new SessionManager(getActivity());
        String statusLogin = String.valueOf(session.isLoggedIn());
        if(statusLogin.equals("false")){
            getActivity().onBackPressed();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        kdUsers = user.get(SessionManager.KEY_USERCODE);
        nama    = user.get(SessionManager.KEY_NAMAUSER);

        CustomGrid adapter = new CustomGrid(getActivity(), name, imageId);
        grid=(GridView) rootView.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (name[position]) {
                    case "Wilayah":
                        Intent intent = new Intent(getActivity(), InputDaftarWilayah.class);
                        startActivity(intent);
                        break;
                    case "Masjid":
                        Intent intent2 = new Intent(getActivity(), InputDaftarMasjid.class);
                        startActivity(intent2);
                        break;
                    case "Kajian":
                        Intent intent3 = new Intent(getActivity(), InputDaftarKajian.class);
                        startActivity(intent3);
                        break;
                }
                //Toast.makeText(getActivity(),"nama "+name[position], Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.navcategorytoolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure want to Logout?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Back to Login.class
                getActivity().onBackPressed();
            }

        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), "Welcome "+nama, Toast.LENGTH_SHORT).show();
            }
        });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
