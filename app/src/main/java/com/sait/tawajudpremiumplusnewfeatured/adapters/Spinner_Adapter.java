package com.sait.tawajudpremiumplusnewfeatured.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.sait.tawajudpremiumplusnewfeatured.R;

import java.util.ArrayList;

public class Spinner_Adapter extends BaseAdapter implements android.widget.SpinnerAdapter {
    Context context;
    ArrayList<String> arrayList;
    LayoutInflater inflter;

    public Spinner_Adapter(Context applicationContext, ArrayList<String> arrayList) {
        this.context = applicationContext;
        this.arrayList = arrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {

            return null;
    }

    @Override
    public long getItemId(int i) {

        return 0;
    }




    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item, null);
        TextView names = view.findViewById(R.id.tv_title);
        //SpinnerItemBO spinnerItemBO = arrayList.get(i);
        names.setText(arrayList.get(i));
        return view;

    }
}