package com.gmail.andrewjoelbecker.sc2vb.starcraft2voicebuilds.sc2vb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Class: CustomAdapter.java
 * Function: This class is a custom Adapter that extends the base ArrayAdapter, it supports two textviews
 * and is linked to the displayItem class, it allows build items to be displayed in a neat format
 */

public class CustomAdapter extends ArrayAdapter<DisplayItem> {
    private ArrayList<DisplayItem> objects;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<DisplayItem> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row, null);
        }


        DisplayItem i = objects.get(position);

        if (i != null) {
            TextView nameView = (TextView) v.findViewById(R.id.itemName);
            TextView timeView = (TextView) v.findViewById(R.id.time);

            if (nameView != null){
                nameView.setText(i.getItem());
            }
            if (timeView != null){
                timeView.setText(i.getTime());
            }

        }

        return v;
    }
}