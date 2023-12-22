package com.example.androidlab5;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<CurrencyItem> {
    private List<CurrencyItem> originalData;
    private List<CurrencyItem> filteredData;
    private Filter filter;

    public CustomArrayAdapter(Context context, int resource) {
        super(context, resource);
        originalData = new ArrayList<>();
        filteredData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public CurrencyItem getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CurrencyFilter();
        }
        return filter;
    }

    public void addAll(List<CurrencyItem> items) {
        originalData.clear();
        originalData.addAll(items);
        getFilter().filter("");
    }

    private class CurrencyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            String filter = charSequence.toString().toUpperCase();

            List<CurrencyItem> filteredList = new ArrayList<>();

            for (CurrencyItem item : originalData) {
                if (item.getTargetCurrency().contains(filter)) {
                    filteredList.add(item);
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredData.clear();
            filteredData.addAll((List<CurrencyItem>) filterResults.values);
            notifyDataSetChanged();
        }
    }
}
