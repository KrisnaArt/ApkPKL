package com.example.ApkPKL.ui.history;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.ApkPKL.R;
import com.example.ApkPKL.adapter.OrderRecyclerAdapter;
import com.example.ApkPKL.pasien;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerViewOrder;
    private List<pasien> listOrder;
    private OrderRecyclerAdapter orderRecyclerAdapter;
    private HistoryViewModel historyViewModel;
    private View root;
    private String emailFromIntent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class );
        root = inflater.inflate(R.layout.fragment_history, container, false);

        initViews();
        initObjects();

        return root;
    }

    private void initViews() {
        recyclerViewOrder = root.findViewById(R.id.recyclerViewOrder);
    }

    private void initObjects() {
        listOrder = new ArrayList<>();
        orderRecyclerAdapter = new OrderRecyclerAdapter(listOrder);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewOrder.setLayoutManager(mLayoutManager);
        recyclerViewOrder.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOrder.setHasFixedSize(true);
        recyclerViewOrder.setAdapter(orderRecyclerAdapter);

        Bundle bundle = getActivity().getIntent().getExtras();
        emailFromIntent = bundle.getString("USERNAME");

        history();
    }

    private void history(){
        AndroidNetworking.get("http://panggilambulan.my.id/api/pasien?user_ambulan={user_ambulan}&token={token}")
                .addQueryParameter("user_ambulan", emailFromIntent)
                .addQueryParameter("token", "mant4pgans")
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                pasien item = new pasien(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("nama"),
                                        jsonObject.getString("kontak"),
                                        jsonObject.getString("lokasi"),
                                        jsonObject.getString("tujuan"),
                                        jsonObject.getString("lainnya"),
                                        jsonObject.getString("waktu_jemput"),
                                        jsonObject.getString("waktu_sampai")
                                );
                                listOrder.add(item);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        orderRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                        System.out.println("list = error");
                    }
                });
    }
}