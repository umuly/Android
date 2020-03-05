package com.umuly.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umuly.R;
import com.umuly.activity.ChartAndVisitActivity;
import com.umuly.models.response.AllUrlListResponseObject;

import java.util.List;

import static com.umuly.constants.Constants.dateFormat;

public class HistoryUrlAdapter extends BaseAdapter {
    private List<AllUrlListResponseObject> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private String url = "";

    public HistoryUrlAdapter(Context context, List<AllUrlListResponseObject> list) {
        this.context = context;
        this.list = list;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        AllUrlListResponseObject allUrlListResponseObject = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_history_url, parent, false);
            viewHolder.shortUrl = convertView.findViewById(R.id.item_short_url);
            viewHolder.redirecingUrl = convertView.findViewById(R.id.item_redirecing_url);
            viewHolder.title = convertView.findViewById(R.id.item_title);
            viewHolder.desc = convertView.findViewById(R.id.item_desc);
            viewHolder.tags = convertView.findViewById(R.id.item_tags);
            viewHolder.createDate = convertView.findViewById(R.id.item_create_date);
            viewHolder.LLDirectLink = convertView.findViewById(R.id.item_ll_direct_link);
            viewHolder.LLCopyLink = convertView.findViewById(R.id.item_ll_copy_link);
            viewHolder.LLShare = convertView.findViewById(R.id.item_ll_share);
            viewHolder.chartCount = convertView.findViewById(R.id.chart_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.shortUrl.setText(allUrlListResponseObject.getShortUrl());
        viewHolder.redirecingUrl.setText(allUrlListResponseObject.getRedirectUrl());
        viewHolder.title.setText(allUrlListResponseObject.getTitle());
        viewHolder.desc.setText(allUrlListResponseObject.getDescription());
        viewHolder.tags.setText(allUrlListResponseObject.getTags());
        viewHolder.createDate.setText(dateFormat(allUrlListResponseObject.getCreatedOn()));
        viewHolder.chartCount.setText(allUrlListResponseObject.getVisitCount() + "");


        viewHolder.LLDirectLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getShortUrl()));
                context.startActivity(browserIntent);
            }
        });

        viewHolder.LLCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copyText", list.get(position).getShortUrl());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, context.getResources().getString(R.string.link_copy_button), Toast.LENGTH_LONG).show();
            }
        });

        viewHolder.LLShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, list.get(position).getShortUrl());
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        viewHolder.chartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChartAndVisitActivity.class);
                intent.putExtra("shortLinkName", list.get(position).getShortUrl());
                intent.putExtra("shortLinkId", list.get(position).getId());
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    class ViewHolder {
        TextView shortUrl, redirecingUrl, title, desc, tags, createDate, chartCount;
        LinearLayout LLDirectLink, LLCopyLink, LLShare;
    }
}
