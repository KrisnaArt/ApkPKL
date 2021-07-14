package com.example.ApkPKL.ui.profile;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.ApkPKL.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private TextView nama, lok, stat, no, ambulan;
    private ImageView profile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        nama = root.findViewById(R.id.username_pro);
        lok = root.findViewById(R.id.lokasi_pro);
        no = root.findViewById(R.id.nomor_Telp);
        stat = root.findViewById(R.id.statusPro);
        ambulan = root.findViewById(R.id.ambulanPro);
        profile =  root.findViewById(R.id.imageView);

        Bundle bundle = getActivity().getIntent().getExtras();
        String emailFromIntent = bundle.getString("USERNAME");

        profil(emailFromIntent);

        return root;
    }

    public void profil(String a){
        AndroidNetworking.get("http://panggilambulan.my.id/api/profil?nama={nama}&token={token}")
                .addQueryParameter("nama", a)
                .addQueryParameter("token", "mant4pgans")
                .setTag(".Profil")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            profile.setImageResource(R.drawable.male);
                            nama.setText(a);
                            no.setText(response.getString("noTelpon"));
                            lok.setText(response.getString("lokasi"));
                            stat.setText(response.getString("status"));
                            ambulan.setText(response.getString("jenis_ambulan"));
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