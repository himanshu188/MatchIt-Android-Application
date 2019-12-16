package com.example.matchit.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.matchit.R;

public class FeedFragment extends Fragment {

    private FeedViewModel feedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        Display the post
        feedViewModel =
                ViewModelProviders.of(this).get(FeedViewModel.class);
        View root = inflater.inflate(R.layout.post_list, container, false);
        return root;
    }
}