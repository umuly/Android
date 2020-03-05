package com.umuly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umuly.R;
import com.umuly.models.SpinnerObject;
import com.umuly.models.response.DomainListResponseObject;

import java.util.List;

public class StatusAdapter extends BaseAdapter {
    private List<SpinnerObject> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public StatusAdapter(List<SpinnerObject> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_long_url_spinner, null);
        TextView title = convertView.findViewById(R.id.item_long_url_spinner_title);

        title.setText(list.get(position).getTitle());
        return convertView;
    }

}
