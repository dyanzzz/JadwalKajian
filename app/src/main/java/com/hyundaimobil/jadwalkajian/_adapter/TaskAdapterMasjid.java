package com.hyundaimobil.jadwalkajian._adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyundaimobil.jadwalkajian.R;
import com.hyundaimobil.jadwalkajian._model.ModalTaskMasjid;

import java.util.List;

/**
 * Created by User HMI on 9/17/2017.
 */

public class TaskAdapterMasjid extends RecyclerView.Adapter<TaskAdapterMasjid.MyViewHolder> {

    private List<ModalTaskMasjid> moviesList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, number, address, informasi;


        MyViewHolder(View view) {
            super(view);
            number      = view.findViewById(R.id.valueNumber);
            title       = view.findViewById(R.id.valueText);
            address     = view.findViewById(R.id.valueAddress);
            informasi   = view.findViewById(R.id.valueInformasi);
            //count       = view.findViewById(R.id.valueCount);
        }
    }

    public TaskAdapterMasjid(List<ModalTaskMasjid> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.costum_recyclerviewmasjid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModalTaskMasjid modalTaskMasjid = moviesList.get(position);
        holder.number.setText(modalTaskMasjid.getNumber());
        holder.title.setText(modalTaskMasjid.getTitle());
        holder.address.setText(modalTaskMasjid.getAddress());
        holder.informasi.setText(modalTaskMasjid.getInformasi());
        //holder.count.setText(modalTaskMasjid.getCount());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
