package com.hyundaimobil.jadwalkajian;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hyundaimobil.jadwalkajian._adapter.CategoriAdapter;
import com.hyundaimobil.jadwalkajian._adapter.RecyclerTouchListener;
import com.hyundaimobil.jadwalkajian._model.ModalCategori;

import java.util.ArrayList;
import java.util.List;

public class AddFragment extends Fragment {

    private List<ModalCategori> modalTaskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CategoriAdapter mAdapter;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mAdapter = new CategoriAdapter(modalTaskList);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModalCategori modalTask = modalTaskList.get(position);
                Toast.makeText(getContext(), modalTask.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();

        return rootView;
    }

    private void prepareMovieData() {

        ModalCategori modalTask = new ModalCategori("1", "Personal");
        modalTaskList.add(modalTask);

        modalTask = new ModalCategori("2", "Work");
        modalTaskList.add(modalTask);

        modalTask = new ModalCategori("3", "Appointment");
        modalTaskList.add(modalTask);

    }

}
