package com.example.matchit.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.matchit.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

//Adapter for displaying the dynamic post in the home
public class CustomAdapter extends BaseAdapter {
    private Context mContext;

    private FragmentActivity fragmentActivity;
    private LayoutInflater mLayoutInflater;

    private TextView title;

//    List of the Post Entries
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
            }
        });
        TextView content = (TextView) itemView.findViewById(R.id.listContent);

        String titleSearch = mEntries.get(position).title;
        title.setText(titleSearch);
        String contentSearch = mEntries.get(position).content;
        content.setText(contentSearch);
        new DownloadTask(itemView).execute(mEntries.get(position).photo);
        return itemView;
    }

    public void upDateEntries(ArrayList<SearchResult> entries){
        mEntries = entries;
        notifyDataSetChanged();
    }
}
class DownloadTask extends AsyncTask<String, Bitmap, Bitmap> {

    RelativeLayout itemView;
    public DownloadTask(RelativeLayout itemView){
        this.itemView = itemView;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
//        Download the post image from the Amazon S3
        String url = strings[0];
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap result){
//        Display the images in the ImageView
        ImageView imageView = itemView.findViewById(R.id.imageView4);
        imageView.setImageBitmap(result);
    }
}
