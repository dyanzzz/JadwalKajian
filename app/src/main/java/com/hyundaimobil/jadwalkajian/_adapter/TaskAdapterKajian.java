package com.hyundaimobil.jadwalkajian._adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyundaimobil.jadwalkajian.R;
import com.hyundaimobil.jadwalkajian._model.ModalTaskKajian;

import java.util.List;

/**
 * Created by User HMI on 9/17/2017.
 */

public class TaskAdapterKajian extends RecyclerView.Adapter<TaskAdapterKajian.MyViewHolder> {

    private List<ModalTaskKajian> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView number, keterangan;


        public MyViewHolder(View view) {
            super(view);
            number      = (TextView) view.findViewById(R.id.valueNumber);
            //pemateri    = (TextView) view.findViewById(R.id.valuePemateri);
            //tema        = (TextView) view.findViewById(R.id.valueTema);
            //waktu       = (TextView) view.findViewById(R.id.valueWaktu);
            keterangan  = (TextView) view.findViewById(R.id.valueKeterangan);
        }
    }

    public TaskAdapterKajian(List<ModalTaskKajian> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.costum_recyclerviewkajian, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModalTaskKajian modalTaskKajian = moviesList.get(position);
        holder.number.setText(modalTaskKajian.getNumber());
        //holder.pemateri.setText(modalTaskKajian.getPemateri());
        //holder.tema.setText(modalTaskKajian.getTema());
        //holder.waktu.setText(modalTaskKajian.getWaktu());
        holder.keterangan.setText(modalTaskKajian.getKeterangan());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
