package com.example.ApkPKL;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {

    private final AppCompatActivity activity = login.this;

    private EditText username;
    private EditText pass;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        username    = findViewById(R.id.username);
        pass        = findViewById(R.id.pass);
        btn_login   = findViewById(R.id.btn_login);

        initObjects();

        btn_login.setOnClickListener(v -> {

            String Username = username.getText().toString();
            String Password = pass.getText().toString();

            AndroidNetworking.get("http://panggilambulan.my.id/api/login?nama={nama}&password={password}&token={token}")
                    .addQueryParameter("nama" , Username)
                    .addQueryParameter("password" , Password)
                    .addQueryParameter("token" , "mant4pgans")
                    .setTag(".login")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean result = response.getBoolean("pesan");
                                if (result) {
                                    goToMainActivity();
                                } else {
                                    Snackbar.make(btn_login, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError error) {
                            System.out.println(error.toString());
                        }
                    });
        });
    }


    public void goToMainActivity(){
        final String Username = username.getText().toString();
        profil(Username);
    }

    private void initObjects() {
        AndroidNetworking.initialize(this);
    }

    private void emptyInputEditText() {
        username.setText(null);
        pass.setText(null);
    }

    private void profil(String a){
        AndroidNetworking.get("http://panggilambulan.my.id/api/profil?nama={nama}&token={token}")
                .addQueryParameter("nama", a)
                .addQueryParameter("token", "mant4pgans")
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Intent i = new Intent(activity,MainActivity.class);
                            String Username = username.getText().toString();
                            Bundle bundle = new Bundle();
                            int id =response.getInt("id");
                            String lokasi = response.getString("lokasi");
                            String tipe = response.getString("jenis_ambulan");
                            bundle.putInt("ID", id);
                            bundle.putString("USERNAME", Username);
                            bundle.putString("LOK", lokasi);
                            bundle.putString("AMBULAN", tipe);
                            i.putExtras(bundle);
                            emptyInputEditText();
                            activity.finish();
                            startActivity(i);
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