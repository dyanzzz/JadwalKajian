package com.hyundaimobil.jadwalkajian;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.hyundaimobil.jadwalkajian.config.SessionManager;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    SessionManager session;
    String usercode, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(R.mipmap.mainlogo3);
        //actionBar.setHomeAsUpIndicator(R.mipmap.mainlogo4);

        session = new SessionManager(getApplicationContext());
        String statusLogin = String.valueOf(session.isLoggedIn());
        if(statusLogin.equals("false")){
            onBackPressed();
            Intent intent = new Intent(Dashboard.this, MainActivity.class);
            startActivity(intent);
        }
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        usercode        = user.get(SessionManager.KEY_USERCODE);
        nama            = user.get(SessionManager.KEY_NAMAUSER);

        Toast.makeText(Dashboard.this, "Welcome "+nama, Toast.LENGTH_LONG).show();

        CategoryFragment categoryFragment = new CategoryFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, categoryFragment);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    CategoryFragment categoryFragment = new CategoryFragment();
                    FragmentTransaction fragmentAddTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentAddTransaction.replace(R.id.content, categoryFragment);
                    fragmentAddTransaction.commit();
                    return true;
            }
            return false;
        }

    };
}
