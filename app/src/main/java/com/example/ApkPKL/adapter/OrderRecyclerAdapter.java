package com.example.ApkPKL.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ApkPKL.R;
import com.example.ApkPKL.pasien;

import java.util.List;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder> {

    private List<pasien> listOrder;

    public OrderRecyclerAdapter(List<pasien> listOrder) {
        this.listOrder = listOrder;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_order_recycler, parent, false);

        return new OrderViewHolder(itemView);
        }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.textViewID.setText(Integer.toString(listOrder.get(position).getId()));
        holder.textViewNama.setText(listOrder.get(position).getNama());
        holder.textViewKontak.setText(listOrder.get(position).getKontak());
        holder.textViewLokasiP.setText(listOrder.get(position).getLokasi_Pasien());
        holder.textViewLokasiT.setText(listOrder.get(position).getLokasi_Tujuan());
        holder.textViewDetail.setText(listOrder.get(position).getDetail());
        holder.textViewWaktuP.setText(listOrder.get(position).getWaktu_penjemputan());
        holder.textViewWaktuS.setText(listOrder.get(position).getWaktu_sampai());
    }

    @Override
    public int getItemCount() {
        Log.v(OrderRecyclerAdapter.class.getSimpleName(),""+listOrder.size());
        return listOrder.size();
    }


    /**
     * ViewHolder class
     */
    public class OrderViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewID;
        public AppCompatTextView textViewNama;
        public AppCompatTextView textViewKontak;
        public AppCompatTextView textViewLokasiP;
        public AppCompatTextView textViewLokasiT;
        public AppCompatTextView textViewDetail;
        public AppCompatTextView textViewWaktuP;
        public AppCompatTextView textViewWaktuS;

        public OrderViewHolder(View view) {
            super(view);
            textViewID = (AppCompatTextView) view.findViewById(R.id.textViewID);
            textViewNama = (AppCompatTextView) view.findViewById(R.id.textViewPasien);
            textViewKontak = (AppCompatTextView) view.findViewById(R.id.textViewKontak);
            textViewLokasiP = (AppCompatTextView) view.findViewById(R.id.textViewLokasiP);
            textViewLokasiT = (AppCompatTextView) view.findViewById(R.id.textViewLokasiT);
            textViewDetail = (AppCompatTextView) view.findViewById(R.id.textViewDetail);
            textViewWaktuP = (AppCompatTextView) view.findViewById(R.id.textViewWaktuJ);
            textViewWaktuS = (AppCompatTextView) view.findViewById(R.id.textViewWaktuS);
        }
    }
}
