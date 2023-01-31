package com.example.babyland;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private int images[];
    private String[] bloodType;
    private LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, int[] images, String[] bloodType) {
        this.context = applicationContext;
        this.images = images;
        this.bloodType = bloodType;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageSex);
        TextView names = (TextView) view.findViewById(R.id.textAmka);
        icon.setImageResource(images[i]);
        names.setText(bloodType[i]);
        return view;
    }

    @Override
    public int getCount() {
        return bloodType.length;
    }

    @Override
    public Object getItem(int i) {
        return bloodType[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
