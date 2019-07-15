package com.hyundaimobil.jadwalkajian;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyundaimobil.jadwalkajian.config.Config;
import com.hyundaimobil.jadwalkajian.config.SessionManager;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    NavigationView navigation_view;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setIcon(R.mipmap.mainlogo3);
        //actionBar.setHomeAsUpIndicator(R.mipmap.mainlogo4);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.navigationdashboard);
        toolbar.setLogo(R.mipmap.mainlogo3);
        toolbar.setLogoDescription(R.string.app_name);

        session = new SessionManager(MainActivity.this);
        String statusLogin = String.valueOf(session.isLoggedIn());

        drawerLayout    = findViewById(R.id.drawer);
        navigation_view = findViewById(R.id.navigation_view);

        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_header, navigation_view, false);
        navigation_view.addHeaderView(headerView);
        TextView name   = headerView.findViewById(R.id.name);
        name.setText("Welcome User");

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Memeriksa apakah item tersebut dalam keadaan dicek  atau tidak,
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Menutup  drawer item klik
                drawerLayout.closeDrawers();
                //Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
                switch (menuItem.getItemId()){

                    case R.id.menuLogin:
                        Intent login = new Intent(MainActivity.this, Login.class);
                        startActivity(login);
                        return true;


                    default:
                        Toast.makeText(getApplicationContext(),Config.ALERT_TITLE_CONN_ERROR,Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        //session.checkLogin();
        //HashMap<String, String> user = session.getUserDetails();
        //nama    = user.get(SessionManager.KEY_NAMAUSER);
        //kdUsers = user.get(SessionManager.KEY_USERCODE);

        //Toast.makeText(MainActivity.this, statusLogin, Toast.LENGTH_LONG).show();

        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.actionbar);

        HomeFragmentDaftarMasjid homeFragment = new HomeFragmentDaftarMasjid();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, homeFragment);
        fragmentTransaction.commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeFragmentDaftarMasjid homeFragment = new HomeFragmentDaftarMasjid();
                    FragmentTransaction fragmentHomeTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentHomeTransaction.replace(R.id.content, homeFragment);
                    fragmentHomeTransaction.commit();
                    return true;

                case R.id.navigation_news:
                    NewsFragment newsFragment = new NewsFragment();
                    FragmentTransaction fragmentAddTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentAddTransaction.replace(R.id.content, newsFragment);
                    fragmentAddTransaction.commit();
                    return true;

            }
            return false;
        }

    };

    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        // TODO Add your menu entries here
        getMenuInflater().inflate(R.menu.navigationdashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation_dashboard) {
            drawerLayout.openDrawer(Gravity.END);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
