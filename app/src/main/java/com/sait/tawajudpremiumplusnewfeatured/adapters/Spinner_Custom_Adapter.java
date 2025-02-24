package com.sait.tawajudpremiumplusnewfeatured.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sait.tawajudpremiumplusnewfeatured.R;

import java.util.ArrayList;

import android.widget.Filter;
import android.widget.Filterable;

public class Spinner_Custom_Adapter extends BaseAdapter implements android.widget.SpinnerAdapter, Filterable {
    private final ArrayList<String> originalList;
    private ArrayList<String> filteredList;
    private final LayoutInflater inflater;
    private final ItemFilter itemFilter = new ItemFilter();

    private final OnFilterCompleteListener filterCompleteListener;

    public interface OnFilterCompleteListener {
        void onFilterComplete(ArrayList<String> filteredList);
    }


    public Spinner_Custom_Adapter(Context context, ArrayList<String> arrayList,OnFilterCompleteListener listener) {
        this.originalList = arrayList;
        this.filteredList = arrayList;
        this.filterCompleteListener = listener;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_item, viewGroup, false);
        }
        TextView names = view.findViewById(R.id.tv_title);
        names.setText(filteredList.get(i));
        return view;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> filteredItems = new ArrayList<>();
                for (String item : originalList) {
                    if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredItems.add(item);
                    }
                }
                results.count = filteredItems.size();
                results.values = filteredItems;
            } else {
                results.count = originalList.size();
                results.values = originalList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
            if (filterCompleteListener != null) {
                filterCompleteListener.onFilterComplete(filteredList);
            }
        }
    }
}
