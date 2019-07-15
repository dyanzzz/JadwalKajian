package com.hyundaimobil.jadwalkajian._adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyundaimobil.jadwalkajian.R;

/**
 * Created by User HMI on 9/16/2017.
 */

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final String[] name;
    private final int[] backGroundImage;

    private View grid;

    public CustomGrid(Context c, String[] name, int[] backGroundImage ) {
        mContext = c;
        this.backGroundImage = backGroundImage;
        this.name = name;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.costum_grid, null);
            TextView textView = (TextView) grid.findViewById(R.id.gridText);
            ImageView imageView = (ImageView) grid.findViewById(R.id.gridIcon);

            textView.setText(name[position]);
            imageView.setImageResource(backGroundImage[position]);

            if (name[position].equals("Gone")){
                textView.setVisibility(View.GONE);
            }


        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}