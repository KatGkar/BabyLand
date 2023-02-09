package com.example.babyland;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterChildSpinner extends BaseAdapter {
    private Context context;
    private int images[];
    private ArrayList<Baby> listKids;
    private LayoutInflater inflater;

        public AdapterChildSpinner(Context applicationContext, int[] images, ArrayList<Baby>listKids) {
        this.context = applicationContext;
        this.images = images;
        this.listKids = listKids; 
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageSex);
        TextView names = (TextView) view.findViewById(R.id.textAmka);
        if (listKids.get(i).getSex().equals("BOY")) {
            icon.setImageResource(images[0]);
        } else {
            icon.setImageResource(images[1]);
        }
        names.setText(listKids.get(i).getAmka());
        return view;
    }

    @Override
    public int getCount() {
        return listKids.size();
    }

    @Override
    public Object getItem(int i) {
        return listKids.get(i).getAmka();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
