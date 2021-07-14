package com.example.ApkPKL.ui.maps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.ApkPKL.App;
import com.example.ApkPKL.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MapsFragment extends Fragment {

    private MapsViewModel mapsViewModel;
    private TextView namaP, kontakP, alamatP, ketP, StatusA;
    private Button call, antar, finish;
    private int idFromIntent, idP;
    private double latitude, longitude, latitudeP, latitudePT, longitudeP, longitudePT;
    private String namaFromIntent, status;
    private static final int LOCATION_REQUEST_CODE = 1;

    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;
    private static final String TAG = "MapsFragment";

    LocationRequest locationRequest;
    LocationRequest locationRequest1;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                Log.d(TAG, "onLocationResult: " + location.toString());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                update1(idFromIntent, latitude, longitude);
            }
        }
    };

    private Handler mHandler = new Handler();

    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            profil();
            mHandler.postDelayed(this, 10000);
        }
    };

    public void startRepeating() {
        System.out.println("thread start");
        mToastRunnable.run();
    }

    public void stopRepeating() {
        mHandler.removeCallbacks(mToastRunnable);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mapsViewModel =
                ViewModelProviders.of(this).get(MapsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_maps, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(15000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest1 = LocationRequest.create();
        locationRequest1.setInterval(3000);
        locationRequest1.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        namaP = root.findViewById(R.id.namaP);
        kontakP = root.findViewById(R.id.kontakP);
        alamatP = root.findViewById(R.id.alamatP);
        ketP = root.findViewById(R.id.ketP);
        call = root.findViewById(R.id.call);
        finish = root.findViewById(R.id.finish);
        antar = root.findViewById(R.id.antar);
        StatusA = root.findViewById(R.id.statusA);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            askLocationPermission();
        }

        Bundle bundel = getActivity().getIntent().getExtras();
        idFromIntent = bundel.getInt("ID");
        namaFromIntent = bundel.getString("USERNAME");

        startRepeating();

        call.setOnClickListener(v -> {
            if(kontakP.getText().toString().isEmpty()){

            } else {
                Dexter.withContext(getContext())
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                makeCall();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                System.out.println("permision Denied");
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                            }
                        }).check();
            }
        });

        antar.setOnClickListener(v -> {
            if(latitudeP == 0||longitudeP == 0){
                openDialog();
            }else{
                status = "Mengantar";
                GoogleDirection.withServerKey("AIzaSyCAe8FKib8kPQMD7yUFJllVEjTWqPdnovw")
                        .from(new LatLng(latitudeP, longitudeP))
                        .to(new LatLng(latitudePT, longitudePT))
                        .transportMode(TransportMode.DRIVING)
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction) {
                                if (direction.isOK()) {
                                    supportMapFragment.getMapAsync(googleMap -> {
                                        googleMap.clear();
                                        getLastLocation();
                                        Route route = direction.getRouteList().get(0);
                                        Leg leg = route.getLegList().get(0);
                                        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                        PolylineOptions polylineOptions = DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.RED);
                                        if (polylineOptions != null) {
                                            googleMap.addPolyline(polylineOptions);
                                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
                                        } else {
                                            System.out.println("antar error");
                                        }
                                    });
                                    antar.setVisibility(View.INVISIBLE);
                                    finish.setVisibility(View.VISIBLE);
                                    startLocationUpdates1();
                                    update1(idFromIntent, latitude, longitude);
                                } else {

                                }
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {

                            }
                        });
            }
        });

        finish.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setMessage("Selesai");
            alertDialogBuilder.setPositiveButton("yes",
                    (arg0, arg1) -> {
                        status = "Pembersihan";
                        getLastLocation1();
                        updateWaktuSampaiPasien();
                        antar.setVisibility(View.VISIBLE);
                        finish.setVisibility(View.INVISIBLE);
                        startLocationUpdates1();
                        update(idFromIntent, latitude, longitude, status);
                        empty();
                        Toast.makeText(App.getContext(),"Silahkan Menuju Ke Tab Home Untuk Mengubah Status",Toast.LENGTH_LONG).show();
                    });
            alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {

            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        return root;
    }

    private void update(int a, double b, double c, String d) {
        AndroidNetworking.get("http://panggilambulan.my.id/api/updateAmbulan?id={id}&ltd={ltd}&lgd={lgd}&status={status}&token={token}")
                .addQueryParameter("id", String.valueOf(a))
                .addQueryParameter("ltd", String.valueOf(b))
                .addQueryParameter("lgd", String.valueOf(c))
                .addQueryParameter("status", d)
                .addQueryParameter("token", "mant4pgans")
                .setTag(".update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (d.equals("Ready")) {
                            Toast.makeText(getContext(), "Status : Ready", Toast.LENGTH_SHORT).show();
                        } else if (d.equals("Menjemput")) {
                            Toast.makeText(getContext(), "Status : Menjemput", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Status : " + response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void update1(int a, double b, double c) {
        AndroidNetworking.get("http://panggilambulan.my.id/api/updateAmbulan1?id={id}&ltd={ltd}&lgd={lgd}&token={token}")
                .addQueryParameter("id", String.valueOf(a))
                .addQueryParameter("ltd", String.valueOf(b))
                .addQueryParameter("lgd", String.valueOf(c))
                .addQueryParameter("token", "mant4pgans")
                .setTag(".update1")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getPasien() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y-M-d H:m:s");
        AndroidNetworking.get("http://panggilambulan.my.id/api/pasien?user_ambulan={user_ambulan}&token={token}")
                .addQueryParameter("user_ambulan", namaFromIntent)
                .addQueryParameter("token", "mant4pgans")
                .setTag(".testData")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String waktu = jsonObject.getString("waktu_sampai");
                                Date date1 = simpleDateFormat.parse(waktu);
                                Date date2 = simpleDateFormat.parse("0000-00-00 00:00:00");
                                if(date1.equals(date2)){
                                    stopRepeating();
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setMessage("Ada Pasien Baru");
                                    alertDialogBuilder.setPositiveButton("yes", (arg0, arg1) -> {
                                        try {
                                            idP = jsonObject.getInt("id");
                                            String nama = jsonObject.getString("nama");
                                            String kontak = jsonObject.getString("kontak");
                                            String lokasi = jsonObject.getString("lokasi");
                                            String lainnya = jsonObject.getString("lainnya");
                                            latitudeP = jsonObject.getDouble("lat_lokasi");
                                            longitudeP = jsonObject.getDouble("lgd_lokasi");
                                            latitudePT = jsonObject.getDouble("lat_tujuan");
                                            longitudePT = jsonObject.getDouble("lgd_tujuan");
                                            jemputPasien();
                                            namaP.setText(nama);
                                            kontakP.setText(kontak);
                                            alamatP.setText(lokasi);
                                            ketP.setText(lainnya);
                                            mark(latitudeP,longitudeP);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {
                                        startRepeating();
                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }else{
                                    System.out.println("pasien = Error");
                                }
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        System.out.println("Error : "+ anError);
                    }
                });
    }

    private void updateWaktuSampaiPasien() {
        AndroidNetworking.get("http://panggilambulan.my.id/api/updatePasien?id={id}&token={token}")
                .addQueryParameter("id", String.valueOf(idP))
                .addQueryParameter("token", "mant4pgans")
                .setTag(".updateWaktu")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(getActivity());

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(locationSettingsResponse -> {
            //Settings of device are satisfied and we can start location updates
            startLocationUpdates();
        });
        locationSettingsResponseTask.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException apiException = (ResolvableApiException) e;
                try {
                    apiException.startResolutionForResult(getActivity(), 1001);
                } catch (IntentSender.SendIntentException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void startLocationUpdates1() {
        if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(locationRequest1, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        client.removeLocationUpdates(locationCallback);
        Task<Location> locationTask = client.getLastLocation();
        locationTask.addOnSuccessListener(location -> supportMapFragment.getMapAsync(googleMap -> {
            googleMap.clear();
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(latitude, longitude);
            markerOptions.position(latLng);
            int height = 110;
            int width = 90;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cek2);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }));
    }

    private void getLastLocation() {
        Task<Location> locationTask = client.getLastLocation();
        locationTask.addOnSuccessListener(location -> supportMapFragment.getMapAsync(googleMap -> {
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            markerOptions.position(latLng);
            int height = 110;
            int width = 90;
            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.cek2);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float)0.5,(float)0.5);
            googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
            //update1(idFromIntent, latitude, longitude);
        }));
        locationTask.addOnFailureListener(e -> Log.e(TAG, "onFailure: " + e.getLocalizedMessage() ));
    }

    private void getLastLocation1() {
        Task<Location> locationTask = client.getLastLocation();
        locationTask.addOnSuccessListener(location -> supportMapFragment.getMapAsync(googleMap -> {
            googleMap.clear();
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            markerOptions.position(latLng);
            int height = 110;
            int width = 90;
            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.cek2);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float)0.5,(float)0.5);
            googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        }));
        locationTask.addOnFailureListener(e -> Log.e(TAG, "onFailure: " + e.getLocalizedMessage() ));
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getLastLocation();
                checkSettingsAndStartLocationUpdates();
            } else {
                //Permission not granted
            }
        }
    }

    public void mark(final double a, final double b){
        supportMapFragment.getMapAsync(googleMap -> {
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(a, b);
            markerOptions.position(latLng);
            googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        });
    }

    public void makeCall(){
        if(status.equals("Menjemput")){
            call.setText("Telfon Pasien");
            String s = "tel:"+ kontakP.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(s));
            startActivity(callIntent);
        } else if(status.equals("Mengatar")){
            call.setText("Telfon Perawat");
            String s = "tel:"+ kontakP.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(s));
            startActivity(callIntent);
        } else{
            call.setText("Telfon");
        }
    }

    public void empty(){
        namaP.setText("");
        kontakP.setText("");
        alamatP.setText("");
        ketP.setText("");
    }

    private void jemputPasien(){
        status = "Menjemput";
        update(idFromIntent, latitude, longitude, status);
        GoogleDirection.withServerKey("AIzaSyCAe8FKib8kPQMD7yUFJllVEjTWqPdnovw")
                .from(new LatLng(latitude, longitude))
                .to(new LatLng(latitudeP, longitudeP))
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        if (direction.isOK()) {
                            supportMapFragment.getMapAsync(googleMap -> {
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);
                                ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.RED);
                                if (polylineOptions != null) {
                                    googleMap.addPolyline(polylineOptions);
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
                                } else {
                                    System.out.println("error");
                                }
                            });
                            finish.setVisibility(View.INVISIBLE);
                            antar.setVisibility(View.VISIBLE);
                            startLocationUpdates1();
                        } else {
                            System.out.println("error");
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        t.toString();
                    }
                });
    }

    private void openDialog(){
        ClickDialog clickDialog = new ClickDialog();
        clickDialog.show(getFragmentManager(),"clickdialog");
    }

    public void profil(){
        AndroidNetworking.get("http://panggilambulan.my.id/api/profil?nama={nama}&token={token}")
                .addQueryParameter("nama", namaFromIntent)
                .addQueryParameter("token", "mant4pgans")
                .setTag(".Profil")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stat = response.getString("status");
                            StatusA.setText(stat);
                            if (stat.equals("Ready")||stat.equals("Minta Persetujuan")) {
                                update1(idFromIntent, latitude, longitude);
                                getPasien();
                                startLocationUpdates();
                            } else{
                                update1(idFromIntent, latitude, longitude);
                                empty();
                                stopLocationUpdates();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
}