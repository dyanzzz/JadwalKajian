package com.hyundaimobil.jadwalkajian._adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyundaimobil.jadwalkajian.R;
import com.hyundaimobil.jadwalkajian._model.ModalCategori;

import java.util.List;

/**
 * Created by User HMI on 9/16/2017.
 */

public class CategoriAdapter extends RecyclerView.Adapter<CategoriAdapter.MyViewHolder> {

    private List<ModalCategori> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView valueTitle;
        public CardView linearBox;

        public MyViewHolder(View view)
        {
            super(view);
            valueTitle = (TextView) view.findViewById(R.id.valueTitle);
            linearBox = (CardView) view.findViewById(R.id.linearBox);
        }
    }

    public CategoriAdapter(List<ModalCategori> moviesList)
    {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.costum_recycleritem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ModalCategori modalTask = moviesList.get(position);
        holder.valueTitle.setText(modalTask.getTitle());

        holder.linearBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.valueTitle.getCurrentTextColor() == -1){
                    holder.linearBox.setBackgroundResource(R.color.colorWhite);
                    holder.valueTitle.setTextColor(Color.GRAY);
                } else {
                    holder.linearBox.setBackgroundResource(R.color.colorPrimary);
                    holder.valueTitle.setTextColor(Color.WHITE);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}