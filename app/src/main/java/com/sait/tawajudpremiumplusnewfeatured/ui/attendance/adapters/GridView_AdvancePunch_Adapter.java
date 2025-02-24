package com.sait.tawajudpremiumplusnewfeatured.ui.attendance.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.sait.tawajudpremiumplusnewfeatured.R;

import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.models.ReasonsData;
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences;

import java.util.List;
import java.util.Objects;

public class GridView_AdvancePunch_Adapter extends BaseAdapter {
    Context context;
    List<ReasonsData> data;
    LayoutInflater inflter;

    private AdvancePunchItemClickListener advancePunchItemClickListener;

    public GridView_AdvancePunch_Adapter(Context applicationContext, List<ReasonsData> data, AdvancePunchItemClickListener listener) {
        this.context = applicationContext;
        this.data = data;
        this.advancePunchItemClickListener = listener;
        inflter = (LayoutInflater.from(applicationContext));
    }


        @Override
        public int getCount() {
            return data.size();
        }

    public interface AdvancePunchItemClickListener {
        void onAdvancePunchItemClick(int position);

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
        view = inflter.inflate(R.layout.advance_punch_gridview, null); // inflate the layout
        TextView txt_advance_punch = (TextView) view.findViewById(R.id.txt_advance_punch); // get the reference of ImageView
        ImageView img_adavancepunch = (ImageView) view.findViewById(R.id.img_adavancepunch);
        LinearLayout rlItemLayout = (LinearLayout) view.findViewById(R.id.ll_data);
        // Set text based on language preference
        if (Objects.equals(UserShardPrefrences.getLanguage(context), "0")) {
            txt_advance_punch.setText(data.get(i).getReasonName()); // For language english
        } else {
            txt_advance_punch.setText(data.get(i).getReasonArabicName()); // For language arabic
        }

        // Set data
        img_adavancepunch.setBackgroundResource(R.drawable.sign_in_icon);

        // Set click listener using the interface
        rlItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advancePunchItemClickListener.onAdvancePunchItemClick(data.get(i).getReasonCode());
            }
        });

        if(data.get(i).getColorCode().equals("I")){
            img_adavancepunch.setBackgroundResource(R.drawable.sign_in_icon);
        }
        if(data.get(i).getColorCode().equals("O")){
            img_adavancepunch.setBackgroundResource(R.drawable.sign_out_icon);
        }
        if (data.get(i).getColorCode().equals("OO")) {
            img_adavancepunch.setBackgroundResource(R.drawable.off_out);
        }

        if (data.get(i).getColorCode().equals("PO")) {
            img_adavancepunch.setBackgroundResource(R.drawable.personal_out);
        }
        if (data.get(i).getColorCode().equals("LB")) {
            img_adavancepunch.setBackgroundResource(R.drawable.lunch_break);
        }
        if (data.get(i).getColorCode().equals("BO")) {
            img_adavancepunch.setBackgroundResource(R.drawable.break_out);
        }
        if (data.get(i).getColorCode().equals("OI")) {
            img_adavancepunch.setBackgroundResource(R.drawable.official_in);
        }
        if (data.get(i).getColorCode().equals("SO")) {
            img_adavancepunch.setBackgroundResource(R.drawable.sick_out);
        }  if (data.get(i).getColorCode().equals("LO")) {
            img_adavancepunch.setBackgroundResource(R.drawable.lunch_out);
        }
        return view;
    }

}
