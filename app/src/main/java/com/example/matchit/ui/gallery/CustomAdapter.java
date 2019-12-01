package com.example.matchit.ui.gallery;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.matchit.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;

    private FragmentActivity fragmentActivity;
    private LayoutInflater mLayoutInflater;

    private TextView title;

    private ArrayList<SearchResult> mEntries = new ArrayList<SearchResult>();

    public CustomAdapter(Context context){
        fragmentActivity = (FragmentActivity) context;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setClick(View.OnClickListener onClickListener){
        title.setOnClickListener(onClickListener);
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
        final RelativeLayout itemView;
        if(convertView == null){
           itemView = (RelativeLayout) mLayoutInflater.inflate(R.layout.post_list_item, parent, false);
        } else {
            itemView = (RelativeLayout) convertView;
        }
        title = (TextView) itemView.findViewById(R.id.listTitle);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactDialogFragment contactDialogFragment = new ContactDialogFragment();
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                contactDialogFragment.show(fragmentManager, "Dialog box");
//                Toast.makeText(mContext, "Not Working", Toast.LENGTH_SHORT).show();
                Log.d("OnClick","Working fine ");
            }
        });
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
