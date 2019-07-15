package com.hyundaimobil.jadwalkajian._adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyundaimobil.jadwalkajian.R;
import com.hyundaimobil.jadwalkajian._model.ModalTask;

import java.util.List;

/**
 * Created by User HMI on 9/16/2017.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<ModalTask> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, number, count;

        public MyViewHolder(View view) {
            super(view);
            number = (TextView) view.findViewById(R.id.valueNumber);
            title = (TextView) view.findViewById(R.id.valueText);
            count = (TextView) view.findViewById(R.id.count);
        }
    }

    public TaskAdapter(List<ModalTask> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.costum_recyclerview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModalTask modalTask = moviesList.get(position);
        holder.number.setText(modalTask.getNumber());
        holder.title.setText(modalTask.getTitle());
        holder.count.setText(modalTask.getCount());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}