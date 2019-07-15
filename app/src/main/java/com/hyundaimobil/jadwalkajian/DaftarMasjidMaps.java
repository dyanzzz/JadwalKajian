package com.hyundaimobil.jadwalkajian;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hyundaimobil.jadwalkajian.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DaftarMasjidMaps extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    MapFragment mapFragment;
    GoogleMap gMap;
    GoogleApiClient mGoogleApiClient;
    MarkerOptions markerOptions = new MarkerOptions();
    CameraPosition cameraPosition;
    LatLng center, latLng;
    String title, description, kd_wilayah, wilayah, kd_masjid;
    //static final int tampil_error = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(R.mipmap.mainlogo3);
        //actionBar.setHomeAsUpIndicator(R.mipmap.mainlogo4);


        Intent intent   = getIntent();
        kd_masjid       = intent.getStringExtra(Config.DISP_KD_MASJID);
        kd_wilayah      = intent.getStringExtra(Config.DISP_KD_WILAYAH);
        wilayah         = intent.getStringExtra(Config.DISP_WILAYAH);

        //mAdapter = new TaskAdapterMasjid(modalTaskList);
        //ModalTaskMasjid modalTask = modalTaskList.get(position);

        if (googleServicesAvailable()) {
            Toast.makeText(this, "Lihat Lokasi Masjid!!!", Toast.LENGTH_LONG).show();

            setContentView(R.layout.activity_daftar_masjid_maps);
            initMap();
        } else {
            Toast.makeText(this, "Not Connect!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void initMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (gMap != null) {
            //center = new LatLng(-6.894796, 110.638413);
            //cameraPosition = new CameraPosition.Builder().target(center).zoom(10).build();
            //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            gMap.setMyLocationEnabled(true);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();

            getMarkers();
        }
    }

    private void addMarker(LatLng latlng, final String title, final String deskripsi) {
        markerOptions.position(latlng);
        markerOptions.title(title);
        markerOptions.snippet(deskripsi);

        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker)));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        gMap.addMarker(markerOptions);
        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
/*
                if(Config.CEK_KONEKSI(DaftarMasjidMaps.this)) {
                    Intent intent = new Intent(DaftarMasjidMaps.this, DaftarKajian.class);
                    intent.putExtra(Config.DISP_KD_MASJID, marker.getTitle());
                    intent.putExtra(Config.DISP_KD_WILAYAH, kd_wilayah);
                    intent.putExtra(Config.DISP_WILAYAH, wilayah);
                    startActivity(intent);
                } else {
                    showDialog(tampil_error);
                }
*/
            }
        });
    }

    // Fungsi get JSON marker
    private void getMarkers() {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_GET_ALL_MASJID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());
                //ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString(Config.TAG_JSON_ARRAY);
                    JSONArray jsonArray = new JSONArray(getObject);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        kd_masjid   = jsonObject.getString(Config.DISP_KD_MASJID);
                        title       = jsonObject.getString(Config.DISP_NAMA_MASJID);
                        description = jsonObject.getString(Config.DISP_ALAMAT_MASJID);
                        latLng      = new LatLng(Double.parseDouble(jsonObject.getString(Config.DISP_LAT)),
                                Double.parseDouble(jsonObject.getString(Config.DISP_LNG)));

                        //HashMap<String, String> viewBooking = new HashMap<>();
                        //viewBooking.put(Config.DISP_KD_MASJID, kd_masjid);
                        //list.add(viewBooking);

                        //Toast.makeText(getApplicationContext(), viewBooking.toString(), Toast.LENGTH_SHORT).show();

                        // Menambah data marker untuk di tampilkan ke google map
                        addMarker(latLng, title, description);
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), Config.ALERT_MESSAGE_SRV_NOT_FOUND, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                //Toast.makeText(DaftarMasjidMaps.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), Config.ALERT_MESSAGE_SRV_NOT_FOUND, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Config.DISP_KD_WILAYAH, kd_wilayah);
                params.put(Config.DISP_KD_MASJID, kd_masjid);
                return params;
            }
        };

        MyRequestQueue.add(strReq);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pilihmodelmap, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.mapTypeNormal:
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    LocationRequest mLocationRequest;
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setInterval(1000);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            gMap.animateCamera(update);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
