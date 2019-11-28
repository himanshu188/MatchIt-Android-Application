package com.example.matchit.ui.home.ui.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.matchit.R;

import java.util.ArrayList;
import java.util.Map;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private ArrayList<SearchResult> mEntries = new ArrayList<SearchResult>();

    public CustomAdapter(Context context){
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return mEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout itemView;
        if(convertView == null){
           itemView = (RelativeLayout) mLayoutInflater.inflate(R.layout.post_list_item, parent, false);
        } else {
            itemView = (RelativeLayout) convertView;
        }
        TextView title = (TextView) itemView.findViewById(R.id.listTitle);
        TextView content = (TextView) itemView.findViewById(R.id.listContent);

        String titleSearch = mEntries.get(0).title.toString();
        title.setText(titleSearch);
        String contentSearch = mEntries.get(0).content.toString();
        content.setText(contentSearch);
        return itemView;
    }

    public void upDateEntries(ArrayList<SearchResult> entries){
        mEntries = entries;
        notifyDataSetChanged();
    }
}
