package com.example.ApkPKL.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.ApkPKL.R;
import com.example.ApkPKL.login;

import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;
    private TextView isi;
    private EditText namaPerawat;
    private Button simpan, keluar;
    private int idFromIntent;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class );
        root = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundel = getActivity().getIntent().getExtras();
        idFromIntent = bundel.getInt("ID");
        namaPerawat = root.findViewById(R.id.namaPerawat);
        simpan = root.findViewById(R.id.button_simpan);
        keluar = root.findViewById(R.id.button_keluar);
        Spinner spin = root.findViewById(R.id.spinner1);
        isi = root.findViewById(R.id.paramedis);
        preferences = getActivity().getSharedPreferences("LastSelect", Context.MODE_PRIVATE);
        editor = preferences.edit();

        final int LastClick = preferences.getInt("LastSetting",0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_layout, getResources().getStringArray(R.array.list));
        adapter.setDropDownViewResource(R.layout.custom_dropdown_menu);
        spin.setAdapter(adapter);
        spin.setSelection(LastClick);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 3 ){
                    namaPerawat.setVisibility(View.VISIBLE);
                    isi.setVisibility(View.VISIBLE);
                }else{
                    namaPerawat.setVisibility(View.INVISIBLE);
                    isi.setVisibility(View.INVISIBLE);
                }
                simpan.setOnClickListener(v -> {
                    if (position == 1) {
                        editor.putInt("LastSetting",1).commit();
                        updateStatus(idFromIntent, "Maintenance");
                        Toast.makeText(getContext(), "Status : Maintenance" , Toast.LENGTH_SHORT).show();
                    } else if (position == 2) {
                        editor.putInt("LastSetting",2).commit();
                        updateStatus(idFromIntent, "Tidak Ada Paramedis");
                        Toast.makeText(getContext(), "Status : Tidak Ada Paramedis", Toast.LENGTH_SHORT).show();
                    } else {
                        editor.putInt("LastSetting",3).commit();
                        String nama_perawat = namaPerawat.getText().toString();
                        if (nama_perawat.isEmpty()) {
                            namaPerawat.setError("Masukan Nama Perawat");
                        } else {
                            updatePerawat(idFromIntent,nama_perawat);
                            updateStatus(idFromIntent, "Ready");
                            Toast.makeText(getContext(),"Silahkan Menuju Ke Tab Maps",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        keluar.setOnClickListener(v -> {
            updateStatus(idFromIntent,"Off");
            editor.putInt("LastSetting",0).commit();
            goToLogin();
        });
        return root;
    }

    private void updateStatus(int a, String d) {
        AndroidNetworking.get("http://panggilambulan.my.id/api/updateStatusAmbulan?id={id}&status={status}&token={token}")
                .addQueryParameter("id", String.valueOf(a))
                .addQueryParameter("status", d)
                .addQueryParameter("token", "mant4pgans")
                .setTag(".update")
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

    private void updatePerawat(int a, String d) {
        AndroidNetworking.get("http://panggilambulan.my.id/api/updatePerawat?id={id}&nama_perawat={nama_perawat}&token={token}")
                .addQueryParameter("id", String.valueOf(a))
                .addQueryParameter("nama_perawat", d)
                .addQueryParameter("token", "mant4pgans")
                .setTag(".updatePerawat")
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

    private void goToLogin(){
        Intent i = new Intent(getActivity(), login.class);
        getActivity().finish();
        startActivity(i);
    }
}
